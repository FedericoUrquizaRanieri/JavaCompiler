package Semantic.ST;

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
}
