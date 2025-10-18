package Semantic.AST.Expressions.References;

import Semantic.AST.Expressions.ExpressionNode;
import Semantic.AST.Expressions.ReferenceNode;
import Semantic.ST.Class;
import Semantic.ST.Parameter;
import Semantic.ST.Type;

import java.util.HashMap;

public class ConstructorCallNode extends ReferenceNode {
    private final Class classElement;
    private final HashMap<String, Parameter> args;

    public ConstructorCallNode(Class classElement, HashMap<String, Parameter> args) {
        this.classElement = classElement;
        this.args = args;
    }

    @Override
    public Type check() {
        return null; //TODO revisar clase que existe y los params
    }
}
