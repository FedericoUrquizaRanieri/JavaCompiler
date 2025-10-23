package Semantic.AST.Expressions.References;

import Semantic.AST.Chains.ChainedNode;
import Semantic.AST.Chains.EmptyChainedNode;
import Semantic.ST.Class;
import Semantic.ST.ReferenceType;
import Semantic.ST.Type;
import Semantic.SemExceptions.SemanticException;

import java.util.List;

public class ThisCallNode extends ReferenceNode {
    private final Class thisClass;

    public ThisCallNode(Class c){
        thisClass = c;
        chainedElement = new EmptyChainedNode();
    }

    @Override
    public Type check() throws SemanticException {
        return new ReferenceType(thisClass.getClassToken(),null);
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
