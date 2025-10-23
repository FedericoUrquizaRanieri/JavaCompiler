package Semantic.AST.Chains;

import Lexical.Analyzer.Token;
import Semantic.ST.Type;
import Semantic.SemExceptions.SemanticException;

public class ChainedVarNode extends ChainedNode{
    public ChainedVarNode(Token mainToken) {
        idToken = mainToken;
    }

    @Override
    public void setChainedNode(ChainedNode chainedNode) {
        this.chainedNode = chainedNode;
    }

    @Override
    public Type check() throws SemanticException {
        return null; //TODO revisar tipos de los otros encadenados
    }

    @Override
    public ChainedNode getChainedElement() {
        return chainedNode;
    }
}
