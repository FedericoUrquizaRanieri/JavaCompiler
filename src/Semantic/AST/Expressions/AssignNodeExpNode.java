package Semantic.AST.Expressions;

import Semantic.ST.Type;
import Semantic.SemExceptions.SemanticException;

public class AssignNodeExpNode extends ExpressionNode {
    private final ExpressionNode leftExpression;
    private final ExpressionNode rightExpression;

    public AssignNodeExpNode(ExpressionNode leftExpression, ExpressionNode rightExpression) {
        this.leftExpression = leftExpression;
        this.rightExpression = rightExpression;
    }

    @Override
    public Type check() throws SemanticException {
        Type retType = leftExpression.check();
        retType.compareTypes(rightExpression.check());
        checked = true;
        return retType;
    }
}
