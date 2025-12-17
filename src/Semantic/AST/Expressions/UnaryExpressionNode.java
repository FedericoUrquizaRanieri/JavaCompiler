package Semantic.AST.Expressions;

import Lexical.Analyzer.Token;
import Main.MainGen;
import Semantic.ST.Type;
import Semantic.SemExceptions.SemanticException;

public class UnaryExpressionNode extends ComposedExpressionNode{
    private final Token operator;
    private final ExpressionNode expression;

    public UnaryExpressionNode(ExpressionNode expression, Token operator) {
        this.expression = expression;
        this.operator = operator;
    }

    @Override
    public Type check() throws SemanticException {
        Type retType = expression.check();
        retType.isOperandCompatibleUnary(operator);
        checked = true;
        return retType;
    }

    @Override
    public void generateCode() {
        expression.generateCode();
        switch (operator.getLexeme()){
            case "-" -> MainGen.symbolTable.instructionsList.add("NEG");
            case "--" -> {
                MainGen.symbolTable.instructionsList.add("PUSH 1");
                MainGen.symbolTable.instructionsList.add("SUB");
            }
            case "++" -> {
                MainGen.symbolTable.instructionsList.add("PUSH 1");
                MainGen.symbolTable.instructionsList.add("ADD");
            }
            case "!" -> MainGen.symbolTable.instructionsList.add("NOT");
        }
    }
}
