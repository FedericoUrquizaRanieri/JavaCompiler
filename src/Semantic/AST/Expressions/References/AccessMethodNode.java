package Semantic.AST.Expressions.References;

import Lexical.Analyzer.Token;
import Semantic.AST.Expressions.ExpressionNode;
import Semantic.AST.Expressions.ReferenceNode;
import Semantic.ST.Type;

import java.util.List;

public class AccessMethodNode extends ReferenceNode {
    private final Token lastMethod;
    private final List<ExpressionNode> params;

    public AccessMethodNode(List<ExpressionNode> params, Token lastMethod, List<ReferenceNode> chainedElements) {
        this.chainedElements = chainedElements;
        this.params = params;
        this.lastMethod = lastMethod;
    }

    @Override
    public Type check() {
        return null; //TODO revisar elementos previos y actual
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
