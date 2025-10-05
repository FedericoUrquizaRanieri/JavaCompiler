package Semantic;

import Lexical.Analyzer.Token;
import Semantic.SemExceptions.SemanticException;

public class Parameter {
    public String name;
    public Type type;
    public Token token;

    public Parameter(Type type, Token token){
        this.name = token.getLexeme();
        this.type = type;
        this.token = token;
    }

    public void checkStatements() throws SemanticException {
        throw new SemanticException("mock","mock",1);
    }

    public void consolidate() throws SemanticException{
        throw new SemanticException("mock","mock",1);
    }
}
