package Semantic.AST.Expressions.TypeNode;

import Semantic.AST.Expressions.OperandNode;
import Semantic.ST.Type;

public class NullTypeNode extends OperandNode {
    public NullTypeNode(){
    }

    @Override
    public Type check() {
        return null;
    }
}
