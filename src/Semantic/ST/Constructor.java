package Semantic.ST;

import Lexical.Analyzer.Token;
import Semantic.AST.Sentences.BlockNode;
import Semantic.SemExceptions.SemanticException;

import java.util.HashMap;
import java.util.Objects;

public class Constructor {
    private final HashMap<String,Parameter> parameters;
    private final Token token;
    private BlockNode block;

    public Constructor(Token token){
        parameters = new HashMap<>();
        this.token = token;
    }

    public void checkStatements(Token classToken) throws SemanticException {
        if (!Objects.equals(token.getLexeme(), classToken.getLexeme())){
            throw new SemanticException(token.getLexeme(),"Nombre incorrecto en constructor ",token.getLine());
        }
        for (Parameter p : parameters.values()){
            p.checkStatements();
        }
    }

    public void addParam(Parameter p) throws SemanticException {
        if( parameters.putIfAbsent(p.getName(),p)!=null)
            throw new SemanticException(p.getName(),"Se intento agregar un parametro repetido llamada ",p.getToken().getLine());
    }

    public void checkSentences() throws SemanticException{
        block.check();
    }

    public Token getToken() {
        return token;
    }

    public void setBlock(BlockNode block){
        this.block=block;
    }

    public HashMap<String, Parameter> getParameters() {
        return parameters;
    }
}
