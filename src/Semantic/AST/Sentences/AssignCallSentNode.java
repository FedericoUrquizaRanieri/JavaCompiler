package Semantic.AST.Sentences;

import Semantic.AST.Expressions.ExpressionNode;

public class AssignCallSentNode extends SentenceNode{
    private final ExpressionNode innerExpression;

    public AssignCallSentNode(ExpressionNode innerExpression) {
        this.innerExpression = innerExpression;
    }

    @Override
    public void check() {
        innerExpression.check();
        checked = true;
    }
}
