package Semantic.AST.Sentences;

import Semantic.AST.Expressions.ExpressionNode;

public class WhileNode extends SentenceNode{
    private final ExpressionNode condition;
    private final SentenceNode body;

    public WhileNode(ExpressionNode cond, SentenceNode body){
        this.condition = cond;
        this.body = body;
    }

    @Override
    public void check() {
        condition.check().isCompatible("boolean");
        body.check();
    }
}
