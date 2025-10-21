package Semantic.AST.Sentences;

import Semantic.AST.Expressions.ExpressionNode;
import Semantic.ST.Type;

public class ReturnNode extends SentenceNode{
    private final ExpressionNode retValue;
    private final Type retType;
    public ReturnNode(ExpressionNode e,Type retType){
        retValue=e;
        this.retType = retType;
    }

    @Override
    public void check() {
        if (retValue==null){
            //TODO throw
        }
        else if (!retType.compareTypes(retValue.check())){
                //TODO throw
        }
        checked = true;
    }
}
