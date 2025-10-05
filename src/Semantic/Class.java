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

    public void addConstructor(Constructor c) throws SemanticException {
        if(constructors.putIfAbsent(c.token.getLexeme(),c)!=null)
            throw new SemanticException(c.token.getLexeme(),"Se intento agregar un atributo repetido llamada ",c.token.getLine());
    }

    public void addMethod(Method m) throws SemanticException {
        if(methods.putIfAbsent(m.name,m)!=null)
            throw new SemanticException(m.name,"Se intento agregar un atributo repetido llamada ",m.token.getLine());
    }

    public void addAttribute(Attribute a) throws SemanticException {
        if(attributes.putIfAbsent(a.name,a)!=null)
            throw new SemanticException(a.name,"Se intento agregar un atributo repetido llamada ",a.token.getLine());
    }
}