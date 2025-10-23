package Semantic.AST.Expressions;

import Lexical.Analyzer.Token;
import Semantic.ST.Type;
import Semantic.SemExceptions.SemanticException;

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
    public Type check() throws SemanticException {
        leftExpression.check().isOperandCompatibleBinary(operator, rightExpression.check());
        checked = true;
        //TODO retornar tipo de op segun el operador
        return null;
    }
}
