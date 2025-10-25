package Semantic.AST.Sentences;

import Semantic.AST.Chains.ChainedMethodNode;
import Semantic.AST.Chains.ChainedNode;
import Semantic.AST.Chains.ChainedVarNode;
import Semantic.AST.Chains.EmptyChainedNode;
import Semantic.AST.Expressions.ExpressionNode;
import Semantic.AST.Expressions.References.ReferenceNode;
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
        if (innerExpression instanceof ReferenceNode referenceNode){
            if (!endsInVariable(referenceNode)){
                throw new SemanticException(t.getNameType(),"Se detecto una expresion invalida en forma de sentencia: ",t.getTokenType().getLine());
            }
        }
        List<String> validSentences = List.of("class AssignExpNode", "class AccessMethodNode", "class ConstructorCallNode", "class StaticMethodNode");
        if (validSentences.contains(innerExpression.getClass().toString())){
            throw new SemanticException(t.getNameType(),"Se detecto una expresion invalida en forma de sentencia: ",t.getTokenType().getLine());
        }

        checked = true;
    }

    public boolean endsInVariable(ReferenceNode ref) {
        ChainedNode chain = ref.getChainedElement();
        while (!(chain instanceof EmptyChainedNode)) {
            if (chain.getChainedElement() instanceof EmptyChainedNode) {
                if (chain instanceof ChainedMethodNode)
                    return true;
            }
            chain = chain.getChainedElement();
        }
        return false;
    }
}
