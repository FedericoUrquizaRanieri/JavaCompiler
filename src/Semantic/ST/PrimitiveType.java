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
    public void isCompatible(String neededType, Token operation) throws SemanticException{
        if (neededType.equals("boolean")){
            if (!token.getTokenName().equals("pr_boolean")){
                throw new SemanticException(operation.getLexeme(),"El tipo actual no es el tipo esperado: ",operation.getLine());
            }
        } else {
            if (!token.getLexeme().equals(neededType)){
                throw new SemanticException(operation.getLexeme(),"El tipo actual no es el tipo esperado: ",operation.getLine());
            }
        }
    }

    @Override
    public void isOperandCompatibleUnary(Token typeToken) throws SemanticException {
        if (token.getTokenName().equals("pr_int") && typeToken.getTokenName().equals("not")){
            throw new SemanticException(typeToken.getLexeme(),"Operacion incompatible sobre tipo int: ",typeToken.getLine());
        }
        if (token.getTokenName().equals("pr_boolean") && !typeToken.getTokenName().equals("not")){
            throw new SemanticException(typeToken.getLexeme(),"Operacion incompatible sobre tipo boolean: ",typeToken.getLine());
        }
        if (!(token.getTokenName().equals("pr_boolean") || token.getTokenName().equals("pr_int"))){
            throw new SemanticException(typeToken.getLexeme(),"Tipo incompatible para operaciones unarias: ",typeToken.getLine());
        }
    }

    @Override
    public void isOperandCompatibleBinary(Token typeToken, Type typeExp) throws SemanticException {
        var listInt = List.of("+","-","*","%","/","<","<=",">",">=","==","!=");
        var listBool = List.of("&&","||","==","!=");
        if (token.getTokenName().equals("Universal") && !listBool.contains(typeToken.getLexeme())){
            throw new SemanticException(typeToken.getLexeme(),"Operacion sobre nulo incompatible",typeToken.getLine());
        } else if (!token.getTokenName().equals("Universal")){
            if(!token.getTokenName().equals(typeExp.getTokenType().getTokenName())){
                throw new SemanticException(typeToken.getLexeme(),"Tipos incompatibles para operar sobre ",typeToken.getLine());
            }
            if (token.getTokenName().equals("pr_int") && typeExp.getTokenType().getTokenName().equals("pr_int") && (!listInt.contains(typeToken.getLexeme()))){
                throw new SemanticException(typeToken.getLexeme(),"Tipos actuales incompatibles con operacion ",typeToken.getLine());
            }
            if (token.getTokenName().equals("pr_boolean") && typeExp.getTokenType().getTokenName().equals("pr_boolean") && !listBool.contains(typeToken.getLexeme())){
                throw new SemanticException(typeToken.getLexeme(),"Tipos actuales incompatibles con operacion ",typeToken.getLine());
            }
            if (token.getTokenName().equals("pr_char") || typeExp.getTokenType().getTokenName().equals("pr_char")){
                if (!(typeToken.getLexeme().equals("==") || typeToken.getLexeme().equals("!=")))
                    throw new SemanticException(typeToken.getLexeme(),"Tipo char es incompatibles con operacion ",typeToken.getLine());
            }
        }
    }

    @Override
    public void compareTypes(Type type, Token operator) throws SemanticException{
        if (!token.getTokenName().equals(type.getTokenType().getTokenName())){
            throw new SemanticException(operator.getLexeme(),"Asignacion fallida por tipo incompatible: ",operator.getLine());
        }
    }
}
