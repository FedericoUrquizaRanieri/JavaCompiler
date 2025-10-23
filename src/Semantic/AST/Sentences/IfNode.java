package Semantic.AST.Sentences;

import Lexical.Analyzer.Token;
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
        if(!condition.check().isCompatible("boolean")){
            throw new SemanticException(mainToken.getLexeme(), "La condicion no es booleana en ", mainToken.getLine());
        }
        body.check();
        elseSentence.check();
        checked = true;
    }
}
