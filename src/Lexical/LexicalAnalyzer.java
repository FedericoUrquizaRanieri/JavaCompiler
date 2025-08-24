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

    public Token getNextToken() throws LexicException {
        lexeme = "";
        nextChar();
        return state0();
    }

    private void changeLexeme(){
        lexeme = lexeme + currentChar;
    }

    private void nextChar(){
        try {
            currentChar = fileManager.getNextChar();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Token state0() {
        switch (currentChar){
            case SourceManager.END_OF_FILE: return new Token("EOF","EOF",fileManager.getLineNumber());
            case ' ', '\t', '\n': return state0();
        }
        if (Character.isLetter(currentChar)){
            changeLexeme();
            nextChar();
            return state1();
        } else if (Character.isDigit(currentChar)) {
            changeLexeme();
            nextChar();
            return state2();
        } else if (currentChar == '\'') {
            changeLexeme();
            nextChar();
            return state3();
        } else if (currentChar == '"') {
            changeLexeme();
            nextChar();
            return state4();
        } else if (currentChar == '>') {
            changeLexeme();
            nextChar();
            return state5();
        } else if (currentChar == '<') {
            changeLexeme();
            nextChar();
            return state6();
        } else if (currentChar == '=') {
            changeLexeme();
            nextChar();
            return state7();
        } else if (currentChar == '!') {
            changeLexeme();
            nextChar();
            return state8();
        } else if (currentChar == '&') {
            changeLexeme();
            nextChar();
            return state9();
        } else if (currentChar == '|') {
            changeLexeme();
            nextChar();
            return state10();
        } else if (currentChar == '+') {
            changeLexeme();
            nextChar();
            return state11();
        } else if (currentChar == '-') {
            changeLexeme();
            nextChar();
            return state12();
        } else if (currentChar == '%') {
            changeLexeme();
            nextChar();
            return state13();
        } else if (currentChar == '*') {
            changeLexeme();
            nextChar();
            return state14();
        } else if (currentChar == '/') {
            changeLexeme();
            nextChar();
            return state15();
        } else if (currentChar == '(') {
            changeLexeme();
            nextChar();
            return state16();
        } else if (currentChar == ')') {
            changeLexeme();
            nextChar();
            return state17();
        } else if (currentChar == '{') {
            changeLexeme();
            nextChar();
            return state18();
        } else if (currentChar == '}') {
            changeLexeme();
            nextChar();
            return state19();
        } else if (currentChar == ';') {
            changeLexeme();
            nextChar();
            return state20();
        } else if (currentChar == ',') {
            changeLexeme();
            nextChar();
            return state21();
        } else if (currentChar == ':') {
            changeLexeme();
            nextChar();
            return state22();
        } else if (currentChar == '.') {
            changeLexeme();
            nextChar();
            return state23();
        }
        return null;
    }

    private Token state23() {
        return null;
    }

    private Token state22() {
        return null;
    }

    private Token state21() {
        return null;
    }

    private Token state20() {
        return null;
    }

    private Token state19() {
        return null;
    }

    private Token state18() {
        return null;
    }

    private Token state17() {
        return null;
    }

    private Token state16() {
        return null;
    }

    private Token state15() {
        return null;
    }

    private Token state14() {
        return null;
    }

    private Token state13() {
        return null;
    }

    private Token state12() {
        return null;
    }

    private Token state11() {
        return null;
    }

    private Token state10() {
        return null;
    }

    private Token state9() {
        return null;
    }

    private Token state8() {
        return null;
    }

    private Token state7() {
        return null;
    }

    private Token state6() {
        return null;
    }

    private Token state5() {
        return null;
    }

    private Token state4() {
        return null;
    }

    private Token state3() {
        return null;
    }

    private Token state2() {
        return null;
    }

    private Token state1() {
        if(Character.isDigit(currentChar) || Character.isLetter(currentChar)){
            changeLexeme();
            nextChar();
            return state1();
        }
        changeLexeme();
        //TODO check for table
        return new Token("id",lexeme,fileManager.getLineNumber());
    }
}
