package Semantic;

import Lexical.Analyzer.Token;
import Main.MainSemantic;
import Semantic.SemExceptions.SemanticException;

import java.util.Objects;

public class Parameter {
    private final String name;
    private final Type type;
    private final Token token;

    public Parameter(Type type, Token token){
        this.name = token.getLexeme();
        this.type = type;
        this.token = token;
    }

    public void checkStatements() throws SemanticException {
        if (Objects.equals(type.getTokenType().getTokenName(), "idClase")){
            if (MainSemantic.symbolTable.existsClass(type.getTokenType())==null){
                throw new SemanticException(type.getNameType(),"Se intento usar un parametro de un tipo inexistente ", type.getTokenType().getLine());
            }
        }
    }

    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }

    public Token getToken() {
        return token;
    }
}
