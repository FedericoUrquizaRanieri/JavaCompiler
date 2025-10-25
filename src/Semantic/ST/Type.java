package Semantic.ST;

import Lexical.Analyzer.Token;
import Semantic.SemExceptions.SemanticException;

public interface Type {
    Token getTokenType();
    String getNameType();
    void isCompatible(String neededType) throws SemanticException;
    void isOperandCompatibleUnary(Token typeToken) throws SemanticException;
    void isOperandCompatibleBinary(Token typeToken, Type typeExp) throws SemanticException;
    void compareTypes(Type type, Token operator) throws SemanticException;
}
