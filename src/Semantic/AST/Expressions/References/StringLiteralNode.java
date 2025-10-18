package Semantic.AST.Expressions.References;

import Lexical.Analyzer.Token;
import Semantic.AST.Expressions.ReferenceNode;
import Semantic.ST.PrimitiveType;
import Semantic.ST.Type;

public class StringLiteralNode extends ReferenceNode {
    private final Token string;

    public StringLiteralNode(Token string) {
        this.string = string;
    }

    @Override
    public Type check() {
        return new PrimitiveType(string);
    }
}
