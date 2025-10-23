package Semantic.AST.Expressions.References;

import Lexical.Analyzer.Token;
import Semantic.AST.Chains.ChainedNode;
import Semantic.AST.Chains.EmptyChainedNode;
import Semantic.ST.Type;
import Semantic.SemExceptions.SemanticException;

public class AccessVarNode extends ReferenceNode {
    private final Token varToken;

    public AccessVarNode(Token varToken) {
        chainedElement = new EmptyChainedNode();
        this.varToken = varToken;
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
    public void setChainedElement(ChainedNode chainedNode) {
        chainedElement = chainedNode;
    }
}
