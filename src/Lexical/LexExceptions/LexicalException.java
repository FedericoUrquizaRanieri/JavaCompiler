package Lexical.LexExceptions;

import Main.CompiException;

public class LexicalException extends CompiException {
    private final String lexeme;
    private final int line;
    private final int column;
    private final String errorExplanation;

    public LexicalException(String lexeme, int line, int col, String errorExplanation) {
        this.lexeme = lexeme;
        this.line = line;
        this.column = col;
        this.errorExplanation = errorExplanation;
    }

    public void printError(String entireLine) {
        int errorControlNumber = 0;
        if (column!=0){
            errorControlNumber=1;
        }
        System.out.println("Error Léxico en linea " + line + " y columna " + (column - errorControlNumber) + ": " + lexeme + " " + errorExplanation);
        System.out.println("Detalle: " + entireLine);
        for (int i = 0; i < column - errorControlNumber + 8; i++)
            System.out.print(" ");
        System.out.println("^");
        System.out.println();
        System.out.println("[Error:" + lexeme + "|" + line + "]");
    }
}
