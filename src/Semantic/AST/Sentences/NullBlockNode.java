package Semantic.AST.Sentences;

import Semantic.SemExceptions.SemanticException;

public class NullBlockNode extends BlockNode{
    public NullBlockNode(){
        super(null,null);
        checked = false;
    }

    @Override
    public void check() throws SemanticException {
        checked = true;
    }

    @Override
    public void addSentence(SentenceNode sentenceNode) {

    }

    @Override
    public void addLocalVar(String name,LocalVarNode sentenceNode){

    }
}
