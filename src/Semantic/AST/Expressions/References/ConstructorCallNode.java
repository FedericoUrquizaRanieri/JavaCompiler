package Semantic.AST.Expressions.References;

import Lexical.Analyzer.Token;
import Main.MainGen;
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
        Class currentClass = MainGen.symbolTable.existsClass(classElement);
        Type retType = new ReferenceType(classElement,null);
        if(currentClass == null){
            throw new SemanticException(classElement.getLexeme(),"La clase del constructor referenciado no existe: ",classElement.getLine());
        } else{
            Constructor c = MainGen.symbolTable.classes.get(classElement.getLexeme()).getConstructors().get(classElement.getLexeme());
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
        MainGen.symbolTable.instructionsList.add("RMEM 1");
        MainGen.symbolTable.instructionsList.add("PUSH "+MainGen.symbolTable.classes.get(classElement.getLexeme()).getLastAttributeOffset()+" ; atributos y VT");
        MainGen.symbolTable.instructionsList.add("PUSH simple_malloc ; reservar heap");
        MainGen.symbolTable.instructionsList.add("CALL");
        MainGen.symbolTable.instructionsList.add("DUP");
        MainGen.symbolTable.instructionsList.add("PUSH lblVT"+classElement.getLexeme()+" ; Etiqueta de la VT");
        MainGen.symbolTable.instructionsList.add("STOREREF 0");
        MainGen.symbolTable.instructionsList.add("DUP");
        for (ExpressionNode e : args) {
            e.generateCode();
            MainGen.symbolTable.instructionsList.add("SWAP");
        }
        MainGen.symbolTable.instructionsList.add("PUSH lblConstructor@"+classElement.getLexeme());
        MainGen.symbolTable.instructionsList.add("CALL");
        if (chainedElement != null)
            chainedElement.generateCode();
    }
}
