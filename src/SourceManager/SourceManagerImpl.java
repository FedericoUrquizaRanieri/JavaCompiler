package SourceManager;
//Author: Juan Dingevan

import java.io.*;
import java.nio.charset.StandardCharsets;

public class SourceManagerImpl implements SourceManager{
    private BufferedReader reader;
    private int lineNumber;
    private int lineIndexNumber;


    public SourceManagerImpl() {
        lineNumber = 0;
        lineIndexNumber = 0;
    }

    @Override
    public void open(String filePath) throws FileNotFoundException {
        FileInputStream fileInputStream = new FileInputStream(filePath);
        InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8);

        reader = new BufferedReader(inputStreamReader);
    }

    @Override
    public void close() throws IOException {
        reader.close();
    }

    @Override
    public char getNextChar() throws IOException {
        int currentInt = reader.read();
        if (currentInt==-1)
            return END_OF_FILE;
        char currentChar = (char) currentInt;
        if(currentChar=='\n'){
            lineIndexNumber=0;
            lineNumber++;
        } else {
            lineIndexNumber++;
        }
        return currentChar;
    }

    @Override
    public int getLineNumber() {
        return lineNumber;
    }

    @Override
    public int getColumnNumber() {
        return lineIndexNumber;
    }

}
