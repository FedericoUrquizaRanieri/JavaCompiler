package Lexical;

import Lexical.LexExceptions.LexicException;
import SourceManager.SourceManager;

import java.io.IOException;

public class LexicalAnalyzer{
    private final SourceManager fileManager;
    private char currentChar;
    private String lexeme;

    public LexicalAnalyzer(SourceManager fileManager){
        this.fileManager=fileManager;
    }

    public Token getNextToken() throws IOException, LexicException {
        lexeme = "";
        return state0();
    }

    private void changeLexeme(){
        lexeme = lexeme + currentChar;
    }

    private void nextChar() throws IOException {
        currentChar = fileManager.getNextChar();
    }

    private Token state0() {
        //TODO
        return null;
    }
}
