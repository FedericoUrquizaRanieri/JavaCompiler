package Lexical.LexExceptions;

public class LexicalException extends Exception {
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
        System.out.println("Error Léxico en linea " + line + " y columna " + (column - 1) + ": " + lexeme + " " + errorExplanation);
        System.out.println("Detalle: " + entireLine);
        for (int i = 0; i < column - 1 + 8; i++)
            System.out.print(" ");
        System.out.println("^");
        System.out.println();
        System.out.println("[Error:" + lexeme + "|" + line + "]");
    }
}
