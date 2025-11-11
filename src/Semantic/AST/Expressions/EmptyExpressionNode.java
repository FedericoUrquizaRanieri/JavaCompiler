package Semantic.AST.Expressions;

import Lexical.Analyzer.Token;
import Semantic.ST.PrimitiveType;
import Semantic.ST.Type;
import Semantic.SemExceptions.SemanticException;

public class EmptyExpressionNode extends ExpressionNode{
    @Override
    public Type check() throws SemanticException {
        return new PrimitiveType(new Token("Universal","Universal",0));
    }

    @Override
    public void generateCode() {}
}
