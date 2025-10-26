package Semantic.AST.Expressions;

import Lexical.Analyzer.Token;
import Semantic.AST.Chains.ChainedNode;
import Semantic.AST.Chains.ChainedVarNode;
import Semantic.AST.Chains.EmptyChainedNode;
import Semantic.AST.Expressions.References.AccessVarNode;
import Semantic.AST.Expressions.References.ReferenceNode;
import Semantic.ST.Type;
import Semantic.SemExceptions.SemanticException;

public class AssignExpNode extends ExpressionNode {
    private final ExpressionNode leftExpression;
    private final ExpressionNode rightExpression;
    private final Token equalToken;

    public AssignExpNode(ExpressionNode leftExpression, ExpressionNode rightExpression, Token equalToken) {
        this.leftExpression = leftExpression;
        this.rightExpression = rightExpression;
        this.equalToken = equalToken;
    }

    @Override
    public Type check() throws SemanticException {
        if (leftExpression instanceof ReferenceNode referenceNode){
            if (!(referenceNode instanceof AccessVarNode)){
                if(!endsInVariable(referenceNode)){
                    throw new SemanticException(equalToken.getLexeme(),"Expresion invalida a izquierda de asignacion: ", equalToken.getLine());
                }
            }
        } else throw new SemanticException(equalToken.getLexeme(),"Expresion invalida a izquierda de asignacion: ", equalToken.getLine());
        Type retType = leftExpression.check();
        retType.compareTypes(rightExpression.check(),equalToken);
        checked = true;
        return retType;
    }
    //TODO preguntar que hago aca con los instaceof
    public boolean endsInVariable(ReferenceNode ref) {
        ChainedNode chain = ref.getChainedElement();
        while (!(chain instanceof EmptyChainedNode)) {
            if (chain.getChainedElement() instanceof EmptyChainedNode) {
                return chain instanceof ChainedVarNode;
            }
            chain = chain.getChainedElement();
        }
        return ref instanceof AccessVarNode;
    }
}
