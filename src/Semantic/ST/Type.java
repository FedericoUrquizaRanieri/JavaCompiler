package Semantic.ST;

import Lexical.Analyzer.Token;

public interface Type {
    Token getTokenType();
    String getNameType();
    boolean isCompatible(String neededType);
    boolean isOperandCompatible(Token typeToken);
    boolean compareTypes(Type type);
}
