package Semantic;

import java.util.HashSet;

public class Method {
    String name;
    Type returnType;
    private HashSet<Parameter> parameters;

    public Method(String name, Type returnType){
        parameters = new HashSet<>();
        this.name = name;
        this.returnType = returnType;
    }

    public String getName() {
        return name;
    }

    public HashSet<Parameter> getParameters() {
        return parameters;
    }

    public Type getReturnType() {
        return returnType;
    }
}
