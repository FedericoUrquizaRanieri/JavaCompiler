package Semantic;

import Lexical.Analyzer.Token;
import Semantic.SemExceptions.SemanticException;

import java.util.HashMap;
import java.util.Objects;

public class Constructor {
    public HashMap<String,Parameter> parameters;
    public Token token;

    public Constructor(Token token){
        parameters = new HashMap<>();
        this.token = token;
    }

    public void checkStatements(Token classToken) throws SemanticException {
        if (!Objects.equals(token.getLexeme(), classToken.getLexeme())){
            throw new SemanticException(token.getLexeme(),"Se intento agregar un parametro repetido llamada ",token.getLine());
        }
        for (Parameter p : parameters.values()){
            p.checkStatements();
        }
    }

    public void addParam(Parameter p) throws SemanticException {
        if( parameters.putIfAbsent(p.name,p)!=null)
            throw new SemanticException(p.name,"Se intento agregar un parametro repetido llamada ",p.token.getLine());
    }
}
