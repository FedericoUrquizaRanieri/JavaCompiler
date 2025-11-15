package Semantic.AST.Expressions.TypeNode;

import Lexical.Analyzer.Token;
import Main.MainGen;
import Semantic.AST.Expressions.OperandNode;
import Semantic.ST.PrimitiveType;
import Semantic.ST.Type;

public class BooleanLiteralNode extends OperandNode {
    private final Type type;
    public BooleanLiteralNode(Type t){
        type = t;
        staticToken = new PrimitiveType(new Token("pr_boolean",t.getNameType(),t.getTokenType().getLine()));
    }

    @Override
    public Type check() {
        return staticToken;
    }

    @Override
    public void generateCode() {
        if(type.getNameType().equals("true")){
            MainGen.symbolTable.instructionsList.add("PUSH 1");
        } else {
            MainGen.symbolTable.instructionsList.add("PUSH 0");
        }
    }
}