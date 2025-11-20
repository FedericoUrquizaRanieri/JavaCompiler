package Semantic.AST.Chains;

import Lexical.Analyzer.Token;
import Main.MainGen;
import Semantic.ST.Attribute;
import Semantic.ST.Class;
import Semantic.ST.PrimitiveType;
import Semantic.ST.Type;
import Semantic.SemExceptions.SemanticException;

public class ChainedVarNode extends ChainedNode{
    private Class previousClass;
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
            previousClass = MainGen.symbolTable.existsClass(lastClass.getTokenType());
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

    @Override
    public void generateCode() {
        Attribute atr = previousClass.getAttributes().get(idToken.getLexeme());
        if (!isLeftSided || !(chainedNode instanceof EmptyChainedNode)){
            MainGen.symbolTable.instructionsList.add("LOADREF "+atr.getOffset()+" ; Cargo direccion de atributo");
        } else {
            MainGen.symbolTable.instructionsList.add("SWAP ; Pongo this en SP - 1");
            MainGen.symbolTable.instructionsList.add("STOREREF "+atr.getOffset()+" ; Guardo valor en la direccion del atributo");
        }
        if (chainedNode != null)
            chainedNode.generateCode();
    }
}
