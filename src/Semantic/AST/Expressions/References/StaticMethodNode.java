package Semantic.AST.Expressions.References;

import Lexical.Analyzer.Token;
import Main.MainSemantic;
import Semantic.AST.Chains.ChainedNode;
import Semantic.AST.Chains.EmptyChainedNode;
import Semantic.AST.Expressions.ExpressionNode;
import Semantic.ST.Class;
import Semantic.ST.Parameter;
import Semantic.ST.Type;
import Semantic.SemExceptions.SemanticException;

import java.util.HashMap;
import java.util.List;

public class StaticMethodNode extends ReferenceNode {
    private final Token classElement;
    private final Token methodElement;
    private final List<ExpressionNode> args;

    public StaticMethodNode(Token classElement, Token methodElement, List<ExpressionNode> args) {
        this.classElement = classElement;
        this.methodElement = methodElement;
        this.args = args;
        chainedElement = new EmptyChainedNode();
    }

    @Override
    public Type check() throws SemanticException {
        Class currentClass = MainSemantic.symbolTable.classes.get(classElement.getLexeme());
        if (currentClass==null){
            throw new SemanticException(classElement.getLexeme(),"La clase llamada no existe:", classElement.getLine());
        } else if (currentClass.getMethods().get(methodElement.getLexeme())==null){
            throw new SemanticException(methodElement.getLexeme(),"El metodo no existe en la clase mencionada", methodElement.getLine());
        } else parametersAreEqual(currentClass.getMethods().get(methodElement.getLexeme()).getParameters());
        Type retType = currentClass.getMethods().get(methodElement.getLexeme()).getReturnType();
        Type chainedType = chainedElement.check(retType);
        if(chainedType.getNameType().equals("Universal")){
            return retType;
        } else {
            return chainedType;
        }
    }

    @Override
    public ChainedNode getChainedElement() {
        return chainedElement;
    }

    @Override
    public void setChainedElement(ChainedNode chainedNode) {
        chainedElement = chainedNode;
    }

    private void parametersAreEqual(HashMap<String, Parameter> map1) throws SemanticException {
        if (map1.size() != args.size())
            throw new SemanticException(classElement.getLexeme(),"Los parametros son de cantidad incorrecta en el llamado: ", classElement.getLine());
        var i = 0;
        for (Parameter p : map1.values()) {
            Token ct = args.get(i).check().getTokenType();
            if (!p.getType().getTokenType().getTokenName().equals(ct.getTokenName()))
                throw new SemanticException(ct.getLexeme(),"El parametro es de tipo incorrecto: ",ct.getLine());
            i++;
        }
    }
}