package Syntactic.SynExceptions;

public class SyntacticException extends Exception {
  private final String currentToken;
  private final String expectedTokenName;
  private final int line;

  public SyntacticException(String currentToken, String expectedTokenName, int line) {
    this.currentToken = currentToken;
    this.expectedTokenName = expectedTokenName;
    this.line = line;
  }

  public void printError() {
    System.out.println("Error Sintactico:");
    System.out.println("Se esperaban los siguientes tokens: "+expectedTokenName);
    System.out.println("Se encontro: "+currentToken);
    System.out.println();
    System.out.println("[Error:" + currentToken + "|" + line + "]");
  }
}
