package Semantic.AST.Chains;

import Lexical.Analyzer.Token;
import Main.MainGen;
import Semantic.AST.Expressions.ExpressionNode;
import Semantic.ST.*;
import Semantic.ST.Class;
import Semantic.SemExceptions.SemanticException;

import java.util.HashMap;
import java.util.List;

public class ChainedMethodNode extends ChainedNode{
    private final List<ExpressionNode> parameters;
    private Class previousClass;
    public ChainedMethodNode(List<ExpressionNode> params, Token mainToken) {
        parameters = params;
        idToken = mainToken;
    }

    @Override
    public void setChainedNode(ChainedNode chainedNode) {
        this.chainedNode = chainedNode;
    }

    @Override
    public Type check(Type lastClass) throws SemanticException {
        if (lastClass != null) {
            if (!(lastClass instanceof ReferenceType)){
                throw new SemanticException(idToken.getLexeme(),"La llamada encadenada se hace sobre una variable primitiva: ", idToken.getLine());
            }
            previousClass = MainGen.symbolTable.existsClass(lastClass.getTokenType());
            if(previousClass==null){
                throw new SemanticException(idToken.getLexeme(),"La clase encadenada previa no existe: ", idToken.getLine());
            }
            Method method = previousClass.getMethods().get(idToken.getLexeme());
            if(method==null){
                throw new SemanticException(idToken.getLexeme(),"El metodo al que se accede no exite: ", idToken.getLine());
            } else if (method.getModifier() != null && method.getModifier().getLexeme().equals("static")) {
                throw new SemanticException(method.getToken().getLexeme(), "No es posible llamar a un metodo estatico en esta secuencia: ", method.getToken().getLine());
            } else{
                parametersAreEqual(method.getParameters());
            }
            Type chainedType = chainedNode.check(method.getReturnType());
            if(chainedType.getNameType().equals("Universal")){
                if (method.getReturnType()==null){
                    return new PrimitiveType(new Token("pr_null","null",0));
                } else return method.getReturnType();
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

    private void parametersAreEqual(HashMap<String, Parameter> map1) throws SemanticException {
        if (map1.size() != parameters.size())
            throw new SemanticException(idToken.getLexeme(),"Los parametros son de cantidad incorrecta en el llamado: ", idToken.getLine());
        var i = 0;
        for (Parameter p : map1.values()) {
            Token ct = parameters.get(i).check().getTokenType();
            if (!p.getType().getTokenType().getTokenName().equals(ct.getTokenName()))
                throw new SemanticException(ct.getLexeme(),"El parametro es de tipo incorrecto: ",ct.getLine());
            i++;
        }
    }

    @Override
    public void generateCode() {
        Method method = previousClass.getMethods().get(idToken.getLexeme());
        if (method.getModifier() != null && !method.getModifier().getLexeme().equals("static")){
            MainGen.symbolTable.instructionsList.add("POP ; Borro la referencia al objeto");
            if (!method.getReturnType().getTokenType().getLexeme().equals("void")){
                MainGen.symbolTable.instructionsList.add("RMEM 1 ; Reservo lugar para el retorno");
            }
            for (ExpressionNode p : parameters){
                p.generateCode();
            }
            MainGen.symbolTable.instructionsList.add("PUSH lblMet"+method.getName()+"@"+method.getOriginalClass().getClassName());
            MainGen.symbolTable.instructionsList.add("CALL");
        } else {
            if (method.getReturnType() !=null){
                MainGen.symbolTable.instructionsList.add("RMEM 1 ; Reservo lugar para el retorno");
                MainGen.symbolTable.instructionsList.add("SWAP");
            }
            for (ExpressionNode p : parameters){
                p.generateCode();
                MainGen.symbolTable.instructionsList.add("SWAP ; Muevo this");
            }
            MainGen.symbolTable.instructionsList.add("DUP ; Duplico this");
            MainGen.symbolTable.instructionsList.add("LOADREF 0 ; Cargo VT");
            MainGen.symbolTable.instructionsList.add("LOADREF "+method.getOffset()+" ; Cargo metodo "+method.getName());
            MainGen.symbolTable.instructionsList.add("CALL ; Llamo metodo");
        }

        if (chainedNode != null)
            chainedNode.generateCode();
    }
}
