package Semantic.AST.Sentences;

import Semantic.AST.Expressions.ComposedExpressionNode;
import Semantic.AST.Expressions.ExpressionNode;
import Semantic.SemExceptions.SemanticException;

public class AssignNode extends SentenceNode{
    private final ComposedExpressionNode leftExpression;
    private final ExpressionNode rightExpression;

    public AssignNode(ComposedExpressionNode leftExpression, ExpressionNode rightExpression) {
        this.leftExpression = leftExpression;
        this.rightExpression = rightExpression;
    }

    @Override
    public void check() {
        if (leftExpression.check().compareTypes(rightExpression.check())){
            checked = true;
        } else ;//TODO throw
    }
}
