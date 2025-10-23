package Semantic.AST.Sentences;

import Lexical.Analyzer.Token;
import Semantic.AST.Expressions.ExpressionNode;
import Semantic.ST.Type;
import Semantic.SemExceptions.SemanticException;

public class ReturnNode extends SentenceNode{
    private final ExpressionNode retValue;
    private final Type retType;
    private final Token mainToken;

    public ReturnNode(ExpressionNode e,Type retType, Token mainToken){
        retValue=e;
        this.retType = retType;
        this.mainToken = mainToken;
    }

    @Override
    public void check() throws SemanticException {
        Type realType = retValue.check();
        if (retType==null){
            if (realType.getNameType().equals("Universal")){
                throw new SemanticException(mainToken.getLexeme(),"El tipo de retorno esperado por el metodo no coincide con el retorno vacio en ", mainToken.getLine());
            }
        }
        else if (!retType.compareTypes(retValue.check())){ //TODO revisar codigos de error en estos casos donde no hay elemento para elegir
            throw new SemanticException(mainToken.getLexeme(),"El tipo de retorno esperado por el metodo no coincide con "+realType.getNameType()+" en ", mainToken.getLine());
        }
        checked = true;
    }
}
