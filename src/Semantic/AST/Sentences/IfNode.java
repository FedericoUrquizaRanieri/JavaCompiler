package Semantic.AST.Sentences;

import Lexical.Analyzer.Token;
import Semantic.AST.Expressions.ExpressionNode;
import Semantic.SemExceptions.SemanticException;

public class IfNode extends SentenceNode{
    private final ExpressionNode condition;
    private final SentenceNode body;
    private final SentenceNode elseSentence;

    public IfNode(ExpressionNode cond, SentenceNode body,SentenceNode elseS){
        this.condition = cond;
        this.body = body;
        this.elseSentence = elseS;
    }

    @Override
    public void check() throws SemanticException {
        condition.check().isCompatible("boolean");
        body.check();
        elseSentence.check();
        checked = true;
    }
}
