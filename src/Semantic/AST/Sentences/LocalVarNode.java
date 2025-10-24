package Semantic.AST.Sentences;

import Lexical.Analyzer.Token;
import Semantic.AST.Expressions.ExpressionNode;
import Semantic.ST.Type;
import Semantic.SemExceptions.SemanticException;

public class LocalVarNode extends SentenceNode{
    private final Token tokenName;
    private final ExpressionNode compExpression;
    private Type varType;

    public LocalVarNode(Token name, ExpressionNode e){
        compExpression=e;
        tokenName = name;
    }

    @Override
    public void check() throws SemanticException {
        varType = compExpression.check();
        if (varType.getNameType()==null) {
            //TODO revisar todo esto porque no tengo las cosas necesarias
        }
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
