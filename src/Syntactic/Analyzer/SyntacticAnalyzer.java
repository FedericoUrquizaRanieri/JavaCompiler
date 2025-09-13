package Syntactic.Analyzer;

import Lexical.Analyzer.LexicalAnalyzer;
import Lexical.Analyzer.Token;
import Lexical.LexExceptions.LexicalException;

public class SyntacticAnalyzer {
    private final LexicalAnalyzer analyzer;
    private Token currentToken;
    private boolean errors;
    private boolean panicState;

    public SyntacticAnalyzer(LexicalAnalyzer analyzer) {
        this.analyzer = analyzer;
    }

    public boolean notErrorInFile() {
        return errors;
    }

    void match(String tokenName) {
        if (tokenName.equals(currentToken.getTokenName())){
            try {
                currentToken = analyzer.getNextToken();
            } catch (LexicalException e) {
                e.printError(analyzer.getLine());
            }
        }
    }

    public void startAnalysis(){
        try {
            currentToken = analyzer.getNextToken();
        } catch (LexicalException e) {
            e.printError(analyzer.getLine());
        }
        start();
    }
    
    private void start(){
        classesList();
        match("EOF");
    }

    private void classesList() {
        switch (currentToken.getTokenName()) {
            case "pr_class", "pr_abstract", "pr_static", "pr_final", "pr_interface" -> {
                classState();
                classesList();
            }
        }
    }

    private void classState() {
        switch (currentToken.getTokenName()) {
            case "pr_abstract", "pr_static", "pr_final" -> {
                optionalModifier();
                classState();
                break;
            }
            case "pr_class" -> {
                match("pr_class");
                match("idClase");
                optionalInheritance();
                match("openBrace");
                membersList();
                match("closeBrace");
                break;
            }
            case "pr_interfce" -> {
                match("pr_interface");
                match("idClase");
                optionalInheritance();
                match("openBrace");
                membersList();
                match("closeBrace");
                break;
            }
        }
    }

    private void membersList() {
    }

    private void optionalInheritance() {
    }


    private void optionalModifier() {
        switch (currentToken.getTokenName()){
            case "pr_abstract" -> {
                match("pr_abstract");
                break;
            }
            case "pr_static" -> {
                match("pr_static");
                break;
            }
            case "pr_final" -> {
                match("pr_final");
                break;
            }
        }
    }
}
