package Semantic.AST.Sentences;

import Lexical.Analyzer.Token;
import Main.MainGen;
import Semantic.AST.Expressions.ExpressionNode;
import Semantic.SemExceptions.SemanticException;

public class WhileNode extends SentenceNode{
    private final ExpressionNode condition;
    private final SentenceNode body;
    private final Token mainToken;

    public WhileNode(ExpressionNode cond, SentenceNode body, Token mainToken){
        this.condition = cond;
        this.body = body;
        this.mainToken = mainToken;
    }

    @Override
    public void check() throws SemanticException{
        condition.check().isCompatible("boolean",mainToken);
        body.check();
        checked = true;
    }

    @Override
    public void generateCode() {
        int whileMark = MainGen.symbolTable.conditionalsMark;
        int whileEndMark = MainGen.symbolTable.conditionalsMark;
        MainGen.symbolTable.instructionsList.add("while"+ whileMark + ": NOP");
        condition.generateCode();
        MainGen.symbolTable.instructionsList.add("BF whilefin" + whileEndMark);
        body.generateCode();
        MainGen.symbolTable.instructionsList.add("JUMP while" + whileMark + " ; salta a la cond");
        MainGen.symbolTable.instructionsList.add("whilefin"+ whileEndMark + ": NOP");
    }
}
