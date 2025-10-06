package Semantic;

import Lexical.Analyzer.Token;
import Main.MainSemantic;
import Semantic.SemExceptions.SemanticException;

import java.util.Objects;

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
        if (Objects.equals(type.getTokenType().getTokenName(), "pr_class")){
            if (MainSemantic.symbolTable.existsClass(type.getTokenType())==null){
                throw new SemanticException(type.getTokenType().getLexeme(),"Se intento usar un parametro de un tipo inexistente ",type.getTokenType().getLine());
            }
        }
    }
}
