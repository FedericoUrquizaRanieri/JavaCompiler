package Semantic.AST.Expressions.References;

import Lexical.Analyzer.Token;
import Semantic.AST.Expressions.ReferenceNode;
import Semantic.ST.Type;

public class AccessMethodNode extends ReferenceNode {
    private ReferenceNode chainedElements;
    private Token lastMethod;

    @Override
    public Type check() {
        return null; //TODO revisar elementos previos y actual
    }
}
