package Semantic.AST.Expressions.References;

import Lexical.Analyzer.Token;
import Main.MainGen;
import Semantic.AST.Chains.ChainedNode;
import Semantic.AST.Chains.EmptyChainedNode;
import Semantic.AST.Expressions.ExpressionNode;
import Semantic.AST.Sentences.BlockNode;
import Semantic.ST.*;
import Semantic.SemExceptions.SemanticException;

import java.util.HashMap;
import java.util.List;

public class AccessMethodNode extends ReferenceNode {
    private final Token methodToken;
    private final List<ExpressionNode> params;
    private final BlockNode blockNode;

    public AccessMethodNode(List<ExpressionNode> params, Token methodToken, BlockNode block) {
        chainedElement = new EmptyChainedNode();
        this.params = params;
        this.methodToken = methodToken;
        this.blockNode = block;
    }

    @Override
    public Type check() throws SemanticException {
        Method method = blockNode.getClassElement().getMethods().get(methodToken.getLexeme());
        if (method == null) {
            throw new SemanticException(methodToken.getLexeme(), "El metodo no existe: ", methodToken.getLine());
        }  else{
            parametersAreEqual(method.getParameters());
        }
        Type chainedType = chainedElement.check(method.getReturnType());
        if(chainedType.getNameType().equals("Universal")){
            if (method.getReturnType()==null){
                return new PrimitiveType(new Token("pr_null","null",0));
            } else return method.getReturnType();
        } else {
            return chainedType;
        }
    }

    @Override
    public ChainedNode getChainedElement() {
        return chainedElement;
    }

    @Override
    public void setChainedElement(ChainedNode chainedElement) {
        this.chainedElement = chainedElement;
    }

    private void parametersAreEqual(HashMap<String, Parameter> map1) throws SemanticException {
        if (map1.size() != params.size())
            throw new SemanticException(methodToken.getLexeme(),"Los parametros son de cantidad incorrecta en el llamado: ", methodToken.getLine());
        var i = 0;
        for (Parameter p : map1.values()) {
            Type ct = params.get(i).check();
            if (!p.getType().getTokenType().getLexeme().equals(ct.getTokenType().getLexeme())){
                p.getType().compareTypes(ct,methodToken);
            }
            i++;
        }
    }

    @Override
    public void generateCode() {
        Method method = blockNode.getClassElement().getMethods().get(methodToken.getLexeme());
        if (method.getModifier() != null && method.getModifier().getLexeme().equals("static")) {
            if (method.getReturnType()!=null && !method.getReturnType().getTokenType().getLexeme().equals("void")){
                MainGen.symbolTable.instructionsList.add("RMEM 1 ; Reservo retorno");
            }
            for (ExpressionNode p : params){
                p.generateCode();
            }
            MainGen.symbolTable.instructionsList.add("PUSH lblMet"+method.getName()+"@"+method.getOriginalClass().getClassName());
            MainGen.symbolTable.instructionsList.add("CALL");
        } else {
            MainGen.symbolTable.instructionsList.add("LOAD 3 ; Cargo this");
            if (method.getReturnType() !=null){
                MainGen.symbolTable.instructionsList.add("RMEM 1 ; Reservo retorno");
                MainGen.symbolTable.instructionsList.add("SWAP");
            }
            for (ExpressionNode p : params){
                p.generateCode();
                MainGen.symbolTable.instructionsList.add("SWAP");
            }
            MainGen.symbolTable.instructionsList.add("DUP");
            MainGen.symbolTable.instructionsList.add("LOADREF 0 ; Cargo VT");
            MainGen.symbolTable.instructionsList.add("LOADREF "+method.getOffset()+" ; Cargo metodo");
            MainGen.symbolTable.instructionsList.add("CALL ; Llamo metodo");
        }
        if (chainedElement != null)
            chainedElement.generateCode();
    }
}
