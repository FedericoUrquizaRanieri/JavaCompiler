package Semantic.AST.Expressions.TypeNode;

import Lexical.Analyzer.Token;
import Semantic.AST.Expressions.OperandNode;
import Semantic.ST.PrimitiveType;
import Semantic.ST.Type;

public class IntLiteralNode extends OperandNode {
    private final Type type;
    public IntLiteralNode(Type t){
        type = t;
        staticToken = new PrimitiveType(new Token("pr_int",t.getNameType(),t.getTokenType().getLine()));
    }

    @Override
    public Type check() {
        return staticToken;
    }
}
