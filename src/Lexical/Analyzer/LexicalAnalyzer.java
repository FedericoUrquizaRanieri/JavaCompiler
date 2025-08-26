package Lexical.Analyzer;

import Lexical.LexExceptions.LexicException;
import Lexical.SpecialWordMap.SpecialWordsMap;
import SourceManager.SourceManager;

import java.io.IOException;

public class LexicalAnalyzer{
    private final SourceManager fileManager;
    private final SpecialWordsMap wordsMap;
    private char currentChar;
    private String lexeme;

    public LexicalAnalyzer(SourceManager fileManager, SpecialWordsMap wordsMap){
        this.fileManager=fileManager;
        this.wordsMap=wordsMap;
        nextChar();
    }

    public Token getNextToken() throws LexicException {
        lexeme = "";
        return state0();
    }

    private void changeLexeme(){
        lexeme = lexeme + currentChar;
        //System.out.println("Lexema:"+lexeme);
    }

    private void nextChar(){
        try {
            currentChar = fileManager.getNextChar();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //System.out.println("Caracter:"+currentChar);
    }

    public String getLine(){
        try {
            return fileManager.getLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Token state0() throws LexicException {
        if (currentChar==SourceManager.END_OF_FILE){
            return new Token("EOF","EOF",fileManager.getLineNumber());
        } else if (currentChar == ' ' || currentChar == '\t' || currentChar == '\n' || currentChar == '\r'){
            nextChar();
            return state0();
        } else if (Character.isLetter(currentChar) && Character.isUpperCase(currentChar)){
            changeLexeme();
            nextChar();
            return state1();
        } else if (Character.isLetter(currentChar) && Character.isLowerCase(currentChar)){
            changeLexeme();
            nextChar();
            return state1_1();
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
        } else {
            changeLexeme();
            nextChar();
            throw new LexicException(lexeme,fileManager.getLineNumber());
        }
    }

    private Token state40() throws LexicException {
        if (currentChar=='/') {
            lexeme = "";
            nextChar();
            return state0();
        } else {
            nextChar();
            return state39();
        }
    }

    private Token state39() throws LexicException {
        if (currentChar=='*') {
            nextChar();
            return state40();
        } else {
            nextChar();
            return state39();
        }
    }

    private Token state38() throws LexicException {
        if (currentChar=='\n') {
            lexeme = "";
            nextChar();
            return state0();
        } else {
            nextChar();
            return state38();
        }
    }

    private Token state37() {
        return new Token("decrement",lexeme,fileManager.getLineNumber());
    }

    private Token state36() {
        return new Token("increment",lexeme,fileManager.getLineNumber());
    }

    private Token state35() {
        return new Token("or",lexeme,fileManager.getLineNumber());
    }

    private Token state34() {
        return new Token("and",lexeme,fileManager.getLineNumber());
    }

    private Token state33() {
        return new Token("inequality",lexeme, fileManager.getLineNumber());
    }

    private Token state32() {
        return new Token("equality",lexeme, fileManager.getLineNumber());
    }

    private Token state31() {
        return new Token("lessOrEqual",lexeme, fileManager.getLineNumber());
    }

    private Token state30() {
        return new Token("greaterOrEqual",lexeme, fileManager.getLineNumber());
    }

    private Token state29() {
        return new Token("stringLiteral",lexeme,fileManager.getLineNumber());
    }

    private Token state28() throws LexicException {
        if (Character.isLetterOrDigit(currentChar) || currentChar>=33 && currentChar<=126) {
            changeLexeme();
            nextChar();
            return state4();
        }
        throw new LexicException(lexeme,fileManager.getLineNumber());
    }

    private Token state27() {
        return new Token("charLiteral",lexeme,fileManager.getLineNumber());
    }

    private Token state26() throws LexicException {
        if(currentChar=='\''){
            changeLexeme();
            nextChar();
            return state27();
        }
        throw new LexicException(lexeme,fileManager.getLineNumber());
    }

    private Token state25() throws LexicException {
        if (Character.isLetterOrDigit(currentChar) || currentChar>=33 && currentChar<=126) {
            changeLexeme();
            nextChar();
            return state26();
        }
        throw new LexicException(lexeme,fileManager.getLineNumber());
    }

    private Token state24() throws LexicException {
        if (currentChar=='\'') {
            changeLexeme();
            nextChar();
            return state27();
        }
        throw new LexicException(lexeme,fileManager.getLineNumber());
    }

    private Token state23() {
        return new Token("dot",lexeme, fileManager.getLineNumber());
    }

    private Token state22() {
        return new Token("colon",lexeme, fileManager.getLineNumber());
    }

    private Token state21() {
        return new Token("comma",lexeme, fileManager.getLineNumber());
    }

    private Token state20() {
        return new Token("semicolon",lexeme, fileManager.getLineNumber());
    }

    private Token state19() {
        return new Token("closeBrace",lexeme, fileManager.getLineNumber());
    }

    private Token state18() {
        return new Token("openBrace",lexeme, fileManager.getLineNumber());
    }

    private Token state17() {
        return new Token("closeParenthesis",lexeme, fileManager.getLineNumber());
    }

    private Token state16() {
        return new Token("openParenthesis",lexeme, fileManager.getLineNumber());
    }

    private Token state15() throws LexicException {
        if (currentChar=='/'){
            nextChar();
            return state38();
        } else if (currentChar=='*') {
            nextChar();
            return state39();
        }
        return new Token("slash",lexeme, fileManager.getLineNumber());
    }

    private Token state14() {
        return new Token("asterisk",lexeme, fileManager.getLineNumber());
    }

    private Token state13() {
        return new Token("modulo",lexeme, fileManager.getLineNumber());
    }

    private Token state12() {
        if (currentChar=='-') {
            changeLexeme();
            nextChar();
            return state37();
        }
        return new Token("minus",lexeme,fileManager.getLineNumber());
    }

    private Token state11() {
        if (currentChar=='+') {
            changeLexeme();
            nextChar();
            return state36();
        }
        return new Token("plus",lexeme,fileManager.getLineNumber());
    }

    private Token state10() throws LexicException {
        if (currentChar=='|') {
            changeLexeme();
            nextChar();
            return state35();
        }
        throw new LexicException(lexeme, fileManager.getLineNumber());
    }

    private Token state9() throws LexicException {
        if (currentChar=='&') {
            changeLexeme();
            nextChar();
            return state34();
        }
        throw new LexicException(lexeme, fileManager.getLineNumber());
    }

    private Token state8() {
        if (currentChar=='=') {
            changeLexeme();
            nextChar();
            return state33();
        }
        return new Token("not",lexeme,fileManager.getLineNumber());
    }

    private Token state7() {
        if (currentChar=='=') {
            changeLexeme();
            nextChar();
            return state32();
        }
        return new Token("equals",lexeme,fileManager.getLineNumber());
    }

    private Token state6() {
        if (currentChar=='=') {
            changeLexeme();
            nextChar();
            return state31();
        }
        return new Token("less",lexeme,fileManager.getLineNumber());
    }

    private Token state5() {
        if (currentChar=='=') {
            changeLexeme();
            nextChar();
            return state30();
        }
        return new Token("greater",lexeme,fileManager.getLineNumber());
    }

    private Token state4() throws LexicException {
        if (currentChar=='"') {
            changeLexeme();
            nextChar();
            return state29();
        }else if(currentChar=='\\'){
            changeLexeme();
            nextChar();
            return state28();
        } else if (Character.isLetterOrDigit(currentChar)){
            changeLexeme();
            nextChar();
            return state4();
        }
        throw new LexicException(lexeme,fileManager.getLineNumber());
    }

    private Token state3() throws LexicException {
        if(currentChar!='\\' && currentChar!='\''){
            changeLexeme();
            nextChar();
            return state24();
        } else if (currentChar=='\\') {
            changeLexeme();
            nextChar();
            return state25();
        }
        throw new LexicException(lexeme,fileManager.getLineNumber());
    }

    private Token state2() throws LexicException {
        if(Character.isDigit(currentChar)){
            changeLexeme();
            nextChar();
            return state1();
        } else if(lexeme.length()<10){
            return new Token("intLiteral",lexeme,fileManager.getLineNumber());
        }else {
            throw new LexicException(lexeme,fileManager.getLineNumber());
        }
    }

    private Token state1_1() {
        if(Character.isDigit(currentChar) || Character.isLetter(currentChar)){
            changeLexeme();
            nextChar();
            return state1_1();
        } else {
            return new Token(wordsMap.getOrDefault(lexeme,"idMetVar"), lexeme,fileManager.getLineNumber());
        }
    }

    private Token state1() {
        if(Character.isDigit(currentChar) || Character.isLetter(currentChar)){
            changeLexeme();
            nextChar();
            return state1();
        } else {
            return new Token("idClase",lexeme,fileManager.getLineNumber());
        }
    }
}
