package Semantic.AST.Expressions.TypeNode;

import Lexical.Analyzer.Token;
import Semantic.AST.Expressions.OperandNode;
import Semantic.ST.PrimitiveType;
import Semantic.ST.Type;

public class NullTypeNode extends OperandNode {
    public NullTypeNode(){
    }

    @Override
    public Type check() {
        return new PrimitiveType(new Token("pr_null","null",0));
    } //TODO revisar operaciones con esto
}
