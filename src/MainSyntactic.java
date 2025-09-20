import Lexical.Analyzer.LexicalAnalyzer;
import Lexical.SpecialWordMap.SpecialWordsMap;
import SourceManager.*;

import java.io.FileNotFoundException;
import java.io.IOException;

import Syntactic.Analyzer.ProductionsMap;
import Syntactic.Analyzer.SyntacticAnalyzer;
import Syntactic.SynExceptions.SyntacticException;

public class MainSyntactic {

    public static void main(String[] args) {
        SourceManager sourceManager = new SourceManagerImpl();
        try {
            sourceManager.open(args[0]);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        SpecialWordsMap specialWordsMap = new SpecialWordsMap();
        ProductionsMap productionsMap = new ProductionsMap();
        LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer(sourceManager, specialWordsMap);
        SyntacticAnalyzer syntacticAnalyzer = new SyntacticAnalyzer(lexicalAnalyzer, productionsMap);

        boolean noMistakes = true;
        try {
            syntacticAnalyzer.startAnalysis();
        } catch (SyntacticException e) {
            e.printError();
            noMistakes=false;
        }

        if (noMistakes) {
            System.out.println("Compilacion Exitosa");
            System.out.println();
            System.out.println("[SinErrores]");
        }

        try {
            sourceManager.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}