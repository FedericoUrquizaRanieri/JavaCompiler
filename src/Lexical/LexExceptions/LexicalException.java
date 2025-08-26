package Lexical.LexExceptions;

public class LexicalException extends Exception {
    private final String lexeme;
    private final int line;
    private final int column;

    public LexicalException(String lexeme, int line, int col) {
        this.lexeme = lexeme;
        this.line = line;
        this.column = col;
    }

    public void printError(String entireLine) {
        System.out.println("Error Léxico en linea" + line + ": " + lexeme + " no es un símbolo valido");
        System.out.println("Detalle: " + entireLine);
        for (int i = 0; i < column - 1 + 8; i++)
            System.out.print(" ");
        System.out.println("^");
        System.out.println();
        System.out.println("[Error:" + lexeme + "|" + line + "|" + (column - 1) + "]");
    }
}
