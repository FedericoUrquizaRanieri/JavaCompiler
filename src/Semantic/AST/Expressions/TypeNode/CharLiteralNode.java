package Semantic.AST.Expressions.TypeNode;

import Lexical.Analyzer.Token;
import Semantic.AST.Expressions.OperandNode;
import Semantic.ST.PrimitiveType;
import Semantic.ST.Type;

public class CharLiteralNode extends OperandNode {
    private final Type type;
    public CharLiteralNode(Type t){
        type = t;
        staticToken = new PrimitiveType(new Token("pr_char",t.getNameType(),t.getTokenType().getLine()));
    }

    @Override
    public Type check() {
        return staticToken;
    }

    @Override
    public void generateCode() {
        //TODO apilar?
    }
}
