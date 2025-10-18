package Semantic.AST.Sentences;

import Semantic.AST.Expressions.ComposedExpressionNode;
import Semantic.AST.Expressions.ExpressionNode;
import Semantic.ST.Type;

public class LocalVarNode extends SentenceNode{
    private final ExpressionNode compExpression;
    private Type varType;

    public LocalVarNode(ExpressionNode e){
        compExpression=e;
    }

    @Override
    public void check() {
        varType = compExpression.check();
        checked = true;
    }
}
