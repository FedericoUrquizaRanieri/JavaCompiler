package Lexical;

public class Token {
    private final String tokenName;
    private final String lexeme;
    private final int line;

    public Token(String name, String lex, int l){
        tokenName = name;
        lexeme = lex;
        line  = l;
    }

    public String getTokenName(){
        return tokenName;
    }
    public String getLexeme(){
        return lexeme;
    }
    public int getLine(){
        return line;
    }
}
