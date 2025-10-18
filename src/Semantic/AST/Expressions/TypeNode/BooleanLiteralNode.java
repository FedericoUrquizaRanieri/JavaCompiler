package Semantic.AST.Expressions.TypeNode;

import Semantic.AST.Expressions.OperandNode;
import Semantic.ST.Type;

public class BooleanLiteralNode extends OperandNode {
    private final Type type;
    public BooleanLiteralNode(Type t){
        type = t;
    }

    @Override
    public Type check() {
        return type;
    }
}
