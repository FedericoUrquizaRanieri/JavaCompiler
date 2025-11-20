package Semantic.AST.Sentences;

import Lexical.Analyzer.Token;
import Main.MainGen;
import Semantic.AST.Expressions.ExpressionNode;
import Semantic.ST.Method;
import Semantic.ST.Type;
import Semantic.SemExceptions.SemanticException;

public class ReturnNode extends SentenceNode{
    private final ExpressionNode retValue;
    private final Type retType;
    private final Token mainToken;
    private final Method method;

    public ReturnNode(ExpressionNode e,Type retType, Token mainToken, Method method){
        retValue=e;
        this.retType = retType;
        this.mainToken = mainToken;
        this.method = method;
    }

    @Override
    public void check() throws SemanticException {
        Type realType = retValue.check();
        if (retType==null){
            if (!realType.getNameType().equals("Universal")){
                throw new SemanticException(mainToken.getLexeme(),"El tipo de retorno esperado por el metodo no coincide con el retorno vacio en ", mainToken.getLine());
            }
        } else if (retType.getTokenType().getTokenName().equals("idClase")){
            if (realType.getNameType().equals("Universal")){
                throw new SemanticException(mainToken.getLexeme(),"El tipo de retorno esperado por el metodo no coincide con el retorno vacio en ", mainToken.getLine());
            }
        }
        else retType.compareTypes(retValue.check(),mainToken);
        checked = true;
    }

    @Override
    public void generateCode() {
        if(retType != null) {
            retValue.generateCode();
            int returnOffset = method.getParameters().size() + 3;
            if(method.getModifier() == null || method.getModifier() != null && !method.getModifier().getLexeme().equals("static")) {
                returnOffset++;
            }
            MainGen.symbolTable.instructionsList.add("STORE " + returnOffset + "; Guardo retorno");
        }
        int memFree;
        if(method.getBlock().getLastOffsetValue() > 0) {
            MainGen.symbolTable.instructionsList.add("FMEM " + method.getBlock().getLastOffsetValue());
        }
        MainGen.symbolTable.instructionsList.add("STOREFP ; Usa ED para volver a RA llamador");
        if (method.getModifier() != null && method.getModifier().getLexeme().equals("static"))
            memFree = method.getParameters().size();
        else memFree = method.getParameters().size() + 1;
        MainGen.symbolTable.instructionsList.add("RET "+memFree+" ; Libera los parametros y retorna de la unidad");
    }
}
