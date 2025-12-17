package Semantic.AST.Sentences;

import Lexical.Analyzer.Token;
import Main.MainGen;
import Semantic.AST.Expressions.ExpressionNode;
import Semantic.SemExceptions.SemanticException;

public class IfNode extends SentenceNode{
    private final ExpressionNode condition;
    private final SentenceNode body;
    private final SentenceNode elseSentence;
    private final Token mainToken;

    public IfNode(ExpressionNode cond, SentenceNode body,SentenceNode elseS,Token mainToken){
        this.condition = cond;
        this.body = body;
        this.elseSentence = elseS;
        this.mainToken = mainToken;
    }

    @Override
    public void check() throws SemanticException {
        condition.check().isCompatible("boolean",mainToken);
        body.check();
        elseSentence.check();
        checked = true;
    }

    @Override
    public void generateCode() {
        int ifIndex = MainGen.symbolTable.getConditionalsMark();
        condition.generateCode();
        if (elseSentence == null){
            MainGen.symbolTable.instructionsList.add("BF finif"+ifIndex+" ; salto segun cond");
            body.generateCode();
            MainGen.symbolTable.instructionsList.add("finif"+ifIndex+": NOP ; final if");
        } else {
            int elseIndex = MainGen.symbolTable.getConditionalsMark();
            MainGen.symbolTable.instructionsList.add("BF else"+elseIndex+" ; salto segun cond");
            body.generateCode();
            MainGen.symbolTable.instructionsList.add("JUMP finif"+ifIndex);
            MainGen.symbolTable.instructionsList.add("else"+elseIndex+": NOP ; tag else");
            elseSentence.generateCode();
            MainGen.symbolTable.instructionsList.add("finif"+ifIndex+": NOP ; final if");
        }
    }
}
