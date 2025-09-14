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
    System.out.println("Se esperaba un "+expectedTokenName+" pero se encontro "+currentToken);
    System.out.println();
    System.out.println("[Error:" + currentToken + "|" + line + "]");
  }
}
