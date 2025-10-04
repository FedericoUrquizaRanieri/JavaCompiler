package Semantic;

import Semantic.SemExceptions.SemanticException;
import java.util.HashSet;

public class SymbolTable {
    private HashSet<Class> classes;
    private Method currentMethod;
    private Class currentClass;

    public SymbolTable(){
        classes = new HashSet<>();
        //TODO meter object?
    }

    public void checkStatements() throws SemanticException{
        throw new SemanticException("mock","mock",1);
    }
}
