package Semantic.AST.Expressions.References;

import Lexical.Analyzer.Token;
import Semantic.AST.Chains.ChainedNode;
import Semantic.AST.Chains.EmptyChainedNode;
import Semantic.AST.Sentences.BlockNode;
import Semantic.ST.Class;
import Semantic.ST.ReferenceType;
import Semantic.ST.Type;
import Semantic.SemExceptions.SemanticException;

public class ThisCallNode extends ReferenceNode {
    private final Class thisClass;
    private final BlockNode block;
    private final Token mainToken;

    public ThisCallNode(Token mainToken, Class c, BlockNode blockNode){
        thisClass = c;
        chainedElement = new EmptyChainedNode();
        block=blockNode;
        this.mainToken = mainToken;
    }

    @Override
    public Type check() throws SemanticException {
        if(block.getMethod().getModifier()!=null && block.getMethod().getModifier().getLexeme().equals("static")){
            throw new SemanticException(mainToken.getLexeme(), "La llamada no se puede realizar en metodos estaticos: ", mainToken.getLine());
        }
        Type retType = new ReferenceType(thisClass.getClassToken(),null);
        Type chainedType = chainedElement.check(retType);
        if(chainedType.getNameType().equals("Universal")){
            return new ReferenceType(thisClass.getClassToken(),null);
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

    @Override
    public void generateCode() {
        //TODO revisar encadenado?
    }
}
