package Semantic.AST.Sentences;

import Main.MainGen;
import Semantic.SemExceptions.SemanticException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NullBlockNode extends BlockNode{
    public NullBlockNode(){
        super(null,null,null);
        checked = false;
        lastOffsetValue = -1;
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

    @Override
    public List<SentenceNode> getSentenceNodeList() {
        return new ArrayList<>();
    }

    @Override
    public HashMap<String,LocalVarNode> getLocalVarList() {
        return new HashMap<>();
    }

    @Override
    public void generateCode() {
        MainGen.symbolTable.instructionsList.add("FMEM 0");
    }
}
