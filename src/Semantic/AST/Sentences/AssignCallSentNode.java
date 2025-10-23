package Semantic.AST.Sentences;

import Semantic.AST.Expressions.ExpressionNode;
import Semantic.SemExceptions.SemanticException;

public class AssignCallSentNode extends SentenceNode{
    private final ExpressionNode innerExpression;

    public AssignCallSentNode(ExpressionNode innerExpression) {
        this.innerExpression = innerExpression;
    }

    @Override
    public void check() throws SemanticException {
        //TODO revisar que sea solo llamada o asignacion?
        innerExpression.check();
        checked = true;
    }
}
