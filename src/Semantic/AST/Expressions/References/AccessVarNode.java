package Semantic.AST.Expressions.References;

import Lexical.Analyzer.Token;
import Main.MainGen;
import Semantic.AST.Chains.ChainedNode;
import Semantic.AST.Chains.EmptyChainedNode;
import Semantic.AST.Sentences.BlockNode;
import Semantic.AST.Sentences.LocalVarNode;
import Semantic.ST.Attribute;
import Semantic.ST.Parameter;
import Semantic.ST.PrimitiveType;
import Semantic.ST.Type;
import Semantic.SemExceptions.SemanticException;

public class AccessVarNode extends ReferenceNode {
    private final Token varToken;
    private final BlockNode blockNode;

    public AccessVarNode(Token varToken, BlockNode block) {
        chainedElement = new EmptyChainedNode();
        this.varToken = varToken;
        blockNode = block;
    }

    @Override
    public Type check() throws SemanticException {
        LocalVarNode fatherVar = blockNode.getVar(varToken);
        LocalVarNode localVar = blockNode.getLocalVarList().get(varToken.getLexeme());
        Parameter param = blockNode.getMethod().getParameters().get(varToken.getLexeme());
        Attribute attribute = blockNode.getClassElement().getAttributes().get(varToken.getLexeme());
        Type retType;
        if (localVar != null) {
            retType = localVar.getVarType();
        } else if (param != null) {
            retType = param.getType();
        } else if (attribute != null) {
            retType = attribute.getType();
        } else if (fatherVar != null) {
            retType = fatherVar.getVarType();
        } else {
            throw new SemanticException(varToken.getLexeme(), "La variable a la que se accede no exite: ", varToken.getLine());
        }
        if (blockNode.getMethod().getModifier() != null && blockNode.getMethod().getModifier().getLexeme().equals("static") && attribute != null) {
            throw new SemanticException(varToken.getLexeme(), "No es posible llamar a la variable en un metodo estatico: ", varToken.getLine());
        }
        Type chainedType = chainedElement.check(retType);
        if(chainedType.getNameType().equals("Universal")){
            return retType;
        } else {
            return chainedType;
        }
    }

    @Override
    public ChainedNode getChainedElement() {
        return chainedElement;
    }

    @Override
    public void setChainedElement(ChainedNode chainedNode) {
        chainedElement = chainedNode;
    }

    @Override
    public void generateCode() {
        Attribute atr = MainGen.symbolTable.classes.get(blockNode.getClassElement().getClassName()).getAttributes().get(varToken.getLexeme());
        if (atr!=null){
            MainGen.symbolTable.instructionsList.add("LOAD 3 ; Acceso a atributo");
            if (!isLeftSided || chainedElement != null){
                MainGen.symbolTable.instructionsList.add("LOADREF "+atr.getOffset()+" ; Cargo direccion de atributo");
            } else {
                MainGen.symbolTable.instructionsList.add("SWAP ; Muevo this a SP - 1");
                MainGen.symbolTable.instructionsList.add("STOREREF "+atr.getOffset()+" ; Guardo valor en atributo");
            }
        } else {
            var localOrParamOffset = 0;
            if (blockNode.getLocalVarList().get(varToken.getLexeme())!=null){
                localOrParamOffset = blockNode.getLocalVarList().get(varToken.getLexeme()).getOffset();
            } else if (blockNode.getMethod().getParameters().get(varToken.getLexeme()) != null) {
                localOrParamOffset =blockNode.getMethod().getParameters().get(varToken.getLexeme()).getOffset();
            }

            if (!isLeftSided || chainedElement != null)
                MainGen.symbolTable.instructionsList.add("LOAD "+localOrParamOffset+" ; Cargo la direccion de var");
            else MainGen.symbolTable.instructionsList.add("STORE "+localOrParamOffset+" ; Guardo valor en var");
        }
        if (chainedElement != null)
            chainedElement.generateCode();
    }
}
