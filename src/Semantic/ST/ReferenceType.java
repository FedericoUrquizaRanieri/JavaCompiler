package Semantic.ST;

import Lexical.Analyzer.Token;
import Main.MainSemantic;
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
    public void isCompatible(String neededType, Token operation) throws SemanticException {
        if (!token.getLexeme().equals(neededType)){
            throw new SemanticException(operation.getLexeme(),"El tipo actual no es el tipo esperado en: ",operation.getLine());
        }
    }

    @Override
    public void isOperandCompatibleUnary(Token typeToken) throws SemanticException {
        throw new SemanticException(token.getLexeme(),"Tipo referenciado es incompatible con operacion unaria",token.getLine());
    }

    @Override
    public void isOperandCompatibleBinary(Token typeToken, Type typeExp) throws SemanticException {
        var listOp = List.of("!=","==");
        if (!listOp.contains(typeToken.getLexeme())){
            throw new SemanticException(typeToken.getLexeme(),"Tipos actuales incompatibles con operacion ",typeToken.getLine());
        }
        if(!token.getTokenName().equals(typeExp.getTokenType().getTokenName())){
            isSameType(typeExp, typeToken);
        }
    }

    @Override
    public void compareTypes(Type type, Token operator) throws SemanticException{
        if (!(token.getLexeme().equals("String") && type.getTokenType().getTokenName().equals("String"))){
            if (!token.getLexeme().equals(type.getTokenType().getLexeme())){
                isSameType(type,operator);
            }
        }
    }

    public void isSameType(Type typeSon, Token operator) throws SemanticException{
        String className;
        if (typeSon.getTokenType().getTokenName().equals("String")){
            className="String";
        } else
            className = typeSon.getNameType();
        if (MainSemantic.symbolTable.classes.get(className)==null){
            throw new SemanticException(className,"Operacion fallida por tipo inexistente: ",typeSon.getTokenType().getLine());
        }
        Token currentFather = MainSemantic.symbolTable.classes.get(className).getInheritance();
        while (currentFather!=null){
            if (currentFather.getLexeme().equals(nameType)){
                return;
            }
            currentFather = MainSemantic.symbolTable.classes.get(currentFather.getLexeme()).getInheritance();
        }
        throw new SemanticException(operator.getLexeme(),"Operacion fallida por tipo incompatible: ",operator.getLine());
    }

    public Token getOptionalGeneric() {
        return optionalGeneric;
    }
}