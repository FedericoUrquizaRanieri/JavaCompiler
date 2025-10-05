package Semantic;

import Lexical.Analyzer.Token;
import Semantic.SemExceptions.SemanticException;

import java.util.HashMap;

public class Class {
    public String className;
    public Token classToken;
    public Token inheritance;
    public Token generics;
    public Token inheritanceGenerics;
    public Token modifierClass;
    public HashMap<String, Attribute> attributes;
    public HashMap<String, Method> methods;
    public HashMap<String, Constructor> constructors;

    public Class(Token token){
        attributes = new HashMap<>();
        methods = new HashMap<>();
        constructors = new HashMap<>();
        this.className = token.getLexeme();
        this.classToken = token;
    }

    public void checkStatements() throws SemanticException {
        throw new SemanticException("mock","mock",1);
    }

    public void consolidate() throws SemanticException{
        throw new SemanticException("mock","mock",1);
    }

    public void addConstructor(Constructor c){
        constructors.put(c.token.getLexeme(),c);
    }

    public void addMethod(Method m){
        methods.put(m.name,m);
    }

    public void addAttribute(Attribute a){
        attributes.put(a.name,a);
    }
}