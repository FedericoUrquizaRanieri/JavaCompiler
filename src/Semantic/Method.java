package Semantic;

import Lexical.Analyzer.Token;
import Main.MainSemantic;
import Semantic.SemExceptions.SemanticException;

import java.util.HashMap;
import java.util.Objects;

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
        if(Objects.equals(returnType.getTokenType().getTokenName(), "pr_class")){
            if (MainSemantic.symbolTable.existsClass(returnType.getTokenType())==null){
                throw new SemanticException(returnType.getTokenType().getLexeme(),"Se intento agregar un tipo de retorno inexistente ",returnType.getTokenType().getLine());
            }
        }
        for (Parameter p : parameters.values()){
            p.checkStatements();
        }
    }

    public void consolidate() throws SemanticException{
        throw new SemanticException("mock","mock",1);
    }

    public void addParam(Parameter p) throws SemanticException {
        if( parameters.putIfAbsent(p.name,p)!=null)
            throw new SemanticException(p.name,"Se intento agregar un parametro repetido llamada ",p.token.getLine());
    }
}
