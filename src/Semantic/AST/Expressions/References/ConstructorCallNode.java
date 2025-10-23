package Semantic.AST.Expressions.References;

import Lexical.Analyzer.Token;
import Semantic.AST.Chains.ChainedNode;
import Semantic.AST.Chains.EmptyChainedNode;
import Semantic.AST.Expressions.ExpressionNode;
import Semantic.ST.Type;
import Semantic.SemExceptions.SemanticException;

import java.util.List;

public class ConstructorCallNode extends ReferenceNode {
    private final Token classElement;
    private final List<ExpressionNode> args;

    public ConstructorCallNode(Token classElement, List<ExpressionNode> args) {
        this.classElement = classElement;
        this.args = args;
        chainedElement = new EmptyChainedNode();
    }

    @Override
    public Type check() throws SemanticException {
        return null; //TODO revisar clase que existe y los params
    }

    @Override
    public ChainedNode getChainedElement() {
        return null;
    }

    @Override
    public void setChainedElement(ChainedNode chainedNode) {
        chainedElement = chainedNode;
    }
}
