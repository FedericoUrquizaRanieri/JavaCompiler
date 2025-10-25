package Semantic.AST.Sentences;

import Lexical.Analyzer.Token;
import Semantic.AST.Expressions.ExpressionNode;
import Semantic.ST.Attribute;
import Semantic.ST.Parameter;
import Semantic.ST.Type;
import Semantic.SemExceptions.SemanticException;

public class LocalVarNode extends SentenceNode{
    private final Token tokenName;
    private final ExpressionNode compExpression;
    private Type varType;
    private final BlockNode blockNode;

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
        blockNode.addLocalVar(tokenName.getLexeme(),this);
        checked = true;
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
