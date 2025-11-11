package Semantic.AST.Expressions;

import Semantic.ST.Type;
import Semantic.SemExceptions.SemanticException;

public abstract class ExpressionNode {
    protected boolean checked = false;
    public boolean isChecked() {
        return checked;
    }
    public abstract Type check() throws SemanticException;
    public abstract void generateCode();
}
