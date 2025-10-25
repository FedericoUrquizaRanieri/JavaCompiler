package Semantic.ST;

import Lexical.Analyzer.Token;
import Semantic.SemExceptions.SemanticException;

import java.util.List;

public class PrimitiveType implements Type{
    private final String nameType;
    private final Token token;

    public PrimitiveType(Token token){
        this.token = token;
        nameType = token.getLexeme();
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
    public void isCompatible(String neededType) throws SemanticException{
        if (neededType.equals("boolean")){
            if (!token.getLexeme().equals("false") && !token.getLexeme().equals("true")){
                throw new SemanticException(token.getLexeme(),"El tipo actual no es el tipo esperado: ",token.getLine());
            }
        } else {
            if (!token.getLexeme().equals(neededType)){
                throw new SemanticException(token.getLexeme(),"El tipo actual no es el tipo esperado: ",token.getLine());
            }
        }
    }

    @Override
    public void isOperandCompatibleUnary(Token typeToken) throws SemanticException {
        if (token.getTokenName().equals("intLiteral") && typeToken.getTokenName().equals("not")){
            throw new SemanticException(typeToken.getLexeme(),"Operacion incompatible sobre tipo int: ",typeToken.getLine());
        }
        if ((nameType.equals("false") || nameType.equals("true")) && !typeToken.getTokenName().equals("not")){
            throw new SemanticException(typeToken.getLexeme(),"Operacion incompatible sobre tipo boolean: ",typeToken.getLine());
        }
        if (!((nameType.equals("false") || nameType.equals("true")) || token.getTokenName().equals("intLiteral"))){
            throw new SemanticException(token.getLexeme(),"Tipo incompatible para operaciones unarias: ",token.getLine());
        }
    }

    @Override
    public void isOperandCompatibleBinary(Token typeToken, Type typeExp) throws SemanticException {
        var listInt = List.of("+","-","*","%","/","<","<=",">",">=");
        var listBool = List.of("&&","||");
        if(!nameType.equals(typeExp.getNameType())){
            throw new SemanticException(typeToken.getLexeme(),"Tipos incompatibles para operar sobre",typeToken.getLine());
        }
        if (token.getTokenName().equals("intLiteral") && typeExp.getNameType().equals("intLiteral") && (!listInt.contains(typeToken.getLexeme()))){
            throw new SemanticException(typeToken.getLexeme(),"Tipos actuales incompatibles con operacion ",typeToken.getLine());
        }
        if ((nameType.equals("false") || nameType.equals("true")) && (typeExp.getNameType().equals("false") || typeExp.getNameType().equals("true")) && !listBool.contains(typeToken.getLexeme())){
            throw new SemanticException(token.getLexeme(),"Tipos actuales incompatibles con operacion ",token.getLine());
        }
    }

    @Override
    public void compareTypes(Type type, Token operator) throws SemanticException{
        if (!token.getTokenName().equals(type.getTokenType().getTokenName())){
            throw new SemanticException(operator.getLexeme(),"Asignacion fallida por tipo incompatible: ",operator.getLine());
        }
    }
}
