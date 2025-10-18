package Semantic.AST.Expressions;

import Lexical.Analyzer.Token;
import Semantic.ST.Type;

public class BinaryExpressionNode extends ComposedExpressionNode{
    private final ComposedExpressionNode leftExpression;
    private final Token operator;
    private final ComposedExpressionNode rightExpression;

    public BinaryExpressionNode(ComposedExpressionNode leftExpression, Token operator, ComposedExpressionNode rightExpression) {
        this.leftExpression = leftExpression;
        this.operator = operator;
        this.rightExpression = rightExpression;
    }

    @Override
    public Type check() {
        if (leftExpression.check().compareTypes(rightExpression.check()) && leftExpression.check().isOperandCompatible(operator)){
            checked = true;
        } else ;//TODO throw
        return null;
    }
}
