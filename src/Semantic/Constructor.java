package Semantic;

import java.util.HashSet;

public class Constructor {
    private HashSet<Parameter> parameters;

    public Constructor(){
        parameters = new HashSet<>();
    }

    public HashSet<Parameter> getParameters() {
        return parameters;
    }
}
