package Semantic.AST.Sentences;

import Semantic.ST.Method;
import Semantic.SemExceptions.SemanticException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BlockNode extends SentenceNode{
    private final List<SentenceNode> sentenceNodeList;
    private final HashMap<String,LocalVarNode> localVarList;
    protected boolean checked;
    private final Method method;

    public BlockNode(Method method){
        sentenceNodeList = new ArrayList<>();
        localVarList = new HashMap<>();
        checked = false;
        this.method = method;
    }

    @Override
    public void check() throws SemanticException {
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

    public void addLocalVar(String name,LocalVarNode sentenceNode) throws SemanticException {
        if (localVarList.put(name,sentenceNode) != null){
            throw new SemanticException(sentenceNode.getTokenName(), "Se intento crear una var local repetida de nombre ", sentenceNode.getTokenLine());
        }
        if (method.getParameters().containsKey(name)){
            throw new SemanticException(sentenceNode.getTokenName(), "se intento crear una var local con nombre de parametro de nombre", sentenceNode.getTokenLine());
        }
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
