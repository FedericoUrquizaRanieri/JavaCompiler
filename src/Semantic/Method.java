package Semantic;

import Lexical.Analyzer.Token;
import Main.MainSemantic;
import Semantic.SemExceptions.SemanticException;

import java.util.LinkedHashMap;
import java.util.Objects;

public class Method {
    private final String name;
    private Type returnType;
    private final Token token;
    private Token modifier;
    private final LinkedHashMap<String,Parameter> parameters;
    private boolean block;

    public Method(Token token){
        parameters = new LinkedHashMap<>();
        this.name = token.getLexeme();
        this.token = token;
    }

    public void checkStatements() throws SemanticException {
        if(returnType!=null){
            if(Objects.equals(returnType.getTokenType().getTokenName(), "idClase")){
                if (MainSemantic.symbolTable.existsClass(returnType.getTokenType())==null){
                    throw new SemanticException(returnType.getTokenType().getLexeme(),"Se intento agregar un tipo de retorno inexistente ",returnType.getTokenType().getLine());
                }
            }
        }
        if(block && modifier!=null && Objects.equals(modifier.getTokenName(), "pr_abstract")){
            throw new SemanticException(token.getLexeme(), "Se intento agregar un bloque a un metodo ", token.getLine());
        }
        if(!block && (modifier == null || !Objects.equals(modifier.getTokenName(), "pr_abstract"))){
            throw new SemanticException(token.getLexeme(), "Se intento agregar un metodo sin bloque ", token.getLine());
        }
        for (Parameter p : parameters.values()){
            p.checkStatements();
        }
    }

    public void addParam(Parameter p) throws SemanticException {
        if( parameters.putIfAbsent(p.getName(),p)!=null)
            throw new SemanticException(p.getName(),"Se intento agregar un parametro repetido llamada ",p.getToken().getLine());
    }

    public LinkedHashMap<String, Parameter> getParameters() {
        return parameters;
    }

    public Token getToken() {
        return token;
    }

    public Token getModifier() {
        return modifier;
    }

    public Type getReturnType() {
        return returnType;
    }

    public boolean hasNoBlock(){
        return !block;
    }

    public String getName() {
        return name;
    }

    public void setModifier(Token modifier) {
        this.modifier = modifier;
    }

    public void setReturnType(Type returnType) {
        this.returnType = returnType;
    }

    public void setBlock(boolean block) {
        this.block = block;
    }
}
