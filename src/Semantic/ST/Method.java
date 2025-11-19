package Semantic.ST;

import Lexical.Analyzer.Token;
import Main.MainGen;
import Semantic.AST.Sentences.BlockNode;
import Semantic.SemExceptions.SemanticException;

import java.util.LinkedHashMap;
import java.util.Objects;

public class Method {
    private final String name;
    private Type returnType;
    private final Token token;
    private Token modifier;
    private final LinkedHashMap<String,Parameter> parameters;
    private BlockNode block;
    private int offset;
    private final Class originalClass;

    public Method(Token token, Class orig){
        parameters = new LinkedHashMap<>();
        this.name = token.getLexeme();
        this.token = token;
        this.originalClass = orig;
    }

    public void checkStatements() throws SemanticException {
        if(returnType!=null){
            if(Objects.equals(returnType.getTokenType().getTokenName(), "idClase")){
                if (MainGen.symbolTable.existsClass(returnType.getTokenType())==null){
                    throw new SemanticException(returnType.getTokenType().getLexeme(),"Se intento agregar un tipo de retorno inexistente ",returnType.getTokenType().getLine());
                }
            }
        }
        if(!hasNoBlock() && modifier!=null && Objects.equals(modifier.getTokenName(), "pr_abstract")){
            throw new SemanticException(token.getLexeme(), "Se intento agregar un bloque a un metodo ", token.getLine());
        }
        if(hasNoBlock() && (modifier == null || !Objects.equals(modifier.getTokenName(), "pr_abstract"))){
            throw new SemanticException(token.getLexeme(), "Se intento agregar un metodo sin bloque ", token.getLine());
        }
        for (Parameter p : parameters.values()){
            p.checkStatements();
        }
    }

    public void checkSentences() throws SemanticException{
        if (!block.isChecked()){
            block.check();
        }

    }

    public void addParam(Parameter p) throws SemanticException {
        if( parameters.putIfAbsent(p.getName(),p)!=null)
            throw new SemanticException(p.getName(),"Se intento agregar un parametro repetido llamada ",p.getToken().getLine());
    }

    public void generateCode(String className){
        if (modifier == null || !modifier.getLexeme().equals("static") || name.equals("main")){
            MainGen.symbolTable.instructionsList.add("");
            MainGen.symbolTable.instructionsList.add("lblMet"+name+"@"+className+": LOADFP");
            MainGen.symbolTable.instructionsList.add("LOADSP");
            MainGen.symbolTable.instructionsList.add("STOREFP");
            block.generateCode();
            MainGen.symbolTable.instructionsList.add("FMEM "+block.getLocalVarList().size());
            MainGen.symbolTable.instructionsList.add("STOREFP");
            if (modifier != null && modifier.getLexeme().equals("static")){
                MainGen.symbolTable.instructionsList.add("RET "+parameters.size()+" ; Libera los parametros y retorna de la unidad");
            } else
                MainGen.symbolTable.instructionsList.add("RET "+(parameters.size() + 1)+" ; Libera los parametros y retorna de la unidad");
        }
    }

    public void setParamsOffsets(){
        int i;
        if (modifier != null && modifier.getLexeme().equals("static"))
            i = 3;
        else
            i = 4;
        for(Parameter p : parameters.values()){
            p.setOffset(i);
            i++;
        }
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
        return block==null;
    }

    public void setBlock(BlockNode block){
        this.block=block;
    }

    public BlockNode getBlock() {
        return block;
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

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public Class getOriginalClass() {
        return originalClass;
    }
}
