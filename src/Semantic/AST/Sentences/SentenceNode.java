package Semantic.AST.Sentences;

import Semantic.SemExceptions.SemanticException;

public abstract class SentenceNode {
    protected boolean checked = false;
    public abstract void check();
    public boolean isChecked() {
        return checked;
    }
}
