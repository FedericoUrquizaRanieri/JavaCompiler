package Semantic.AST.Sentences;

import Semantic.AST.Expressions.ExpressionNode;
import Semantic.ST.Type;

public class ReturnNode extends SentenceNode{
    private final ExpressionNode retValue;
    private Type retType;
    public ReturnNode(ExpressionNode e){
        retValue=e;
    }

    @Override
    public void check() {
        retType = retValue.check();
    }
}
