package Semantic.AST.Sentences;

import Semantic.AST.Expressions.ExpressionNode;
import Semantic.ST.Type;
import Semantic.SemExceptions.SemanticException;

import java.util.List;

public class AssignCallSentNode extends SentenceNode{
    private final ExpressionNode innerExpression;

    public AssignCallSentNode(ExpressionNode innerExpression) {
        this.innerExpression = innerExpression;
    }

    @Override
    public void check() throws SemanticException {
        Type t = innerExpression.check();

        //TODO revisar que sea correcto esta llamada extraña
        List<String> validSentences = List.of("class AssignExpNode", "class AccessMethodNode", "class AccessVarNode", "class ConstructorCallNode", "class StaticMethodNode");
        if (validSentences.contains(innerExpression.getClass().toString())){
            throw new SemanticException(t.getNameType(),"Se detecto una expresion invalida en forma de sentencia: ",t.getTokenType().getLine());
        }

        checked = true;
    }
}
