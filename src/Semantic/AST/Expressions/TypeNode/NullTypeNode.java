package Semantic.AST.Expressions.TypeNode;

import Lexical.Analyzer.Token;
import Main.MainGen;
import Semantic.AST.Expressions.OperandNode;
import Semantic.ST.PrimitiveType;
import Semantic.ST.Type;

public class NullTypeNode extends OperandNode {
    private final Token currentToken;
    public NullTypeNode(Token currentToken){
        this.currentToken = currentToken;
    }

    @Override
    public Type check() {
        return new PrimitiveType(new Token("Universal","null", currentToken.getLine()));
    }

    @Override
    public void generateCode() {
        MainGen.symbolTable.instructionsList.add("PUSH 0");
    }
}
