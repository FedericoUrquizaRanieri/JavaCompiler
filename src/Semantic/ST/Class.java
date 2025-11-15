package Semantic.ST;

import Lexical.Analyzer.Token;
import Main.MainGen;
import Semantic.AST.Sentences.NullBlockNode;
import Semantic.SemExceptions.SemanticException;

import java.util.*;

public class Class {
    private final String className;
    private final Token classToken;
    private Token inheritance;
    private Token generics;
    private Token inheritanceGenerics;
    private Token modifierClass;
    private HashMap<String, Attribute> attributes;
    private HashMap<String, Method> methods;
    private final HashMap<String, Constructor> constructors;
    private boolean isConsolidated = false;
    private boolean attributesNumbered = false;
    private boolean methodsNumbered = false;
    private int lastAttributeOffset;
    private int lastMethodOffset;


    public Class(Token token){
        attributes = new HashMap<>();
        methods = new HashMap<>();
        constructors = new HashMap<>();
        this.className = token.getLexeme();
        this.classToken = token;
        if(Objects.equals(className, "Object"))
            isConsolidated = true;
        lastMethodOffset = 0;
        lastAttributeOffset = 0;
    }

    public void checkStatements() throws SemanticException {
        if (inheritance!=null) {
            Class confirmedFather = MainGen.symbolTable.existsClass(inheritance);
            if (confirmedFather != null) {
                if (this == confirmedFather)
                    throw new SemanticException(inheritance.getLexeme(), "La clase actual es igual a la clase padre ", inheritance.getLine());
                if (confirmedFather.isFinalOrStatic()) {
                    throw new SemanticException(classToken.getLexeme(), "Se intenta heredar de una clase final o estatica en ", classToken.getLine());
                }
                List<Class> ancestors = new ArrayList<>(List.of(this, confirmedFather));
                if (circularInheritance(ancestors, confirmedFather)) {
                    throw new SemanticException(inheritance.getLexeme(), "Se genera herencia circular con ", inheritance.getLine());
                }
                if (confirmedFather.modifierClass!=null && Objects.equals(confirmedFather.modifierClass.getTokenName(), "pr_abstract")){
                    for (Method m : confirmedFather.methods.values()){
                        if (m.getModifier() != null) {
                            if (Objects.equals(m.getModifier().getTokenName(), "pr_abstract") && methods.get(m.getName()) == null) {
                                throw new SemanticException(m.getToken().getLexeme(), "No se implementa el metodo abstracto ", m.getToken().getLine());
                            } else if (Objects.equals(m.getModifier().getTokenName(), "pr_abstract") && methods.get(m.getName()).hasNoBlock() && m.getModifier() != null && !Objects.equals(m.getModifier().getTokenName(), "pr_abstract")) {
                                throw new SemanticException(m.getToken().getLexeme(), "No se implementa con bloque el metodo abstracto  ", m.getToken().getLine());
                            }
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
                    throw new SemanticException(c.getToken().getLexeme(),"En una clase abstracta no es posible tener un constructor ",c.getToken().getLine());
                }
            }
        }
        for (Method m : methods.values()){
            m.checkStatements();
            if (modifierClass == null || !(Objects.equals(modifierClass.getTokenName(), "pr_abstract"))){
                if (m.getModifier()!=null && Objects.equals(m.getModifier().getTokenName(), "pr_abstract")){
                    throw new SemanticException(m.getModifier().getLexeme(),"En una clase comun no es posible tener un metodo abstracto ",m.getModifier().getLine());
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
            confirmedFather = MainGen.symbolTable.existsClass(inheritance);
        else {
            confirmedFather = MainGen.symbolTable.classes.get("Object");
            if(!Objects.equals(this.className, "Object")){
                this.inheritance=confirmedFather.classToken;
            }
        }
        if(confirmedFather.isNotConsolidated())
            confirmedFather.consolidate();
        consolidateMethods();
        consolidateAttributes();
        if (constructors.isEmpty()){
            Constructor c = new Constructor(new Token("idClase",className,0));
            c.setBlock(new NullBlockNode());
            constructors.put(className,c);
        }
        isConsolidated = true;
    }

    public boolean isNotConsolidated(){
        return !isConsolidated;
    }

    private void consolidateAttributes() throws SemanticException {
        if (inheritance!=null){
            Class confirmedFather = MainGen.symbolTable.existsClass(inheritance);
            HashMap<String, Attribute> newAttributes = new HashMap<>(confirmedFather.attributes);
            for(Attribute a:attributes.values()){
                if(newAttributes.putIfAbsent(a.getName(),a)!=null)
                    throw new SemanticException(a.getName(),"Se intento cambiar un atributo del padre en ",a.getToken().getLine());
            }
            attributes = newAttributes;
        }
    }

    private void consolidateMethods() throws SemanticException {
        if (inheritance!=null){
            Class confirmedFather = MainGen.symbolTable.existsClass(inheritance);
            HashMap<String, Method> newMethods = new HashMap<>(confirmedFather.methods);
            for(Method m : methods.values()){
                Method fatherMethod = newMethods.put(m.getName(),m);
                if(fatherMethod!=null){
                    if(fatherMethod.getModifier()!=null){
                        if (Objects.equals(fatherMethod.getModifier().getTokenName(), "pr_final")){
                            throw new SemanticException(m.getName(),"Se intento sobreescribir un metodo final en ",m.getToken().getLine());
                        }
                        if (Objects.equals(fatherMethod.getModifier().getTokenName(), "pr_static")){
                            throw new SemanticException(m.getName(),"Se intento sobreescribir un metodo static en ",m.getToken().getLine());
                        }
                        if (Objects.equals(fatherMethod.getModifier().getTokenName(), "pr_abstract") && m.hasNoBlock() && m.getModifier()!=null && !Objects.equals(m.getModifier().getTokenName(), "pr_abstract")){
                            throw new SemanticException(m.getName(),"No se completo un metodo abstracto en ",m.getToken().getLine());
                        }
                    }
                    if (fatherMethod.getModifier()==null && m.getModifier()!=null){
                        throw new SemanticException(m.getName(),"Se intento sobreescribir un metodo de manera incorrecta en ",m.getToken().getLine());
                    }
                    if(!compareParams(fatherMethod.getParameters(),m.getParameters())){
                        throw new SemanticException(m.getName(),"Se intento sobreescribir un metodo de manera incorrecta en ",m.getToken().getLine());
                    }
                    if(differentReturnTypes(fatherMethod,m)){
                        throw new SemanticException(m.getName(),"Se intento sobreescribir un metodo de manera incorrecta en ",m.getToken().getLine());
                    }
                }
            }
            methods = newMethods;
        }
    }

    private boolean differentReturnTypes(Method m1,Method m2){
        if(m1.getReturnType()!= null && m2.getReturnType()!=null){
            return !Objects.equals(m1.getReturnType().getTokenType().getTokenName(), m2.getReturnType().getTokenType().getTokenName());
        } else return !(m1.getReturnType() == null && m2.getReturnType() == null);
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

            if (!Objects.equals(fatherParam.getName(), sonParam.getName()) || !Objects.equals(fatherParam.getType().getTokenType().getTokenName(), sonParam.getType().getTokenType().getTokenName())) {
                return false;
            }
        }
        return true;
    }

    public boolean circularInheritance(List<Class> ancestors, Class element){
        if (element.inheritance!=null) {
            Class father = MainGen.symbolTable.existsClass(element.inheritance);
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

    public void checkSentences() throws SemanticException{
        for (Method m : methods.values()) {
            m.checkSentences();
        }
        for (Constructor c : constructors.values()) {
            c.checkSentences();
        }
    }

    public void addConstructor(Constructor c) throws SemanticException {
        if(constructors.isEmpty()){
            if(constructors.putIfAbsent(c.getToken().getLexeme(),c)!=null)
                throw new SemanticException(c.getToken().getLexeme(),"Se intento agregar un constructor repetido llamado ",c.getToken().getLine());
        } else {
            throw new SemanticException(c.getToken().getLexeme(),"Se intento agregar un constructor repetido llamado ",c.getToken().getLine());
        }
    }

    public void generateCode(){
        generateVT();
        MainGen.symbolTable.instructionsList.add(".CODE");
        for(Constructor c: constructors.values()){
            c.generateCode();
        }
        for(Method m : methods.values()){
            m.generateCode(className);
        }
    }

    public void generateVT(){
        HashMap<Integer, String> methodsLabelByOffset = new HashMap<>();
        for (Method m : methods.values()) {
            if (m.getModifier() != null && !m.getModifier().getLexeme().equals("static"))
                methodsLabelByOffset.put(m.getOffset(), m.getName());
        }

        if (!methodsLabelByOffset.isEmpty()){
            MainGen.symbolTable.instructionsList.add(".DATA");
            StringBuilder methodsLabels = new StringBuilder();
            for (int i = 0; i < getLastMethodOffset(); i++) {
                if (methodsLabelByOffset.get(i) != null)
                    methodsLabels.append(methodsLabelByOffset.get(i));
                else methodsLabels.append("0");
                if (i != getLastMethodOffset()-1)
                    methodsLabels.append(",");
            }
            MainGen.symbolTable.instructionsList.add("lblVT"+classToken.getLexeme()+": DW "+methodsLabels);
        } else {
            MainGen.symbolTable.instructionsList.add(".DATA");
            MainGen.symbolTable.instructionsList.add("lblVT"+classToken.getLexeme()+": NOP");
        }
        MainGen.symbolTable.instructionsList.add("");
    }

    public void setAttributeOffsets(){
        if (!attributesNumbered){
            Class fatherClass = MainGen.symbolTable.classes.get(inheritance.getLexeme());
            if (!inheritance.getLexeme().equals("Object")){
                fatherClass.setAttributeOffsets();
            }
            lastAttributeOffset = fatherClass.getLastAttributeOffset();
            for (Attribute a: attributes.values())
                if (fatherClass.attributes.get(a.getName())==null){
                    a.setOffset(lastAttributeOffset++);
                } else {
                    a.setOffset(fatherClass.attributes.get(a.getName()).getOffset());
                }
            attributesNumbered = true;
        }
    }

    public void setMethodOffsets(){
        if (!methodsNumbered){
            Class fatherClass = MainGen.symbolTable.classes.get(inheritance.getLexeme());
            if (!inheritance.getLexeme().equals("Object")){
                fatherClass.setMethodOffsets();
            }
            lastMethodOffset = fatherClass.getLastMethodOffset();
            for (Method m: methods.values())
                if (m.getModifier() != null && !m.getModifier().getLexeme().equals("static"))
                    if (fatherClass.methods.get(m.getName())==null){
                       m.setOffset(lastMethodOffset++);
                    } else {
                        m.setOffset(fatherClass.methods.get(m.getName()).getOffset());
                    }
            methodsNumbered = true;
        }
    }

    public void addMethod(Method m) throws SemanticException {
        if(methods.putIfAbsent(m.getName(),m)!=null)
            throw new SemanticException(m.getName(),"Se intento agregar un metodo repetido llamado ",m.getToken().getLine());
    }

    public void addAttribute(Attribute a) throws SemanticException {
        if(attributes.putIfAbsent(a.getName(),a)!=null)
            throw new SemanticException(a.getName(),"Se intento agregar un atributo repetido llamada ",a.getToken().getLine());
    }

    public Token getClassToken() {
        return classToken;
    }

    public String getClassName() {
        return className;
    }

    public Token getInheritance() {
        return inheritance;
    }

    public Token getModifierClass() {
        return modifierClass;
    }

    public HashMap<String, Method> getMethods() {
        return methods;
    }

    public void setInheritance(Token inheritance) {
        this.inheritance = inheritance;
    }

    public void setModifierClass(Token modifierClass) {
        this.modifierClass = modifierClass;
    }

    public void setGenerics(Token generics) {
        this.generics = generics;
    }

    public HashMap<String, Constructor> getConstructors() {
        return constructors;
    }

    public HashMap<String, Attribute> getAttributes() {
        return attributes;
    }

    public int getLastAttributeOffset() {
        return lastAttributeOffset;
    }

    public int getLastMethodOffset() {
        return lastMethodOffset;
    }

    public void attributesNumbered() {
        attributesNumbered = true;
    }
    public void methodsNumbered() {
        methodsNumbered = true;
    }
}