package Semantic;

import Lexical.Analyzer.Token;
import Semantic.SemExceptions.SemanticException;

public class Attribute {
    public String name;
    public Type type;
    public Token token;
    public Token genericType;

    public Attribute(Token token){
        this.name = token.getLexeme();
        this.token =token;
    }

    public void checkStatements() throws SemanticException {
        throw new SemanticException("mock","mock",1);
    }

    public void consolidate() throws SemanticException{
        throw new SemanticException("mock","mock",1);
    }
}
