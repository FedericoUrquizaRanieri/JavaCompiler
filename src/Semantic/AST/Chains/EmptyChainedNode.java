package Semantic.AST.Chains;

import Lexical.Analyzer.Token;
import Semantic.ST.PrimitiveType;
import Semantic.ST.Type;
import Semantic.SemExceptions.SemanticException;

public class EmptyChainedNode extends ChainedNode{
    public EmptyChainedNode() {
    }

    @Override
    public void setChainedNode(ChainedNode chainedNode) {

    }

    @Override
    public Type check(Type lastClass) throws SemanticException {
        return new PrimitiveType(new Token("Universal","Universal",0));
    }

    @Override
    public ChainedNode getChainedElement() {
        return null;
    }

    @Override
    public void generateCode() {}
}
