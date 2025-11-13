package Main;

import Lexical.Analyzer.LexicalAnalyzer;
import Lexical.SpecialWordMap.SpecialWordsMap;
import Semantic.SemExceptions.SemanticException;
import Semantic.ST.SymbolTable;
import SourceManager.*;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import Syntactic.Analyzer.ProductionsMap;
import Syntactic.Analyzer.SyntacticAnalyzer;

public class MainGen {
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
        } catch (CompiException e) {
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
            try {
                symbolTable.consolidate();
            } catch (SemanticException e) {
                e.printError();
                noMistakes=false;
            }
        }

        if (noMistakes) {
            try {
                symbolTable.checkSentences();
            } catch (SemanticException e) {
                e.printError();
                noMistakes=false;
            }
        }

        if (noMistakes){
            symbolTable.setOffsets();
            symbolTable.generateCode();

            String nombreSolo = args[0].substring(Math.max(args[0].lastIndexOf('/'), args[0].lastIndexOf('\\')) + 1);
            String nombreOut = "[" + nombreSolo + "].out";
            try (PrintWriter writer = new PrintWriter(new FileWriter(nombreOut))) {
                for (String linea : symbolTable.instructionsList) {
                    writer.println(linea);
                }
            } catch (IOException e) {
                System.err.println("Error al escribir el archivo de salida: " + e.getMessage());
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