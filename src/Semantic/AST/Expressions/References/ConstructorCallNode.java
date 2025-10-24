package Semantic.AST.Expressions.References;

import Lexical.Analyzer.Token;
import Main.MainSemantic;
import Semantic.AST.Chains.ChainedNode;
import Semantic.AST.Chains.EmptyChainedNode;
import Semantic.AST.Expressions.ExpressionNode;
import Semantic.ST.Class;
import Semantic.ST.Constructor;
import Semantic.ST.Parameter;
import Semantic.ST.Type;
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
        if(currentClass == null){
            Constructor c = MainSemantic.symbolTable.currentClass.getConstructors().get(classElement.getLexeme());
            parametersAreEqual(c.getParameters());
        } else
            throw new SemanticException(classElement.getLexeme(),"La clase del constructor referenciado no existe: ",classElement.getLine());
        return null;
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
                throw new SemanticException(ct.getLexeme(),"El parametro es de tipo incorrecot: ",ct.getLine());
            i++;
        }
    }
}
