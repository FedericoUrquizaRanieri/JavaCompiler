package Semantic.AST.Chains;

import Lexical.Analyzer.Token;
import Semantic.AST.Expressions.ExpressionNode;
import Semantic.ST.Type;
import Semantic.SemExceptions.SemanticException;

import java.util.List;

public class ChainedMethodNode extends ChainedNode{
    private List<ExpressionNode> parameters;
    public ChainedMethodNode(List<ExpressionNode> params, Token mainToken) {
        parameters = params;
        idToken = mainToken;
    }

    @Override
    public void setChainedNode(ChainedNode chainedNode) {
        this.chainedNode = chainedNode;
    }

    @Override
    public Type check() throws SemanticException {
        return null; //TODO revisar tipos de encadenados (puede ser que tenga params check)
    }

    @Override
    public ChainedNode getChainedElement() {
        return chainedNode;
    }
}
