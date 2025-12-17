package Semantic.AST.Sentences;

import Semantic.SemExceptions.SemanticException;

public class EmptySentenceNode extends SentenceNode{

    public EmptySentenceNode() {
    }

    public void check() throws SemanticException {}

    public void generateCode(){}
}
