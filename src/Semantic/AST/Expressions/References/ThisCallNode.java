package Semantic.AST.Expressions.References;

import Semantic.AST.Expressions.ReferenceNode;
import Semantic.ST.Class;
import Semantic.ST.ReferenceType;
import Semantic.ST.Type;
import Semantic.SemExceptions.SemanticException;

import java.util.List;

public class ThisCallNode extends ReferenceNode {
    private final Class thisClass;

    public ThisCallNode(Class c){
        thisClass = c;
    }

    @Override
    public Type check() throws SemanticException {
        return new ReferenceType(thisClass.getClassToken(),null);
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
