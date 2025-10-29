package Semantic.AST.Chains;

import Lexical.Analyzer.Token;
import Main.MainSemantic;
import Semantic.ST.Attribute;
import Semantic.ST.Class;
import Semantic.ST.PrimitiveType;
import Semantic.ST.Type;
import Semantic.SemExceptions.SemanticException;

public class ChainedVarNode extends ChainedNode{
    public ChainedVarNode(Token mainToken) {
        idToken = mainToken;
    }

    @Override
    public void setChainedNode(ChainedNode chainedNode) {
        this.chainedNode = chainedNode;
    }

    @Override
    public Type check(Type lastClass) throws SemanticException {
        if (lastClass != null){
            if (lastClass instanceof PrimitiveType){
                throw new SemanticException(idToken.getLexeme(), "La llamada encadenada se hace sobre una variable primitiva: ", idToken.getLine());
            }
            Class previousClass = MainSemantic.symbolTable.existsClass(lastClass.getTokenType());
            Attribute attribute = previousClass.getAttributes().get(idToken.getLexeme());
            if(attribute==null){
                throw new SemanticException(idToken.getLexeme(),"La variable a la que se accede no exite: ", idToken.getLine());
            }
            Type chainedType = chainedNode.check(attribute.getType());
            if(chainedType.getNameType().equals("Universal")){
                return attribute.getType();
            } else {
                return chainedType;
            }
        }
        throw new SemanticException(idToken.getLexeme(), "Se intenta llamar sobre tipo nulo: ", idToken.getLine());
    }

    @Override
    public ChainedNode getChainedElement() {
        return chainedNode;
    }
}
