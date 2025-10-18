package Semantic.AST.Expressions.References;

import Semantic.AST.Expressions.ExpressionNode;
import Semantic.AST.Expressions.ReferenceNode;
import Semantic.ST.Type;

public class ParamExpressionNode extends ReferenceNode {
    private final ExpressionNode expression;

    public ParamExpressionNode(ExpressionNode expression) {
        this.expression = expression;
    }

    @Override
    public Type check() {
        return expression.check();
    }
}
