package Semantic.AST.Expressions.TypeNode;

import Semantic.AST.Expressions.OperandNode;
import Semantic.ST.Type;

public class IntLiteralNode extends OperandNode {
    private final Type type;
    public IntLiteralNode(Type t){
        type = t;
    }

    @Override
    public Type check() {
        return type;
    }
}
