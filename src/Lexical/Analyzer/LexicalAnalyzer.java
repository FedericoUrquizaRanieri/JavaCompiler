package Lexical.Analyzer;

import Lexical.LexExceptions.LexicalException;
import Lexical.SpecialWordMap.SpecialWordsMap;
import SourceManager.SourceManager;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LexicalAnalyzer {
    private final SourceManager fileManager;
    private final SpecialWordsMap wordsMap;
    private char currentChar;
    private String lexeme;

    public LexicalAnalyzer(SourceManager fileManager, SpecialWordsMap wordsMap) {
        this.fileManager = fileManager;
        this.wordsMap = wordsMap;
        nextChar();
    }

    public static String decodeUnicode(String input) {
        Pattern pattern = Pattern.compile("\\\\u([0-9A-Fa-f]{4})");
        Matcher matcher = pattern.matcher(input);
        StringBuilder result = new StringBuilder();

        while (matcher.find()) {
            char ch = (char) Integer.parseInt(matcher.group(1), 16);
            matcher.appendReplacement(result, String.valueOf(ch));
        }
        matcher.appendTail(result);

        return result.toString();
    }

    public Token getNextToken() throws LexicalException {
        lexeme = "";
        return initialState();
    }

    private void changeLexeme() {
        lexeme = lexeme + currentChar;
    }

    private void nextChar() {
        try {
            currentChar = fileManager.getNextChar();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getLine() {
        try {
            return fileManager.getLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public int getLineNumber() {
        return fileManager.getLineNumber();
    }

    private Token initialState() throws LexicalException {
        if (currentChar == SourceManager.END_OF_FILE) {
            return new Token("EOF", "EOF", fileManager.getLineNumber());
        } else if (currentChar == ' ' || currentChar == '\t' || currentChar == '\n' || currentChar == '\r') {
            nextChar();
            return initialState();
        } else if (Character.isDigit(currentChar)) {
            changeLexeme();
            nextChar();
            return integerState();
        } else if (Character.isLetter(currentChar) && Character.isLowerCase(currentChar)) {
            changeLexeme();
            nextChar();
            return variableOrKeywordState();
        } else if (Character.isLetter(currentChar) && Character.isUpperCase(currentChar)) {
            changeLexeme();
            nextChar();
            return classState();
        } else if (currentChar == '\'') {
            changeLexeme();
            nextChar();
            return charState();
        } else if (currentChar == '"') {
            changeLexeme();
            nextChar();
            return stringState();
        } else if (currentChar == '>') {
            changeLexeme();
            nextChar();
            return greaterState();
        } else if (currentChar == '<') {
            changeLexeme();
            nextChar();
            return lessState();
        } else if (currentChar == '=') {
            changeLexeme();
            nextChar();
            return equalsState();
        } else if (currentChar == '!') {
            changeLexeme();
            nextChar();
            return notState();
        } else if (currentChar == '&') {
            changeLexeme();
            nextChar();
            return andState();
        } else if (currentChar == '|') {
            changeLexeme();
            nextChar();
            return orState();
        } else if (currentChar == '+') {
            changeLexeme();
            nextChar();
            return plusState();
        } else if (currentChar == '-') {
            changeLexeme();
            nextChar();
            return minusState();
        } else if (currentChar == '%') {
            changeLexeme();
            nextChar();
            return moduloState();
        } else if (currentChar == '*') {
            changeLexeme();
            nextChar();
            return asteriskState();
        } else if (currentChar == '/') {
            changeLexeme();
            nextChar();
            return slashState();
        } else if (currentChar == '(') {
            changeLexeme();
            nextChar();
            return openParenthesisState();
        } else if (currentChar == ')') {
            changeLexeme();
            nextChar();
            return closeParenthesisState();
        } else if (currentChar == '{') {
            changeLexeme();
            nextChar();
            return openBraceState();
        } else if (currentChar == '}') {
            changeLexeme();
            nextChar();
            return closeBraceState();
        } else if (currentChar == ';') {
            changeLexeme();
            nextChar();
            return semicolonState();
        } else if (currentChar == ',') {
            changeLexeme();
            nextChar();
            return commaState();
        } else if (currentChar == ':') {
            changeLexeme();
            nextChar();
            return colonState();
        } else if (currentChar == '.') {
            changeLexeme();
            nextChar();
            return dotState();
        } else if (currentChar == '?') {
            changeLexeme();
            nextChar();
            return questionMarkState();
        } else {
            changeLexeme();
            nextChar();
            throw new LexicalException(lexeme, fileManager.getLineNumber(), fileManager.getColumnNumber(), "no se encontro un caracter valido para leer");
        }
    }

    private Token questionMarkState() throws LexicalException {
        return new Token("questionMark", lexeme, fileManager.getLineNumber());
    }

    private Token unicodeCharState() throws LexicalException {
        for (int i = 0; i < 4; i++) {
            if (Character.isDigit(currentChar) || (currentChar > 64 && currentChar < 71) || (currentChar > 96 && currentChar < 103)) {
                changeLexeme();
                nextChar();
            } else {
                throw new LexicalException(lexeme, fileManager.getLineNumber(), fileManager.getColumnNumber(), "no es valido en un char unicode");
            }
        }
        return closeSpecialCharState();
    }

    private Token closingMultiLineCommentState() throws LexicalException {
        if (currentChar == '/') {
            lexeme = "";
            nextChar();
            return initialState();
        } else {
            nextChar();
            return multiLineCommentState();
        }
    }

    private Token multiLineCommentState() throws LexicalException {
        if (currentChar == '*') {
            nextChar();
            return closingMultiLineCommentState();
        } else if (currentChar == SourceManager.END_OF_FILE) {
            throw new LexicalException(lexeme, fileManager.getLineNumber(), fileManager.getColumnNumber(), "el comentario nunca termina");
        } else {
            nextChar();
            return multiLineCommentState();
        }
    }

    private Token singleLineCommentState() throws LexicalException {
        if (currentChar == '\n') {
            lexeme = "";
            nextChar();
            return initialState();
        } else if (currentChar == SourceManager.END_OF_FILE) {
            return initialState();
        } else {
            lexeme = "";
            nextChar();
            return singleLineCommentState();
        }
    }

    private Token decrementState() {
        return new Token("decrement", lexeme, fileManager.getLineNumber());
    }

    private Token incrementState() {
        return new Token("increment", lexeme, fileManager.getLineNumber());
    }

    private Token orOperatorState() {
        return new Token("or", lexeme, fileManager.getLineNumber());
    }

    private Token andOperatorState() {
        return new Token("and", lexeme, fileManager.getLineNumber());
    }

    private Token inequalityState() {
        return new Token("inequality", lexeme, fileManager.getLineNumber());
    }

    private Token equalityState() {
        return new Token("equality", lexeme, fileManager.getLineNumber());
    }

    private Token lessOrEqualState() {
        return new Token("lessOrEqual", lexeme, fileManager.getLineNumber());
    }

    private Token greaterOrEqualState() {
        return new Token("greaterOrEqual", lexeme, fileManager.getLineNumber());
    }

    private Token closeStringState() {
        return new Token("stringLiteral", decodeUnicode(lexeme), fileManager.getLineNumber());
    }

    private Token specialCharOnStringState() throws LexicalException {
        if (Character.isDefined(currentChar) && !Character.isISOControl(currentChar)) {
            changeLexeme();
            nextChar();
            return stringState();
        }
        throw new LexicalException(lexeme, fileManager.getLineNumber(), fileManager.getColumnNumber(), "no es un caracter especial valido");
    }

    private Token CharReturnState() {
        return new Token("charLiteral", decodeUnicode(lexeme), fileManager.getLineNumber());
    }

    private Token closeSpecialCharState() throws LexicalException {
        if (currentChar == '\'') {
            changeLexeme();
            nextChar();
            return CharReturnState();
        }
        throw new LexicalException(lexeme, fileManager.getLineNumber(), fileManager.getColumnNumber(), "no es un caracter especial valido");
    }

    private Token specialCharState() throws LexicalException {
        if (currentChar == 'u') {
            changeLexeme();
            nextChar();
            return unicodeCharState();
        } else if (Character.isDefined(currentChar) && !Character.isISOControl(currentChar)) {
            changeLexeme();
            nextChar();
            return closeSpecialCharState();
        }
        changeLexeme();
        nextChar();
        throw new LexicalException(lexeme, fileManager.getLineNumber(), fileManager.getColumnNumber(), "no es un caracter especial valido");
    }

    private Token singleCharState() throws LexicalException {
        if (currentChar == '\'') {
            changeLexeme();
            nextChar();
            return CharReturnState();
        }
        throw new LexicalException(lexeme, fileManager.getLineNumber(), fileManager.getColumnNumber(), "no es un caracter valido");
    }

    private Token dotState() {
        return new Token("dot", lexeme, fileManager.getLineNumber());
    }

    private Token colonState() {
        return new Token("colon", lexeme, fileManager.getLineNumber());
    }

    private Token commaState() {
        return new Token("comma", lexeme, fileManager.getLineNumber());
    }

    private Token semicolonState() {
        return new Token("semicolon", lexeme, fileManager.getLineNumber());
    }

    private Token closeBraceState() {
        return new Token("closeBrace", lexeme, fileManager.getLineNumber());
    }

    private Token openBraceState() {
        return new Token("openBrace", lexeme, fileManager.getLineNumber());
    }

    private Token closeParenthesisState() {
        return new Token("closeParenthesis", lexeme, fileManager.getLineNumber());
    }

    private Token openParenthesisState() {
        return new Token("openParenthesis", lexeme, fileManager.getLineNumber());
    }

    private Token slashState() throws LexicalException {
        if (currentChar == '/') {
            nextChar();
            return singleLineCommentState();
        } else if (currentChar == '*') {
            nextChar();
            return multiLineCommentState();
        }
        return new Token("slash", lexeme, fileManager.getLineNumber());
    }

    private Token asteriskState() {
        return new Token("asterisk", lexeme, fileManager.getLineNumber());
    }

    private Token moduloState() {
        return new Token("modulo", lexeme, fileManager.getLineNumber());
    }

    private Token minusState() {
        if (currentChar == '-') {
            changeLexeme();
            nextChar();
            return decrementState();
        }
        return new Token("minus", lexeme, fileManager.getLineNumber());
    }

    private Token plusState() {
        if (currentChar == '+') {
            changeLexeme();
            nextChar();
            return incrementState();
        }
        return new Token("plus", lexeme, fileManager.getLineNumber());
    }

    private Token orState() throws LexicalException {
        if (currentChar == '|') {
            changeLexeme();
            nextChar();
            return orOperatorState();
        }
        throw new LexicalException(lexeme, fileManager.getLineNumber(), fileManager.getColumnNumber(), "es un operador invalido");
    }

    private Token andState() throws LexicalException {
        if (currentChar == '&') {
            changeLexeme();
            nextChar();
            return andOperatorState();
        }
        throw new LexicalException(lexeme, fileManager.getLineNumber(), fileManager.getColumnNumber(), "es un operador invalido");
    }

    private Token notState() {
        if (currentChar == '=') {
            changeLexeme();
            nextChar();
            return inequalityState();
        }
        return new Token("not", lexeme, fileManager.getLineNumber());
    }

    private Token equalsState() {
        if (currentChar == '=') {
            changeLexeme();
            nextChar();
            return equalityState();
        }
        return new Token("equals", lexeme, fileManager.getLineNumber());
    }

    private Token lessState() {
        if (currentChar == '=') {
            changeLexeme();
            nextChar();
            return lessOrEqualState();
        }
        return new Token("less", lexeme, fileManager.getLineNumber());
    }

    private Token greaterState() {
        if (currentChar == '=') {
            changeLexeme();
            nextChar();
            return greaterOrEqualState();
        }
        return new Token("greater", lexeme, fileManager.getLineNumber());
    }

    private Token stringState() throws LexicalException {
        if (currentChar == '"') {
            changeLexeme();
            nextChar();
            return closeStringState();
        } else if (currentChar == '\\') {
            changeLexeme();
            nextChar();
            return specialCharOnStringState();
        } else if (Character.isDefined(currentChar) && !Character.isISOControl(currentChar)) {
            changeLexeme();
            nextChar();
            return stringState();
        }
        throw new LexicalException(lexeme, fileManager.getLineNumber(), fileManager.getColumnNumber(), "es un string invalido");
    }

    private Token charState() throws LexicalException {
        if (currentChar == SourceManager.END_OF_FILE || currentChar == '\n' || currentChar == '\r') {
            throw new LexicalException(lexeme, fileManager.getLineNumber(), fileManager.getColumnNumber(), "no es un caracter valido, llega al final");
        } else if (currentChar != '\\' && currentChar != '\'') {
            changeLexeme();
            nextChar();
            return singleCharState();
        } else if (currentChar == '\\') {
            changeLexeme();
            nextChar();
            return specialCharState();
        }
        throw new LexicalException(lexeme, fileManager.getLineNumber(), fileManager.getColumnNumber(), "no es un caracter valido");
    }

    private Token integerState() throws LexicalException {
        if (Character.isDigit(currentChar)) {
            changeLexeme();
            nextChar();
            return integerState();
        } else if (lexeme.length() < 10) {
            return new Token("intLiteral", lexeme, fileManager.getLineNumber());
        } else {
            throw new LexicalException(lexeme, fileManager.getLineNumber(), fileManager.getColumnNumber(), "digito de mas, numero demasiado grande");
        }
    }

    private Token variableOrKeywordState() {
        if (Character.isDigit(currentChar) || Character.isLetter(currentChar) || currentChar == '_') {
            changeLexeme();
            nextChar();
            return variableOrKeywordState();
        } else {
            return new Token(wordsMap.getOrDefault(lexeme, "idMetVar"), lexeme, fileManager.getLineNumber());
        }
    }

    private Token classState() {
        if (Character.isDigit(currentChar) || Character.isLetter(currentChar) || currentChar == '_') {
            changeLexeme();
            nextChar();
            return classState();
        } else {
            return new Token("idClase", lexeme, fileManager.getLineNumber());
        }
    }
}