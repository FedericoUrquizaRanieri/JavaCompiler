import Lexical.LexExceptions.LexicException;
import Lexical.LexicalAnalyzer;
import Lexical.Token;
import SourceManager.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

public class Main {

    public static void printCorrect(Token token){
        System.out.println("("+token.getTokenName()+","+token.getLexeme()+","+token.getLine()+")");
    }

    public static void main(String[] args) {
        Token currentToken = new Token("EOF","EOF",0); //revisit this later
        SourceManager sourceManager = new SourceManagerImpl();
        LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer(sourceManager);

        try {
            sourceManager.open(Arrays.toString(args));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        do{
            try {
                currentToken = lexicalAnalyzer.getNextToken();
                printCorrect(currentToken);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (LexicException e) {
                e.printStackTrace();
            }
        }while(Objects.equals(currentToken.getTokenName(), "EOF"));

    }
}