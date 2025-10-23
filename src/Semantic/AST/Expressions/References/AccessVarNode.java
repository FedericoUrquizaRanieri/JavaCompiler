package Semantic.AST.Expressions.References;

import Lexical.Analyzer.Token;
import Semantic.AST.Expressions.ExpressionNode;
import Semantic.AST.Expressions.ReferenceNode;
import Semantic.ST.Type;
import Semantic.SemExceptions.SemanticException;

import java.util.List;

public class AccessVarNode extends ReferenceNode {
    private final Token lastVar;

    public AccessVarNode(Token lastVar,List<ReferenceNode> chainedElements) {
        this.chainedElements = chainedElements;
        this.lastVar = lastVar;
    }

    @Override
    public Type check() throws SemanticException {
        return null; //TODO revisar elementos previos y actual
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
