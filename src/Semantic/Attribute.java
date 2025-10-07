package Semantic;

import Lexical.Analyzer.Token;
import Main.MainSemantic;
import Semantic.SemExceptions.SemanticException;

import java.util.Objects;

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
        if(Objects.equals(type.getTokenType().getTokenName(), "idClase")){
            if (MainSemantic.symbolTable.existsClass(type.getTokenType())==null){
                throw new SemanticException(name,"Se intento agregar un atributo de tipo inexistente ", token.getLine());
            }
        }
    }
}
