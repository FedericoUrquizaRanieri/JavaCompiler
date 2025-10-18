package Semantic.AST.Sentences;

import Semantic.AST.Expressions.ExpressionNode;

public class CallNode extends SentenceNode{
    private final ExpressionNode callExpression;

    public CallNode(ExpressionNode e){
        callExpression =e;
    }

    @Override
    public void check() {
        callExpression.check();
    }
}
