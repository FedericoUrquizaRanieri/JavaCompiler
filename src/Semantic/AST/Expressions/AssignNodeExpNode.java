package Semantic.AST.Expressions;

import Semantic.ST.Type;

public class AssignNodeExpNode extends ExpressionNode {
    private final ExpressionNode leftExpression;
    private final ExpressionNode rightExpression;

    public AssignNodeExpNode(ExpressionNode leftExpression, ExpressionNode rightExpression) {
        this.leftExpression = leftExpression;
        this.rightExpression = rightExpression;
    }

    @Override
    public Type check() {
        if (leftExpression.check().compareTypes(rightExpression.check())){
            checked = true;
        } else ;//TODO throw
        return leftExpression.check();
    }
}
