package Semantic.SemExceptions;

public class SemanticException extends Exception {
  private final String currentElement;
  private final String explanation;
  private final int line;

  public SemanticException(String currentElement, String explanation, int line) {
    this.currentElement = currentElement;
    this.explanation = explanation;
    this.line = line;
  }

  public void printError() {
    System.out.println("Error Semantico en linea "+line+":");
    System.out.println(explanation);
    System.out.println();
    System.out.println("[Error:" + currentElement + "|" + line + "]");
  }
}
