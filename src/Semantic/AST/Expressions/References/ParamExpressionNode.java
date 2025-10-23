package Semantic.AST.Expressions.References;

import Semantic.AST.Chains.ChainedNode;
import Semantic.AST.Chains.EmptyChainedNode;
import Semantic.AST.Expressions.ExpressionNode;
import Semantic.ST.Type;
import Semantic.SemExceptions.SemanticException;

import java.util.List;

public class ParamExpressionNode extends ReferenceNode {
    private final ExpressionNode expression;

    public ParamExpressionNode(ExpressionNode expression) {
        this.expression = expression;
        chainedElement = new EmptyChainedNode();
    }

    @Override
    public Type check() throws SemanticException {
        return expression.check();
    }

    @Override
    public ChainedNode getChainedElement() {
        return null;
    }

    @Override
    public void setChainedElement(ChainedNode chainedNode) {
        chainedElement = chainedNode;
    }
}
