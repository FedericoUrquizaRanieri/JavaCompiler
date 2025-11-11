package Semantic.AST.Chains;

import Lexical.Analyzer.Token;
import Semantic.ST.Type;
import Semantic.SemExceptions.SemanticException;

public abstract class ChainedNode {
    protected Token idToken;
    protected ChainedNode chainedNode;
    public abstract void setChainedNode(ChainedNode chainedNode);
    public abstract Type check(Type lastClass) throws SemanticException;
    public abstract ChainedNode getChainedElement();
    public abstract void generateCode();
}