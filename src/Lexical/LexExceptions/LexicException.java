package Lexical.LexExceptions;

public class LexicException extends Exception{
    private String lexeme;
    private int line;

    public LexicException(String lexeme,int line){
        this.lexeme = lexeme;
        this.line = line;
    }

    public void printError(String entireLine) {
        System.out.println("Error Léxico en linea"+line+": "+lexeme+" no es un símbolo valido");
        System.out.println("Detalle: "+entireLine);
        for(int i=0;i<=9;i++)
            System.out.print(" ");
        System.out.println("^");
        System.out.println();
        System.out.println("[Error:"+lexeme+"|"+line+"]");
    }
}
