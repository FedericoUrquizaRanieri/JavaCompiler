package Semantic.ST;

import Lexical.Analyzer.Token;
import Main.MainGen;
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
        if (!typeExp.getTokenType().getTokenName().equals("Universal") && !token.getTokenName().equals("Universal")){
            var listOp = List.of("!=","==");
            if (!listOp.contains(typeToken.getLexeme())){
                throw new SemanticException(typeToken.getLexeme(),"Tipos actuales incompatibles con operacion ",typeToken.getLine());
            }
            if(!token.getLexeme().equals(typeExp.getTokenType().getLexeme())){
                isSameTypeBinary(typeExp, typeToken);
            }
        }
    }

    @Override
    public void compareTypes(Type type, Token operator) throws SemanticException{
        if (!type.getTokenType().getTokenName().equals("Universal")){
            if (!(token.getLexeme().equals("String") && type.getTokenType().getTokenName().equals("String"))){
                if (!token.getLexeme().equals(type.getTokenType().getLexeme())){
                    isSameType(type,operator);
                }
            }
        }
    }

    public void isSameType(Type typeSon, Token operator) throws SemanticException{
        String className;
        if (typeSon.getTokenType().getTokenName().equals("String")){
            className="String";
        } else
            className = typeSon.getNameType();
        if (MainGen.symbolTable.classes.get(className)==null){
            throw new SemanticException(className,"Operacion fallida por tipo inexistente: ",typeSon.getTokenType().getLine());
        }
        Token currentFather = MainGen.symbolTable.classes.get(className).getInheritance();
        while (currentFather!=null){
            if (currentFather.getLexeme().equals(nameType)){
                return;
            }
            currentFather = MainGen.symbolTable.classes.get(currentFather.getLexeme()).getInheritance();
        }
        throw new SemanticException(operator.getLexeme(),"Operacion fallida por tipo incompatible: ",operator.getLine());
    }

    public void isSameTypeBinary(Type typeSon, Token operator) throws SemanticException {
        String className;
        if (typeSon.getTokenType().getTokenName().equals("String")){
            className="String";
        } else
            className = typeSon.getNameType();
        if (MainGen.symbolTable.classes.get(nameType) == null)
            throw new SemanticException(nameType, "Operación fallida por tipo inexistente: ", typeSon.getTokenType().getLine());
        if (MainGen.symbolTable.classes.get(className) == null)
            throw new SemanticException(className, "Operación fallida por tipo inexistente: ", typeSon.getTokenType().getLine());
        if (nameType.equals(className)) {
            return;
        }
        if (isSubtypeOf(className, nameType)) {
            return;
        }
        if (isSubtypeOf(nameType, className)) {
            return;
        }
        throw new SemanticException(operator.getLexeme(), "Operación fallida por tipo incompatible: ", operator.getLine());
    }

    private boolean isSubtypeOf(String child, String parent) {
        Token currentFather = MainGen.symbolTable.classes.get(child).getInheritance();
        while (currentFather != null) {
            if (currentFather.getLexeme().equals(parent)) {
                return true;
            }
            currentFather = MainGen.symbolTable.classes
                    .get(currentFather.getLexeme())
                    .getInheritance();
        }
        return false;
    }

    public Token getOptionalGeneric() {
        return optionalGeneric;
    }
}