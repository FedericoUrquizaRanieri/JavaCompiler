package Semantic.AST.Expressions.References;

import Semantic.AST.Expressions.ReferenceNode;
import Semantic.ST.Class;
import Semantic.ST.ReferenceType;
import Semantic.ST.Type;

public class ThisCallNode extends ReferenceNode {
    private final Class thisClass;

    public ThisCallNode(Class c){
        thisClass = c;
    }

    @Override
    public Type check() {
        return new ReferenceType(thisClass.getClassToken(),null);
    }
}
