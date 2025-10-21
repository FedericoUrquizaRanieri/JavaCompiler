package Semantic.AST.Sentences;

public class NullBlockNode extends BlockNode{
    public NullBlockNode(){
        super();
        checked = false;
    }

    @Override
    public void check() {
        checked = true;
    }

    @Override
    public void addSentence(SentenceNode sentenceNode) {

    }

    @Override
    public void addLocalVar(SentenceNode sentenceNode) {

    }
}
