package Semantic.AST.Sentences;

import java.util.ArrayList;
import java.util.List;

public class BlockNode extends SentenceNode{
    private final List<SentenceNode> sentenceNodeList;
    private final List<LocalVarNode> localVarList;
    protected boolean checked;

    public BlockNode(){
        sentenceNodeList = new ArrayList<>();
        localVarList = new ArrayList<>();
        checked = false;
    }

    @Override
    public void check() {
        for (SentenceNode s : sentenceNodeList){
            s.check();
        }
        checked = true;
    }

    public void addSentence(SentenceNode sentenceNode){
        sentenceNodeList.add(sentenceNode);
    }

    public List<SentenceNode> getSentenceNodeList() {
        return sentenceNodeList;
    }

    public void addLocalVar(SentenceNode sentenceNode){
        localVarList.add((LocalVarNode) sentenceNode); //TODO agregar chequeos
    }

    public List<LocalVarNode> getLocalVarList() {
        return localVarList;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
