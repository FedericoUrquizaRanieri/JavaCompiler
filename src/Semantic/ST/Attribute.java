package Semantic.ST;

import Lexical.Analyzer.Token;
import Main.MainGen;
import Semantic.SemExceptions.SemanticException;

import java.util.Objects;

public class Attribute {
    private final String name;
    private Type type;
    private final Token token;
    private Token genericType;
    private int offset;

    public Attribute(Token token){
        this.name = token.getLexeme();
        this.token =token;
    }

    public void checkStatements() throws SemanticException {
        if(Objects.equals(type.getTokenType().getTokenName(), "idClase")){
            if (MainGen.symbolTable.existsClass(type.getTokenType())==null){
                throw new SemanticException(type.getNameType(),"Se intento agregar un atributo de tipo inexistente ", type.getTokenType().getLine());
            }
        }
    }

    public void generateCode(){

    }

    public Type getType() {
        return type;
    }

    public Token getToken() {
        return token;
    }

    public String getName() {
        return name;
    }

    public Token getGenericType() {
        return genericType;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getOffset() {
        return offset;
    }
}
