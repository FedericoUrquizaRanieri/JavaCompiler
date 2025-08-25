import Lexical.LexExceptions.LexicException;
import Lexical.LexicalAnalyzer;
import Lexical.Token;
import SourceManager.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Objects;

public class Main {

    public static void printCorrect(Token token){
        System.out.println("("+token.getTokenName()+","+token.getLexeme()+","+token.getLine()+")");
    }

    public static void main(String[] args) {
        Token currentToken = new Token("example","example",0); //revisit this later
        SourceManager sourceManager = new SourceManagerImpl();
        try {
            sourceManager.open(args[0]);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer(sourceManager);
        boolean noMistakes=true;

        do{
            try {
                currentToken = lexicalAnalyzer.getNextToken();
                printCorrect(currentToken);
            } catch (LexicException e) {
                e.printStackTrace();
                noMistakes=false;
            }
        }while(!Objects.equals(currentToken.getTokenName(), "EOF"));
        if(noMistakes){
            System.out.println("[SinErrores]");
        }
        try {
            sourceManager.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}