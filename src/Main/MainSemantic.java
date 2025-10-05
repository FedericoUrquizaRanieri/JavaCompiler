package Main;

import Lexical.Analyzer.LexicalAnalyzer;
import Lexical.SpecialWordMap.SpecialWordsMap;
import Semantic.SemExceptions.SemanticException;
import Semantic.SymbolTable;
import SourceManager.*;

import java.io.FileNotFoundException;
import java.io.IOException;

import Syntactic.Analyzer.ProductionsMap;
import Syntactic.Analyzer.SyntacticAnalyzer;
import Syntactic.SynExceptions.SyntacticException;

public class MainSemantic {
    public static SymbolTable symbolTable;

    public static void main(String[] args) {
        SourceManager sourceManager = new SourceManagerImpl();
        try {
            sourceManager.open(args[0]);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        SpecialWordsMap specialWordsMap = new SpecialWordsMap();
        symbolTable = new SymbolTable();
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
            try {
                symbolTable.checkStatements();
            } catch (SemanticException e) {
                e.printError();
                noMistakes=false;
            }
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