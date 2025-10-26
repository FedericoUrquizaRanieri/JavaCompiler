package Semantic.AST.Sentences;

import Semantic.AST.Chains.ChainedMethodNode;
import Semantic.AST.Chains.ChainedNode;
import Semantic.AST.Chains.ChainedVarNode;
import Semantic.AST.Chains.EmptyChainedNode;
import Semantic.AST.Expressions.AssignExpNode;
import Semantic.AST.Expressions.ExpressionNode;
import Semantic.AST.Expressions.References.AccessMethodNode;
import Semantic.AST.Expressions.References.ConstructorCallNode;
import Semantic.AST.Expressions.References.ReferenceNode;
import Semantic.AST.Expressions.References.StaticMethodNode;
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

        //TODO revisar que sea correcto esta llamada extraña y principalmente el retorno de operacion y el control de instancesof
        if (innerExpression instanceof ReferenceNode referenceNode){
            if (!endsInMethod(referenceNode)){
                throw new SemanticException(t.getNameType(),"Se detecto una expresion invalida en forma de sentencia: ",t.getTokenType().getLine());
            }
        }
        if (!(innerExpression instanceof AssignExpNode) && !(innerExpression instanceof AccessMethodNode) && !(innerExpression instanceof ConstructorCallNode) && !(innerExpression instanceof StaticMethodNode)){
            throw new SemanticException(t.getNameType(),"Se detecto una expresion invalida en forma de sentencia: ",t.getTokenType().getLine());
        }

        checked = true;
    }

    public boolean endsInMethod(ReferenceNode ref) {
        ChainedNode chain = ref.getChainedElement();
        while (!(chain instanceof EmptyChainedNode)) {
            if (chain.getChainedElement() instanceof EmptyChainedNode) {
                return chain instanceof ChainedMethodNode;
            }
            chain = chain.getChainedElement();
        }
        return ref instanceof AccessMethodNode;
    }
}
