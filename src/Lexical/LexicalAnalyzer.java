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
        nextChar();
    }

    public Token getNextToken() throws LexicException {
        lexeme = "";
        return state0();
    }

    private void changeLexeme(){
        lexeme = lexeme + currentChar;
        System.out.println("Lexema:"+lexeme);
    }

    private void nextChar(){
        try {
            currentChar = fileManager.getNextChar();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println(currentChar);
    }

    private Token state0() throws LexicException {
        if (currentChar==SourceManager.END_OF_FILE){
            return new Token("EOF","EOF",fileManager.getLineNumber());
        } else if (currentChar == ' ' || currentChar == '\t' || currentChar == '\n' || currentChar == '\r'){
            nextChar();
            return state0();
        } else if (Character.isLetter(currentChar)){
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
        } else {
            throw new LexicException("no encontre nada",fileManager.getLineNumber());
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
        return new Token("idRestaVar",lexeme,fileManager.getLineNumber());
    }

    private Token state36() {
        return new Token("idSumaVar",lexeme,fileManager.getLineNumber());
    }

    private Token state35() {
        return new Token("idOr",lexeme,fileManager.getLineNumber());
    }

    private Token state34() {
        return new Token("idAnd",lexeme,fileManager.getLineNumber());
    }

    private Token state33() {
        return new Token("idDistinto",lexeme, fileManager.getLineNumber());
    }

    private Token state32() {
        return new Token("idDobleIguall",lexeme, fileManager.getLineNumber());
    }

    private Token state31() {
        return new Token("idMenorIgual",lexeme, fileManager.getLineNumber());
    }

    private Token state30() {
        return new Token("idMayorIgual",lexeme, fileManager.getLineNumber());
    }

    private Token state29() {
        return new Token("idString",lexeme,fileManager.getLineNumber());
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
        return new Token("idChar",lexeme,fileManager.getLineNumber());
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
        return new Token("idPunto",lexeme, fileManager.getLineNumber());
    }

    private Token state22() {
        return new Token("idDosPuntos",lexeme, fileManager.getLineNumber());
    }

    private Token state21() {
        return new Token("idComa",lexeme, fileManager.getLineNumber());
    }

    private Token state20() {
        return new Token("idPuntoYComa",lexeme, fileManager.getLineNumber());
    }

    private Token state19() {
        return new Token("idCorcheteCierra",lexeme, fileManager.getLineNumber());
    }

    private Token state18() {
        return new Token("idCorcheteAbre",lexeme, fileManager.getLineNumber());
    }

    private Token state17() {
        return new Token("idParentesisCierra",lexeme, fileManager.getLineNumber());
    }

    private Token state16() {
        return new Token("idParentesisAbre",lexeme, fileManager.getLineNumber());
    }

    private Token state15() throws LexicException {
        if (currentChar=='/'){
            nextChar();
            return state38();
        } else if (currentChar=='*') {
            nextChar();
            return state39();
        }
        return new Token("idBarra",lexeme, fileManager.getLineNumber());
    }

    private Token state14() {
        return new Token("idMultiplicacion",lexeme, fileManager.getLineNumber());
    }

    private Token state13() {
        return new Token("idModulo",lexeme, fileManager.getLineNumber());
    }

    private Token state12() {
        if (currentChar=='-') {
            changeLexeme();
            nextChar();
            return state37();
        }
        return new Token("idSuma",lexeme,fileManager.getLineNumber());
    }

    private Token state11() {
        if (currentChar=='+') {
            changeLexeme();
            nextChar();
            return state36();
        }
        return new Token("idSuma",lexeme,fileManager.getLineNumber());
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
        return new Token("idNegacion",lexeme,fileManager.getLineNumber());
    }

    private Token state7() {
        if (currentChar=='=') {
            changeLexeme();
            nextChar();
            return state32();
        }
        return new Token("idIgual",lexeme,fileManager.getLineNumber());
    }

    private Token state6() {
        if (currentChar=='=') {
            changeLexeme();
            nextChar();
            return state31();
        }
        return new Token("idMenor",lexeme,fileManager.getLineNumber());
    }

    private Token state5() {
        if (currentChar=='=') {
            changeLexeme();
            nextChar();
            return state30();
        }
        return new Token("idMayor",lexeme,fileManager.getLineNumber());
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

    private Token state2() {
        if(Character.isDigit(currentChar)){
            changeLexeme();
            nextChar();
            return state1();
        } else {
            return new Token("id",lexeme,fileManager.getLineNumber());
        }
    }

    private Token state1() {
        if(Character.isDigit(currentChar) || Character.isLetter(currentChar)){
            changeLexeme();
            nextChar();
            return state1();
        } else {
            return new Token("id",lexeme,fileManager.getLineNumber());
        }
    }
}
