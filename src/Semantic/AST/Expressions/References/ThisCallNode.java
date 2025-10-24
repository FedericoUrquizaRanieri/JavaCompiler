package Semantic.AST.Expressions.References;

import Semantic.AST.Chains.ChainedNode;
import Semantic.AST.Chains.EmptyChainedNode;
import Semantic.ST.Class;
import Semantic.ST.ReferenceType;
import Semantic.ST.Type;
import Semantic.SemExceptions.SemanticException;

public class ThisCallNode extends ReferenceNode {
    private final Class thisClass;

    public ThisCallNode(Class c){
        thisClass = c;
        chainedElement = new EmptyChainedNode();
    }

    @Override
    public Type check() throws SemanticException {
        //TODO revisar metodo estatico
        Type chainedType = chainedElement.check();
        if(chainedType.getNameType().equals("Universal")){
            return new ReferenceType(thisClass.getClassToken(),null);
        } else {
            return chainedType;
        }
    }

    @Override
    public ChainedNode getChainedElement() {
        return chainedElement;
    }

    @Override
    public void setChainedElement(ChainedNode chainedNode) {
        chainedElement = chainedNode;
    }
}
