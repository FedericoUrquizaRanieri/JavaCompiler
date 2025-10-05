package Main;

import Lexical.LexExceptions.LexicalException;
import Lexical.Analyzer.LexicalAnalyzer;
import Lexical.Analyzer.Token;
import Lexical.SpecialWordMap.SpecialWordsMap;
import SourceManager.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Objects;

public class MainLexical {

    public static void printCorrect(Token token) {
        System.out.println("(" + token.getTokenName() + "," + token.getLexeme() + "," + token.getLine() + ")");
    }

    public static void main(String[] args) {
        Token currentToken = new Token(" ", " ", 0);
        SourceManager sourceManager = new SourceManagerImpl();
        try {
            sourceManager.open(args[0]);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        SpecialWordsMap specialWordsMap = new SpecialWordsMap();
        LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer(sourceManager, specialWordsMap);
        boolean noMistakes = true;

        do {
            try {
                currentToken = lexicalAnalyzer.getNextToken();
                printCorrect(currentToken);
            } catch (LexicalException e) {
                e.printError(lexicalAnalyzer.getLine());
                noMistakes = false;
            }
        } while (!Objects.equals(currentToken.getTokenName(), "EOF"));
        if (noMistakes) {
            System.out.println("[SinErrores]");
        }

        try {
            sourceManager.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}