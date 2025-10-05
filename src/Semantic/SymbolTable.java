package Semantic;

import Semantic.SemExceptions.SemanticException;

import java.util.HashMap;

public class SymbolTable {
    public HashMap<String,Class> classes;
    public Method currentMethod;
    public Class currentClass;
    public Constructor currentConstructor;

    public SymbolTable(){
        classes = new HashMap<>();
        //TODO meter object string y system?
    }

    public void checkStatements() throws SemanticException{
        throw new SemanticException("mock","mock",1);
    }

    public void consolidate() throws SemanticException{
        throw new SemanticException("mock","mock",1);
    }

    public void addClass(String name, Class classElement) throws SemanticException {
        if(classes.putIfAbsent(name,classElement)!=null)
            throw new SemanticException(name,"Se intento agregar una clase repetida llamada ",classElement.classToken.getLine());
    }
}