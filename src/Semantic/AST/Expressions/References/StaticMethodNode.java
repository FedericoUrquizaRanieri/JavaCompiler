package Semantic.AST.Expressions.References;

import Lexical.Analyzer.Token;
import Main.MainGen;
import Semantic.AST.Chains.ChainedNode;
import Semantic.AST.Chains.EmptyChainedNode;
import Semantic.AST.Expressions.ExpressionNode;
import Semantic.ST.Class;
import Semantic.ST.Method;
import Semantic.ST.Parameter;
import Semantic.ST.Type;
import Semantic.SemExceptions.SemanticException;
import com.sun.tools.javac.Main;

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
        Class currentClass = MainGen.symbolTable.classes.get(classElement.getLexeme());
        if (currentClass==null){
            throw new SemanticException(classElement.getLexeme(),"La clase llamada no existe:", classElement.getLine());
        } else if (currentClass.getMethods().get(methodElement.getLexeme())==null){
            throw new SemanticException(methodElement.getLexeme(),"El metodo no existe en la clase mencionada", methodElement.getLine());
        } else if (currentClass.getMethods().get(methodElement.getLexeme()).getModifier() == null || (currentClass.getMethods().get(methodElement.getLexeme()).getModifier() != null && !currentClass.getMethods().get(methodElement.getLexeme()).getModifier().getLexeme().equals("static"))){
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
                if (!p.getType().getTokenType().getLexeme().equals(ct.getTokenName()))
                    throw new SemanticException(ct.getLexeme(),"El parametro es de tipo incorrecto: ",ct.getLine());
            i++;
        }
    }

    @Override
    public void generateCode() {
        Method method = MainGen.symbolTable.classes.get(classElement.getLexeme()).getMethods().get(methodElement.getLexeme());
        if (method.getReturnType()!=null && !method.getReturnType().getTokenType().getLexeme().equals("void")){
            MainGen.symbolTable.instructionsList.add("RMEM 1 ; Reservo retorno");
        }
        for (ExpressionNode e :args){
            e.generateCode();
        }
        String originalClass = method.getOriginalClass().getClassName();
        MainGen.symbolTable.instructionsList.add("PUSH lblMet"+methodElement.getLexeme()+"@"+originalClass);
        MainGen.symbolTable.instructionsList.add("CALL");
    }
}