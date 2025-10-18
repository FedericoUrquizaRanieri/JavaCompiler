package Semantic.AST.Expressions.References;

import Semantic.AST.Expressions.ReferenceNode;
import Semantic.ST.Class;
import Semantic.ST.Method;
import Semantic.ST.Parameter;
import Semantic.ST.Type;

import java.util.HashMap;

public class StaticMethodNode extends ReferenceNode {
    private final Class classElement;
    private final Method methodElement;
    private final HashMap<String, Parameter> args;

    public StaticMethodNode(Class classElement, Method methodElement, HashMap<String, Parameter> args) {
        this.classElement = classElement;
        this.methodElement = methodElement;
        this.args = args;
    }

    @Override
    public Type check() {
        return null; //TODO aca revisar metodos y retornos
    }
}
