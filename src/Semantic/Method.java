package Semantic;

import java.util.HashSet;

public class Method {
    Type returnType;
    private HashSet<Parameter> parameters;

    public Method(){
        parameters = new HashSet<>();
    }
}
