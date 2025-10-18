package Semantic.AST.Expressions;

import Lexical.Analyzer.Token;
import Semantic.ST.PrimitiveType;
import Semantic.ST.Type;

public class EmptyExpressionNode extends ExpressionNode{
    @Override
    public Type check() {
        return new PrimitiveType(new Token("Universal","Universal",0));
    }
}
