package Semantic;

import Lexical.Analyzer.Token;
import Main.MainSemantic;
import Semantic.SemExceptions.SemanticException;

import java.util.*;

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
                    throw new SemanticException(classToken.getLexeme(), "Se intenta heredar de una clase final o estatica en ", classToken.getLine());
                }
                List<Class> ancestors = new ArrayList<>(List.of(this, confirmedFather));
                if (circularInheritance(ancestors, confirmedFather)) {
                    throw new SemanticException(inheritance.getLexeme(), "Se genera herencia circular con ", inheritance.getLine());
                }
                if (confirmedFather.modifierClass!=null && Objects.equals(confirmedFather.modifierClass.getTokenName(), "pr_abstract")){
                    for (Method m : confirmedFather.methods.values()){
                        if (Objects.equals(m.modifier.getTokenName(), "pr_abstract") && methods.get(m.name)==null){
                            throw new SemanticException(m.token.getLexeme(), "No se implementa el metodo abstracto ", m.token.getLine());
                        } else if (Objects.equals(m.modifier.getTokenName(), "pr_abstract") && !methods.get(m.name).block){
                            throw new SemanticException(m.token.getLexeme(), "No se implementa con bloque el metodo abstracto  ", m.token.getLine());
                        }
                    }
                }
                if (this.modifierClass!=null){
                    if (Objects.equals(this.modifierClass.getTokenName(), "pr_abstract") && confirmedFather.modifierClass==null){
                        throw new SemanticException(classToken.getLexeme(), "No es posible heredar de una clase concreta en la abstracta ", classToken.getLine());
                    }
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
            if (modifierClass!=null){
                if ((Objects.equals(modifierClass.getTokenName(), "pr_abstract"))){
                    throw new SemanticException(c.token.getLexeme(),"En una clase abstracta no es posible tener un constructor ",c.token.getLine());
                }
            }
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
        else {
            confirmedFather = MainSemantic.symbolTable.classes.get("Object");
            if(!Objects.equals(this.className, "Object")){
                this.inheritance=confirmedFather.classToken;
            }
        }
        if(confirmedFather.isNotConsolidated())
            confirmedFather.consolidate();
        consolidateMethods();
        consolidateAttributes();
        if (constructors.isEmpty()){
            constructors.put(className,new Constructor(new Token("idClase",className,0)));
        }
        isConsolidated = true;
    }

    public boolean isNotConsolidated(){
        return !isConsolidated;
    }

    private void consolidateAttributes() throws SemanticException {
        if (inheritance!=null){
            Class confirmedFather = MainSemantic.symbolTable.existsClass(inheritance);
            HashMap<String, Attribute> newAttributes = new HashMap<>(confirmedFather.attributes);
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
            HashMap<String, Method> newMethods = new HashMap<>(confirmedFather.methods);
            for(Method m : methods.values()){
                Method fatherMethod = newMethods.put(m.name,m);
                if(fatherMethod!=null){
                    if(fatherMethod.modifier!=null){
                        if (Objects.equals(fatherMethod.modifier.getTokenName(), "pr_final")){
                            throw new SemanticException(m.name,"Se intento sobreescribir un metodo final en ",m.token.getLine());
                        }
                        if (Objects.equals(fatherMethod.modifier.getTokenName(), "pr_static")){
                            throw new SemanticException(m.name,"Se intento sobreescribir un metodo static en ",m.token.getLine());
                        }
                        if (Objects.equals(fatherMethod.modifier.getTokenName(), "pr_abstract") && !m.block){
                            throw new SemanticException(m.name,"No se completo un metodo abstracto en ",m.token.getLine());
                        }
                    }
                    if(!compareParams(fatherMethod.parameters,m.parameters)){
                        throw new SemanticException(m.name,"Se intento sobreescribir un metodo de manera incorrecta en ",m.token.getLine());
                    }
                    if(differentReturnTypes(fatherMethod,m)){
                        throw new SemanticException(m.name,"Se intento sobreescribir un metodo de manera incorrecta en ",m.token.getLine());
                    }
                }
            }
            methods = newMethods;
        }
    }

    private boolean differentReturnTypes(Method m1,Method m2){
        if(m1.returnType!= null && m2.returnType!=null){
            return !Objects.equals(m1.returnType.getTokenType().getTokenName(), m2.returnType.getTokenType().getTokenName());
        } else return !(m1.returnType == null && m2.returnType == null);
    }

    private boolean compareParams(LinkedHashMap<String,Parameter> fatherParams, LinkedHashMap<String,Parameter> sonParams){
        if (fatherParams.size()!=sonParams.size()){
            return false;
        }
        Iterator<Map.Entry<String, Parameter>> itFather = fatherParams.entrySet().iterator();
        Iterator<Map.Entry<String, Parameter>> itSon = sonParams.entrySet().iterator();

        while (itFather.hasNext() && itSon.hasNext()) {
            Map.Entry<String, Parameter> fatherEntry = itFather.next();
            Map.Entry<String, Parameter> sonEntry = itSon.next();

            Parameter fatherParam = fatherEntry.getValue();
            Parameter sonParam = sonEntry.getValue();

            if (!Objects.equals(fatherParam.name, sonParam.name) || !Objects.equals(fatherParam.type.getTokenType().getTokenName(), sonParam.type.getTokenType().getTokenName())) {
                return false;
            }
        }
        return true;
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