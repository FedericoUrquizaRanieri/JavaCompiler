package Semantic.AST.Expressions.References;

import Lexical.Analyzer.Token;
import Semantic.AST.Chains.ChainedNode;
import Semantic.AST.Chains.EmptyChainedNode;
import Semantic.ST.ReferenceType;
import Semantic.ST.Type;
import Semantic.SemExceptions.SemanticException;

public class StringLiteralNode extends ReferenceNode {
    private final Token string;
    private final ReferenceType stringType;

    public StringLiteralNode(Token string) {
        this.string = string;
        chainedElement = new EmptyChainedNode();
        stringType = new ReferenceType(new Token("String",string.getLexeme(), string.getLine()),null);
    }

    @Override
    public Type check() throws SemanticException {
        Type chainedType = chainedElement.check(stringType);
        if(chainedType.getNameType().equals("Universal")){
            return stringType;
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

    @Override
    public void generateCode() {
        //TODO apilar y revisar encadenado?
    }
}
