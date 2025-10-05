package Semantic;

import Lexical.Analyzer.Token;

public class PrimitiveType implements Type{
    public String nameType;
    public Token token;

    public PrimitiveType(Token token){
        this.token = token;
        nameType = token.getLexeme();
    }
}
