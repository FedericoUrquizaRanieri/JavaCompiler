package Semantic.AST.Expressions.References;

import Lexical.Analyzer.Token;
import Semantic.AST.Chains.ChainedNode;
import Semantic.AST.Chains.EmptyChainedNode;
import Semantic.AST.Sentences.BlockNode;
import Semantic.AST.Sentences.LocalVarNode;
import Semantic.ST.Attribute;
import Semantic.ST.Parameter;
import Semantic.ST.ReferenceType;
import Semantic.ST.Type;
import Semantic.SemExceptions.SemanticException;

public class AccessVarNode extends ReferenceNode {
    private final Token varToken;
    private final BlockNode blockNode;

    public AccessVarNode(Token varToken, BlockNode block) {
        chainedElement = new EmptyChainedNode();
        this.varToken = varToken;
        blockNode = block;
    }

    @Override
    public Type check() throws SemanticException {
        LocalVarNode localVar = blockNode.getLocalVarList().get(varToken.getLexeme());
        Parameter param = blockNode.getMethod().getParameters().get(varToken.getLexeme());
        Attribute attribute = blockNode.getClassElement().getAttributes().get(varToken.getLexeme());
        if (localVar == null && param == null && attribute == null){
            throw new SemanticException(varToken.getLexeme(),"La variable a la que se accede no exite: ", varToken.getLine());
        }
        Type chainedType = chainedElement.check();
        if(chainedType.getNameType().equals("Universal")){
            if (localVar != null) {
                return localVar.getVarType();
            } else if (param != null) {
                return param.getType();
            } else {
                return attribute.getType();
            }
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
}
