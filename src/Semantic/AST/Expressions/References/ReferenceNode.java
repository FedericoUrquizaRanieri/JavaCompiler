package Semantic.AST.Expressions.References;

import Semantic.AST.Chains.ChainedNode;
import Semantic.AST.Expressions.OperandNode;

public abstract class ReferenceNode extends OperandNode {
    protected ChainedNode chainedElement;
    protected boolean isLeftSided = false;
    public abstract ChainedNode getChainedElement();
    public abstract void setChainedElement(ChainedNode chainedElement);
    public void setLeftSided(){
        isLeftSided = true;
        if(chainedElement != null){
            chainedElement.setLeftSided();
        }
    }
}
