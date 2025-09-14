package Syntactic.SynExceptions;

public class SyntacticException extends Exception {
  private final String currentToken;
  private final String tokenName;

  public SyntacticException(String currentToken, String tokenName) {
    this.currentToken = currentToken;
    this.tokenName = tokenName;
  }

  public void printError() {
    System.out.println("Se esperaba un "+tokenName+" pero se encontro "+currentToken);
    System.out.println();
  }
}
