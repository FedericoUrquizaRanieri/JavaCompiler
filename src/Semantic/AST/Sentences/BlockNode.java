package Semantic.AST.Sentences;

import java.util.ArrayList;
import java.util.List;

public class BlockNode extends SentenceNode{
    private final List<SentenceNode> sentenceNodeList;

    public BlockNode(){
        sentenceNodeList = new ArrayList<>();
    }

    @Override
    public void check() {
        super.check();
    }
}
