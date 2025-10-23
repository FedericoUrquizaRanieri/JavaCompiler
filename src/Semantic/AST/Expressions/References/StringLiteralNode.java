package Semantic.AST.Expressions.References;

import Lexical.Analyzer.Token;
import Semantic.AST.Expressions.ReferenceNode;
import Semantic.ST.PrimitiveType;
import Semantic.ST.Type;
import Semantic.SemExceptions.SemanticException;

import java.util.List;

public class StringLiteralNode extends ReferenceNode {
    private final Token string;

    public StringLiteralNode(Token string) {
        this.string = string;
    }

    @Override
    public Type check() throws SemanticException {
        return new PrimitiveType(string);
    }

    @Override
    public List<ReferenceNode> getChainedElements() {
        return null;
    }

    @Override
    public void setChainedElements(List<ReferenceNode> refList) {
        chainedElements = refList;
    }
}
