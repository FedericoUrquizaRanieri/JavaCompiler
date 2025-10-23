package Semantic.ST;

import Lexical.Analyzer.Token;
import Semantic.SemExceptions.SemanticException;

import java.util.List;

public class ReferenceType implements Type{
    private final String nameType;
    private final Token token;
    private final Token optionalGeneric;

    public ReferenceType(Token token, Token optionalGeneric){
        this.token = token;
        nameType = token.getLexeme();
        this.optionalGeneric = optionalGeneric;
    }

    @Override
    public Token getTokenType() {
        return token;
    }

    @Override
    public String getNameType() {
        return nameType;
    }

    @Override
    public void isCompatible(String neededType) throws SemanticException {
        if (!token.getLexeme().equals(neededType)){
            throw new SemanticException(token.getLexeme(),"El tipo actual no es el tipo esperado: ",token.getLine());
        }
    }

    @Override
    public void isOperandCompatibleUnary(Token typeToken) throws SemanticException {
        throw new SemanticException(token.getLexeme(),"Tipo referenciado es incompatible con operacion unaria",token.getLine());
    }

    @Override
    public void isOperandCompatibleBinary(Token typeToken, Type typeExp) throws SemanticException {
        var listOp = List.of("!=","==");
        if(!nameType.equals(typeExp.getNameType())){
            //TODO revisar herencia aca
            throw new SemanticException(typeToken.getLexeme(),"Tipos incompatibles para operar sobre",typeToken.getLine());
        }
        if (!listOp.contains(typeToken.getLexeme())){
            throw new SemanticException(typeToken.getLexeme(),"Tipos actuales incompatibles con operacion ",typeToken.getLine());
        }
    }

    @Override
    public void compareTypes(Type type) throws SemanticException{
        if (!token.getTokenName().equals(type.getTokenType().getTokenName())){
            //TODO revisar herencia aca?
            throw new SemanticException(type.getTokenType().getLexeme(),"Asignacion fallida por tipo incompatible: ",type.getTokenType().getLine());
        }
    }

    public Token getOptionalGeneric() {
        return optionalGeneric;
    }
}