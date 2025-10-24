package Semantic.AST.Chains;

import Lexical.Analyzer.Token;
import Main.MainSemantic;
import Semantic.AST.Expressions.ExpressionNode;
import Semantic.ST.*;
import Semantic.ST.Class;
import Semantic.SemExceptions.SemanticException;

import java.util.HashMap;
import java.util.List;

public class ChainedMethodNode extends ChainedNode{
    private final List<ExpressionNode> parameters;
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
        if (lastClass instanceof ReferenceType){
            throw new SemanticException(lastClass.getNameType(),"La llamada encadenada se hace sobre una variable primitiva: ", lastClass.getTokenType().getLine());
        }
        Class previousClass = MainSemantic.symbolTable.existsClass(lastClass.getTokenType());
        Method method = previousClass.getMethods().get(idToken.getLexeme());
        if(method==null){
            throw new SemanticException(idToken.getLexeme(),"El metodo al que se accede no exite: ", idToken.getLine());
        } else parametersAreEqual(method.getParameters());
        Type chainedType = chainedNode.check(method.getReturnType());
        if(chainedType.getNameType().equals("Universal")){
            return method.getReturnType();
        } else {
            return chainedType;
        }
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
}
