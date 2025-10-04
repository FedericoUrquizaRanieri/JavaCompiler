package Semantic;

import java.util.HashSet;

public class Class {
    private String name;
    private String inheritance;
    private HashSet<Attribute> attributes;
    private HashSet<Method> methods;
    private HashSet<Constructor> constructors;

    public Class(String name, String inheritance){
        attributes = new HashSet<>();
        methods = new HashSet<>();
        constructors = new HashSet<>();
        this.name = name;
        this.inheritance = inheritance;
    }

    public String getName() {
        return name;
    }

    public HashSet<Attribute> getAttributes() {
        return attributes;
    }

    public HashSet<Method> getMethods() {
        return methods;
    }

    public String getInheritance() {
        return inheritance;
    }

    public HashSet<Constructor> getConstructors() {
        return constructors;
    }
}