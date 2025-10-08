package Semantic;

import Lexical.Analyzer.Token;
import Main.MainSemantic;
import Semantic.SemExceptions.SemanticException;

import java.util.LinkedHashMap;
import java.util.Objects;

public class Method {
    public String name;
    public Type returnType;
    public Token token;
    public Token modifier;
    public LinkedHashMap<String,Parameter> parameters;
    public boolean block;

    public Method(Token token){
        parameters = new LinkedHashMap<>();
        this.name = token.getLexeme();
        this.token = token;
    }

    public void checkStatements() throws SemanticException {
        if(returnType!=null){
            if(Objects.equals(returnType.getTokenType().getTokenName(), "idClase")){
                if (MainSemantic.symbolTable.existsClass(returnType.getTokenType())==null){
                    throw new SemanticException(returnType.getTokenType().getLexeme(),"Se intento agregar un tipo de retorno inexistente ",returnType.getTokenType().getLine());
                }
            }
        }
        if(block && modifier!=null && Objects.equals(modifier.getTokenName(), "pr_abstract")){
            throw new SemanticException(token.getLexeme(), "Se intento agregar un bloque a un metodo ", token.getLine());
        }
        if(!block && (modifier == null || !Objects.equals(modifier.getTokenName(), "pr_abstract"))){
            throw new SemanticException(token.getLexeme(), "Se intento agregar un metodo sin bloque ", token.getLine());
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
