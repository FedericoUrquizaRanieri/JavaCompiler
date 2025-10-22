package Semantic.AST.Sentences;

import Lexical.Analyzer.Token;
import Semantic.AST.Expressions.ExpressionNode;
import Semantic.ST.Type;

public class LocalVarNode extends SentenceNode{
    private final Token tokenName;
    private final ExpressionNode compExpression;
    private Type varType;

    public LocalVarNode(Token name, ExpressionNode e){
        compExpression=e;
        tokenName = name;
    }

    @Override
    public void check() {
        varType = compExpression.check();
        checked = true;
    }
}
