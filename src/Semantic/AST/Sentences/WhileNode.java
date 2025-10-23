package Semantic.AST.Sentences;

import Lexical.Analyzer.Token;
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
        if (!condition.check().isCompatible("boolean")){
            throw new SemanticException(mainToken.getLexeme(),"Los tipos son incompatibles en condicion de ",mainToken.getLine());
        }
        body.check();
        checked = true;
    }
}
