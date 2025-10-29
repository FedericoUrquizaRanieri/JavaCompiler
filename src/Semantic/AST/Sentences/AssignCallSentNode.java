package Semantic.AST.Sentences;

import Lexical.Analyzer.Token;
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


public class AssignCallSentNode extends SentenceNode{
    private final ExpressionNode innerExpression;
    private final Token idToken;

    public AssignCallSentNode(ExpressionNode innerExpression,Token currentToken) {
        this.innerExpression = innerExpression;
        idToken = currentToken;
    }

    @Override
    public void check() throws SemanticException {
        Type t = innerExpression.check();

        if (innerExpression instanceof ReferenceNode referenceNode){
            if (!endsInMethod(referenceNode)){
                throw new SemanticException(idToken.getLexeme(), "Se detecto una expresion invalida en forma de sentencia: ",idToken.getLine());
            }
        } else if (!(innerExpression instanceof AssignExpNode)){
                throw new SemanticException(idToken.getLexeme(), "Se detecto una expresion invalida en forma de sentencia: ",idToken.getLine());
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
        return (ref instanceof AccessMethodNode || ref instanceof StaticMethodNode);
    }
}
