package Semantic.AST.Sentences;

import Lexical.Analyzer.Token;
import Semantic.ST.Method;
import Semantic.ST.Class;
import Semantic.SemExceptions.SemanticException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BlockNode extends SentenceNode{
    private final List<SentenceNode> sentenceNodeList;
    private final HashMap<String,LocalVarNode> localVarList;
    protected boolean checked;
    private final Method method;
    private final Class classElement;
    private final BlockNode fatherBlock;

    public BlockNode(Method method,Class classElement, BlockNode fatherBlock){
        sentenceNodeList = new ArrayList<>();
        localVarList = new HashMap<>();
        checked = false;
        this.method = method;
        this.classElement = classElement;
        this.fatherBlock = fatherBlock;
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
            throw new SemanticException(sentenceNode.getTokenName(), "Se intento crear una var local repetida de nombre: ", sentenceNode.getTokenLine());
        }
        if (method.getParameters().containsKey(name)){
            throw new SemanticException(sentenceNode.getTokenName(), "Se intento crear una var local con nombre de parametro de nombre: ", sentenceNode.getTokenLine());
        }
        if (classElement.getAttributes().containsKey(name)){
            throw new SemanticException(sentenceNode.getTokenName(), "Se intento crear una var local con nombre de atributo: ", sentenceNode.getTokenLine());
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

    public Class getClassElement() {
        return classElement;
    }

    public Method getMethod() {
        return method;
    }

    public BlockNode getFatherBlock() {
        return fatherBlock;
    }

    public LocalVarNode getVar(Token nameClass){
        if (fatherBlock == null){
            return null;
        }
        LocalVarNode fatherVar= fatherBlock.getLocalVarList().get(nameClass.getLexeme());
        LocalVarNode fatherOldVar;
        if (fatherBlock.getFatherBlock() == null){
            fatherOldVar = null;
        }
        fatherOldVar= fatherBlock.getVar(nameClass);
        if (fatherOldVar==null)
            return fatherVar;
        else return fatherOldVar;
    }

    public void generateCode(){
        for(SentenceNode s : sentenceNodeList){
            s.generateCode();
        }
    }
}
