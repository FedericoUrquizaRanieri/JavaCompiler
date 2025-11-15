package Semantic.AST.Expressions;

import Lexical.Analyzer.Token;
import Main.MainGen;
import Semantic.ST.PrimitiveType;
import Semantic.ST.Type;
import Semantic.SemExceptions.SemanticException;

import java.util.List;

public class BinaryExpressionNode extends ComposedExpressionNode{
    private final ExpressionNode leftExpression;
    private final Token operator;
    private final ExpressionNode rightExpression;

    public BinaryExpressionNode(ExpressionNode leftExpression, Token operator, ExpressionNode rightExpression) {
        this.leftExpression = leftExpression;
        this.operator = operator;
        this.rightExpression = rightExpression;
    }

    @Override
    public Type check() throws SemanticException {
        leftExpression.check().isOperandCompatibleBinary(operator, rightExpression.check());
        checked = true;
        var listInt = List.of("+","-","*","%","/");
        var listBool = List.of("&&","||","<","<=",">",">=","==","!=");
        if (listBool.contains(operator.getLexeme())){
            return new PrimitiveType(new Token("pr_boolean","true", operator.getLine()));
        } else if (listInt.contains(operator.getLexeme())){
            return new PrimitiveType(new Token("pr_int","int", operator.getLine()));
            //TODO aca se supone que pongo un result???
        } else throw new SemanticException(operator.getLexeme(),"El operador no se puede usar correctamente en una operacion: ", operator.getLine());
    }

    @Override
    public void generateCode() {
        leftExpression.generateCode();
        rightExpression.generateCode();
        switch (operator.getLexeme()){
            case "+" -> MainGen.symbolTable.instructionsList.add("ADD");
            case "-" -> MainGen.symbolTable.instructionsList.add("SUB");
            case "*" -> MainGen.symbolTable.instructionsList.add("MUL");
            case "%" -> MainGen.symbolTable.instructionsList.add("MOD");
            case "/" -> MainGen.symbolTable.instructionsList.add("DIV");
            case "&&" -> MainGen.symbolTable.instructionsList.add("AND");
            case "||" -> MainGen.symbolTable.instructionsList.add("OR");
            case "<" -> MainGen.symbolTable.instructionsList.add("LT");
            case "<=" -> MainGen.symbolTable.instructionsList.add("LE");
            case ">=" -> MainGen.symbolTable.instructionsList.add("GE");
            case ">" -> MainGen.symbolTable.instructionsList.add("GT");
            case "==" -> MainGen.symbolTable.instructionsList.add("EQ");
            case "!=" -> MainGen.symbolTable.instructionsList.add("NE");
        }
    }
}
