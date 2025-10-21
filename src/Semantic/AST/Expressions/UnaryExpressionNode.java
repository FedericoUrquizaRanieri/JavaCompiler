package Semantic.AST.Expressions;

import Lexical.Analyzer.Token;
import Semantic.ST.Type;

public class UnaryExpressionNode extends ComposedExpressionNode{
    private final Token operator;
    private final ExpressionNode expression;

    public UnaryExpressionNode(ExpressionNode expression, Token operator) {
        this.expression = expression;
        this.operator = operator;
    }

    @Override
    public Type check() {
        if (expression.check().isOperandCompatible(operator)){
            checked = true;
        } else;//TODO throw
        return null;
    }
}
