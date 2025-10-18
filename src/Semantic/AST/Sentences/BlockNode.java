package Semantic.AST.Sentences;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BlockNode extends SentenceNode{
    private final List<SentenceNode> sentenceNodeList;
    private final HashMap<String,LocalVarNode> localVarList;
    protected boolean checked;

    public BlockNode(){
        sentenceNodeList = new ArrayList<>();
        localVarList = new HashMap<>();
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
        sentenceNodeList.add(sentenceNode); //TODO agregar chequeos
    }

    public HashMap<String,LocalVarNode> getLocalVarList() {
        return localVarList;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
