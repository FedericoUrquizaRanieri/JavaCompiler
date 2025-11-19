package Semantic.AST.Chains;

import Lexical.Analyzer.Token;
import Semantic.ST.Type;
import Semantic.SemExceptions.SemanticException;

public abstract class ChainedNode {
    protected Token idToken;
    protected ChainedNode chainedNode;
    protected boolean isLeftSided = false;
    public abstract void setChainedNode(ChainedNode chainedNode);
    public abstract Type check(Type lastClass) throws SemanticException;
    public abstract ChainedNode getChainedElement();
    public abstract void generateCode();
    public void setLeftSided(){
        isLeftSided = true;
        if(chainedNode != null){
            chainedNode.setLeftSided();
        }
    }
}