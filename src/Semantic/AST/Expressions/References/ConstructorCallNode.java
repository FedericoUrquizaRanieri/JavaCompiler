package Semantic.AST.Expressions.References;

import Lexical.Analyzer.Token;
import Main.MainSemantic;
import Semantic.AST.Chains.ChainedNode;
import Semantic.AST.Chains.EmptyChainedNode;
import Semantic.AST.Expressions.ExpressionNode;
import Semantic.ST.*;
import Semantic.ST.Class;
import Semantic.SemExceptions.SemanticException;

import java.util.HashMap;
import java.util.List;

public class ConstructorCallNode extends ReferenceNode {
    private final Token classElement;
    private final List<ExpressionNode> args;

    public ConstructorCallNode(Token classElement, List<ExpressionNode> args) {
        this.classElement = classElement;
        this.args = args;
        chainedElement = new EmptyChainedNode();
    }

    @Override
    public Type check() throws SemanticException {
        Class currentClass = MainSemantic.symbolTable.existsClass(classElement);
        Type retType = new ReferenceType(classElement,null);
        if(currentClass == null){
            throw new SemanticException(classElement.getLexeme(),"La clase del constructor referenciado no existe: ",classElement.getLine());
        } else{
            Constructor c = MainSemantic.symbolTable.classes.get(classElement.getLexeme()).getConstructors().get(classElement.getLexeme());
            if (c==null){
                throw new SemanticException(classElement.getLexeme(),"No es posible crear un object: ",classElement.getLine());
            }
            parametersAreEqual(c.getParameters());
        }
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

    @Override
    public void generateCode() {
        for(ExpressionNode e:args){
            e.generateCode();
        }
    }
}
