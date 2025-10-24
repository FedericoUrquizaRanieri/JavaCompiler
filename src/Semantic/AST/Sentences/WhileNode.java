package Semantic.AST.Sentences;

import Lexical.Analyzer.Token;
import Semantic.AST.Expressions.ExpressionNode;
import Semantic.SemExceptions.SemanticException;

public class WhileNode extends SentenceNode{
    private final ExpressionNode condition;
    private final SentenceNode body;

    public WhileNode(ExpressionNode cond, SentenceNode body){
        this.condition = cond;
        this.body = body;
    }

    @Override
    public void check() throws SemanticException{
        condition.check().isCompatible("boolean");
        body.check();
        checked = true;
    }
}
