package Semantic.AST.Expressions.References;

import Lexical.Analyzer.Token;
import Semantic.AST.Expressions.ExpressionNode;
import Semantic.AST.Expressions.ReferenceNode;
import Semantic.ST.Class;
import Semantic.ST.Parameter;
import Semantic.ST.Type;
import Semantic.SemExceptions.SemanticException;

import java.util.HashMap;
import java.util.List;

public class ConstructorCallNode extends ReferenceNode {
    private final Token classElement;
    private final List<ExpressionNode> args;

    public ConstructorCallNode(Token classElement, List<ExpressionNode> args) {
        this.classElement = classElement;
        this.args = args;
    }

    @Override
    public Type check() throws SemanticException {
        return null; //TODO revisar clase que existe y los params
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
