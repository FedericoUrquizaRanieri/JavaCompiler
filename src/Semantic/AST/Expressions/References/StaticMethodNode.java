package Semantic.AST.Expressions.References;

import Lexical.Analyzer.Token;
import Semantic.AST.Expressions.ExpressionNode;
import Semantic.AST.Expressions.ReferenceNode;
import Semantic.ST.Class;
import Semantic.ST.Method;
import Semantic.ST.Parameter;
import Semantic.ST.Type;
import Semantic.SemExceptions.SemanticException;

import java.util.HashMap;
import java.util.List;

public class StaticMethodNode extends ReferenceNode {
    private final Token classElement;
    private final Token methodElement;
    private final List<ExpressionNode> args;

    public StaticMethodNode(Token classElement, Token methodElement, List<ExpressionNode> args) {
        this.classElement = classElement;
        this.methodElement = methodElement;
        this.args = args;
    }

    @Override
    public Type check() throws SemanticException {
        return null; //TODO aca revisar metodos en clases, que las clases existen y retornos
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
