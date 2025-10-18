package Semantic.AST.Expressions.References;

import Lexical.Analyzer.Token;
import Semantic.AST.Expressions.ReferenceNode;
import Semantic.ST.Type;

public class AccessVarNode extends ReferenceNode {
    private ReferenceNode chainedElements;
    private Token lastVar;

    public AccessVarNode(ReferenceNode chainedElements) {
        this.chainedElements = chainedElements;
    }

    @Override
    public Type check() {
        return null; //TODO revisar elementos previos y actual
    }
}
