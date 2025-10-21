package Semantic.AST.Expressions;

import Lexical.Analyzer.Token;
import Semantic.ST.Type;

public class BinaryExpressionNode extends ComposedExpressionNode{
    private final ExpressionNode leftExpression;
    private final Token operator;
    private final ExpressionNode rightExpression;

    public BinaryExpressionNode(ExpressionNode leftExpression, Token operator, ExpressionNode rightExpression) {
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
