package Semantic.AST.Sentences;

public class NullBlockNode extends BlockNode{
    public NullBlockNode(){
        checked = false;
    }

    @Override
    public void check() {
        checked = true;
    }
}
