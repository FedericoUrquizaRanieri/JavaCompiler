package Semantic;

import Lexical.Analyzer.Token;
import Main.MainSemantic;
import Semantic.SemExceptions.SemanticException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class Class {
    public String className;
    public Token classToken;
    public Token inheritance;
    public Token generics;
    public Token inheritanceGenerics;
    public Token modifierClass;
    public HashMap<String, Attribute> attributes;
    public HashMap<String, Method> methods;
    public HashMap<String, Constructor> constructors;
    private boolean isConsolidated = false;

    public Class(Token token){
        attributes = new HashMap<>();
        methods = new HashMap<>();
        constructors = new HashMap<>();
        this.className = token.getLexeme();
        this.classToken = token;
        if(Objects.equals(className, "Object"))
            isConsolidated = true;
    }

    public void checkStatements() throws SemanticException {
        if (inheritance!=null) {
            Class confirmedFather = MainSemantic.symbolTable.existsClass(inheritance);
            if (confirmedFather != null) {
                if (this == confirmedFather)
                    throw new SemanticException(inheritance.getLexeme(), "La clase es igual a la clase padre ", inheritance.getLine());
                if (confirmedFather.isFinalOrStatic()) {
                    throw new SemanticException(inheritance.getLexeme(), "Se intenta heredar de una clase final o estatica ", inheritance.getLine());
                }
                List<Class> ancestors = new ArrayList<>(List.of(this, confirmedFather));
                if (circularInheritance(ancestors, confirmedFather)) {
                    throw new SemanticException(inheritance.getLexeme(), "Se genera herencia circular con ", inheritance.getLine());
                }
            } else {
                throw new SemanticException(inheritance.getLexeme(), "No existe la clase padre ", inheritance.getLine());
            }
        }
        for (Attribute a : attributes.values()){
            a.checkStatements();
        }
        for (Constructor c : constructors.values()){
            c.checkStatements(classToken);
        }
        for (Method m : methods.values()){
            m.checkStatements();
            if (modifierClass!=null){
                if (!(Objects.equals(modifierClass.getTokenName(), "pr_abstract")) && Objects.equals(m.modifier.getTokenName(), "pr_abstract")){
                    throw new SemanticException(m.modifier.getLexeme(),"En una clase comun no es posible tener un metodo abstracto ",m.modifier.getLine());
                }
            }
        }
    }

    private boolean isFinalOrStatic() {
        if (modifierClass==null)
            return false;
        else return Objects.equals(modifierClass.getTokenName(), "pr_static") || Objects.equals(modifierClass.getTokenName(), "pr_final");
    }

    public void consolidate() throws SemanticException{
        Class confirmedFather;
        if(inheritance!=null)
            confirmedFather = MainSemantic.symbolTable.existsClass(inheritance);
        else
            confirmedFather = MainSemantic.symbolTable.classes.get("Object");
        if(!confirmedFather.isConsolidated())
            confirmedFather.consolidate();
        consolidateMethods();
        consolidateAttributes();
        if (constructors.isEmpty()){
            constructors.put(className,new Constructor(new Token("idClase",className,0)));
        }
        isConsolidated = true;
    }

    private boolean isConsolidated(){
        return isConsolidated;
    }

    private void consolidateAttributes() throws SemanticException {
        if (inheritance!=null){
            Class confirmedFather = MainSemantic.symbolTable.existsClass(inheritance);
            HashMap<String, Attribute> newAttributes = confirmedFather.attributes;
            for(Attribute a:attributes.values()){
                if(newAttributes.putIfAbsent(a.name,a)!=null)
                    throw new SemanticException(a.name,"Se intento cambiar un atributo del padre en ",a.token.getLine());
            }
            attributes = newAttributes;
        }
    }

    private void consolidateMethods() throws SemanticException {
        if (inheritance!=null){
            Class confirmedFather = MainSemantic.symbolTable.existsClass(inheritance);
            HashMap<String, Method> newMethods = confirmedFather.methods;
            for(Method m : methods.values()){
                Method fatherMethod = newMethods.put(m.name,m);
                if(fatherMethod!=null){
                    if(fatherMethod.modifier!=null){
                        if (Objects.equals(fatherMethod.modifier.getTokenName(), "pr_final")){
                            throw new SemanticException(m.name,"Se intento sobreescribir un metodo final en ",m.token.getLine());
                        }
                        if (Objects.equals(fatherMethod.modifier.getTokenName(), "pr_abstract") && !m.block){
                            throw new SemanticException(m.name,"No se completo un metodo abstracto en ",m.token.getLine());
                        }
                    }
                    if(!fatherMethod.parameters.equals(m.parameters)){
                        throw new SemanticException(m.name,"Se intento sobreescribir un metodo de manera incorrecta en ",m.token.getLine());
                    }
                }
                if(newMethods.putIfAbsent(m.name,m)!=null)
                    throw new SemanticException(m.name,"Se intento cambiar un atributo del padre en ",m.token.getLine());
            }
            methods = newMethods;
        }
    }

    public boolean circularInheritance(List<Class> ancestors, Class element){
        if (element.inheritance!=null) {
            Class father = MainSemantic.symbolTable.existsClass(element.inheritance);
            if (father == null)
                return false;
            else if (ancestors.contains(father)) {
                return true;
            } else {
                ancestors.add(father);
                return circularInheritance(ancestors,father);
            }
        } else return false;
    }

    public void addConstructor(Constructor c) throws SemanticException {
        if(constructors.putIfAbsent(c.token.getLexeme(),c)!=null)
            throw new SemanticException(c.token.getLexeme(),"Se intento agregar un atributo repetido llamada ",c.token.getLine());
    }

    public void addMethod(Method m) throws SemanticException {
        if(methods.putIfAbsent(m.name,m)!=null)
            throw new SemanticException(m.name,"Se intento agregar un atributo repetido llamada ",m.token.getLine());
    }

    public void addAttribute(Attribute a) throws SemanticException {
        if(attributes.putIfAbsent(a.name,a)!=null)
            throw new SemanticException(a.name,"Se intento agregar un atributo repetido llamada ",a.token.getLine());
    }
}