package Semantic;

import Lexical.Analyzer.Token;

public class ReferenceType implements Type{
    public String nameType;
    public Token token;
    public Token optionalGeneric;

    public ReferenceType(Token token, Token optionalGeneric){
        this.token = token;
        nameType = token.getLexeme();
        this.optionalGeneric = optionalGeneric;
    }

    @Override
    public Token getTokenType() {
        return token;
    }
}