package Semantic.AST.Expressions;

import java.util.List;

public abstract class ReferenceNode extends OperandNode{
    protected List<ReferenceNode> chainedElements;
    public abstract List<ReferenceNode> getChainedElements();
    public abstract void setChainedElements(List<ReferenceNode> refList);
}
