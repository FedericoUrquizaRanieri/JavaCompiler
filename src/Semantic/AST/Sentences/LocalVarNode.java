package Semantic.AST.Sentences;

import Lexical.Analyzer.Token;
import Semantic.AST.Expressions.AssignExpNode;
import Semantic.AST.Expressions.ExpressionNode;
import Semantic.AST.Expressions.References.ParamExpressionNode;
import Semantic.ST.Attribute;
import Semantic.ST.Parameter;
import Semantic.ST.Type;
import Semantic.SemExceptions.SemanticException;

public class LocalVarNode extends SentenceNode{
    private final Token tokenName;
    private final ExpressionNode compExpression;
    private Type varType;
    private final BlockNode blockNode;
    private int offset;

    public LocalVarNode(Token name, ExpressionNode e, BlockNode blockNode){
        compExpression=e;
        tokenName = name;
        this.blockNode = blockNode;
    }

    @Override
    public void check() throws SemanticException {
        varType = compExpression.check();
        if (varType.getNameType().equals("null")) {
            throw new SemanticException(tokenName.getLexeme(),"Se intento crear una variable de tipo null: ",tokenName.getLine());
        }
        LocalVarNode fatherVar = blockNode.getVar(tokenName);
        LocalVarNode localVar = blockNode.getLocalVarList().get(tokenName.getLexeme());
        Parameter param = blockNode.getMethod().getParameters().get(tokenName.getLexeme());
        Attribute attribute = blockNode.getClassElement().getAttributes().get(tokenName.getLexeme());
        if (localVar != null || param != null || attribute != null || fatherVar != null) {
            throw new SemanticException(tokenName.getLexeme(), "La variable ya existe: ", tokenName.getLine());
        }
        if (compExpression instanceof ParamExpressionNode paramReference){
            if (paramReference.getExpression() instanceof AssignExpNode){
                throw new SemanticException(tokenName.getLexeme(),"Expresion invalida a izquierda de asignacion: ", tokenName.getLine());
            }
        }
        blockNode.addLocalVar(tokenName.getLexeme(),this);
        checked = true;
    }

    @Override
    public void generateCode() {
        offset = blockNode.getLastOffsetValue();
        blockNode.addLastOffsetValue();
        compExpression.generateCode();
    }

    public String getTokenName() {
        return tokenName.getLexeme();
    }

    public int getTokenLine(){
        return tokenName.getLine();
    }

    public Type getVarType() {
        return varType;
    }
}
