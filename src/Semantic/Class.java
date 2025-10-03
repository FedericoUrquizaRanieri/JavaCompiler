package Semantic;

import java.util.HashSet;

public class Class {
    private HashSet<Attribute> attributes;
    private HashSet<Method> methods;

    public Class(){
        attributes = new HashSet<>();
        methods = new HashSet<>();
    }
}
