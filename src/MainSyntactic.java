import Lexical.Analyzer.LexicalAnalyzer;
import Lexical.SpecialWordMap.SpecialWordsMap;
import SourceManager.*;

import java.io.FileNotFoundException;
import java.io.IOException;

import Syntactic.Analyzer.FirstsMap;
import Syntactic.Analyzer.SyntacticAnalyzer;

public class MainSyntactic {

    public static void main(String[] args) {
        SourceManager sourceManager = new SourceManagerImpl();
        try {
            sourceManager.open(args[0]);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        SpecialWordsMap specialWordsMap = new SpecialWordsMap();
        FirstsMap firstsMap = new FirstsMap();
        LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer(sourceManager, specialWordsMap);
        SyntacticAnalyzer syntacticAnalyzer = new SyntacticAnalyzer(lexicalAnalyzer, firstsMap);

        boolean noMistakes = true;
        syntacticAnalyzer.startAnalysis();
        noMistakes = syntacticAnalyzer.notErrorInFile();

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