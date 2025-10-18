package Semantic.ST;

import Lexical.Analyzer.Token;

public class ReferenceType implements Type{
    private final String nameType;
    private final Token token;
    private final Token optionalGeneric;

    public ReferenceType(Token token, Token optionalGeneric){
        this.token = token;
        nameType = token.getLexeme();
        this.optionalGeneric = optionalGeneric;
    }

    @Override
    public Token getTokenType() {
        return token;
    }

    @Override
    public String getNameType() {
        return nameType;
    }

    @Override
    public boolean isCompatible(String neededType) {
        return token.getLexeme().equals(neededType);
    }

    @Override
    public boolean isOperandCompatible(Token typeToken) {
        return false; //TODO revisar el tipo del operador?
    }

    @Override
    public boolean compareTypes(Type type) {
        return token.getTokenName().equals(type.getTokenType().getTokenName());
    }

    public Token getOptionalGeneric() {
        return optionalGeneric;
    }
}