package Semantic.AST.Expressions.References;

import Semantic.AST.Expressions.ExpressionNode;
import Semantic.AST.Expressions.ReferenceNode;
import Semantic.ST.Type;
import Semantic.SemExceptions.SemanticException;

import java.util.List;

public class ParamExpressionNode extends ReferenceNode {
    private final ExpressionNode expression;

    public ParamExpressionNode(ExpressionNode expression) {
        this.expression = expression;
    }

    @Override
    public Type check() throws SemanticException {
        return expression.check();
    }

    @Override
    public List<ReferenceNode> getChainedElements() {
        return null;
    }

    @Override
    public void setChainedElements(List<ReferenceNode> refList) {
        chainedElements = refList;
    }
}
