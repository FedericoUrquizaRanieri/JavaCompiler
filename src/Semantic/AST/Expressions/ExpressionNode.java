package Semantic.AST.Expressions;

import Semantic.ST.Type;

public abstract class ExpressionNode {
    protected boolean checked = false;
    public boolean isChecked() {
        return checked;
    }
    public abstract Type check();
}
