package Semantic;

import Semantic.SemExceptions.SemanticException;
import java.util.HashSet;

public class SymbolTable {
    private HashSet<Class> classes;

    public SymbolTable(){
        classes = new HashSet<>();
    }

    public void checkStatements() throws SemanticException{
        throw new SemanticException("mock","mock",1);
    }
}
