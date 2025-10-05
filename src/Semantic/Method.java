package Semantic;

import Lexical.Analyzer.Token;
import Semantic.SemExceptions.SemanticException;

import java.util.HashMap;

public class Method {
    public String name;
    public Type returnType;
    public Token token;
    public Token modifier;
    public HashMap<String,Parameter> parameters;

    public Method(Token token){
        parameters = new HashMap<>();
        this.name = token.getLexeme();
        this.token = token;
    }

    public void checkStatements() throws SemanticException {
        throw new SemanticException("mock","mock",1);
    }

    public void consolidate() throws SemanticException{
        throw new SemanticException("mock","mock",1);
    }

    public void addParam(Parameter p) throws SemanticException {
        if( parameters.putIfAbsent(p.name,p)!=null)
            throw new SemanticException(p.name,"Se intento agregar un parametro repetido llamada ",p.token.getLine());
    }
}
