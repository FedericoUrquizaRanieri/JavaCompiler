package Semantic;

import Lexical.Analyzer.Token;

public class PrimitiveType implements Type{
    private final String nameType;
    private final Token token;

    public PrimitiveType(Token token){
        this.token = token;
        nameType = token.getLexeme();
    }

    @Override
    public Token getTokenType() {
        return token;
    }

    @Override
    public String getNameType() {
        return nameType;
    }
}
