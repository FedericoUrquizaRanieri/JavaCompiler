package Semantic.AST.Sentences;

public abstract class SentenceNode {
    protected boolean checked = false;
    public abstract void check();
    public boolean isChecked() {
        return checked;
    }
}
