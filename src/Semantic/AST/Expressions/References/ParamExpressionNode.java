package Semantic.AST.Expressions.References;

import Semantic.AST.Chains.ChainedNode;
import Semantic.AST.Chains.EmptyChainedNode;
import Semantic.AST.Expressions.ExpressionNode;
import Semantic.ST.Type;
import Semantic.SemExceptions.SemanticException;


public class ParamExpressionNode extends ReferenceNode {
    private final ExpressionNode expression;

    public ParamExpressionNode(ExpressionNode expression) {
        this.expression = expression;
        chainedElement = new EmptyChainedNode();
    }

    @Override
    public Type check() throws SemanticException {
        Type expType = expression.check();
        Type chainedType = chainedElement.check(expType);
        if(chainedType.getNameType().equals("Universal")){
            return expType;
        } else {
            return chainedType;
        }
    }

    @Override
    public ChainedNode getChainedElement() {
        return chainedElement;
    }

    @Override
    public void setChainedElement(ChainedNode chainedNode) {
        chainedElement = chainedNode;
    }
}
