package Semantic.AST.Expressions.References;

import Lexical.Analyzer.Token;
import Semantic.AST.Chains.ChainedNode;
import Semantic.AST.Chains.EmptyChainedNode;
import Semantic.AST.Expressions.ExpressionNode;
import Semantic.ST.Type;
import Semantic.SemExceptions.SemanticException;

import java.util.List;

public class AccessMethodNode extends ReferenceNode {
    private final Token methodToken;
    private final List<ExpressionNode> params;

    public AccessMethodNode(List<ExpressionNode> params, Token methodToken) {
        chainedElement = new EmptyChainedNode();
        this.params = params;
        this.methodToken = methodToken;
    }

    @Override
    public Type check() throws SemanticException {
        return null; //TODO revisar elementos previos y actual
    }

    @Override
    public ChainedNode getChainedElement() {
        return null;
    }

    @Override
    public void setChainedElement(ChainedNode chainedElement) {
        this.chainedElement = chainedElement;
    }
}
