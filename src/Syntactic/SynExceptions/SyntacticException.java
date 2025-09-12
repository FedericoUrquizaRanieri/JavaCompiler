package Syntactic.SynExceptions;

public class SyntacticException extends Exception {
  private final String lexeme;
  private final int line;
  private final String errorExplanation;

  public SyntacticException(String lexeme, int line, String errorExplanation) {
    this.lexeme = lexeme;
    this.line = line;
    this.errorExplanation = errorExplanation;
  }

  public void printError() {
    System.out.println("Error Sintactico en linea " + line + ": " + errorExplanation + " \"" + lexeme + "\"");
    System.out.println();
    System.out.println("[Error:" + lexeme + "|" + line + "]");
  }
}
