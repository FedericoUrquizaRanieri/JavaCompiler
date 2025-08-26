package Lexical.SpecialWordMap;

import java.util.HashMap;

public class SpecialWordsMap{
    private final HashMap<String,String> wordMap;

    public SpecialWordsMap(){
        wordMap = new HashMap<>();
        wordMap.put("class","pr_class");
        wordMap.put("extends","pr_extends");
        wordMap.put("public","pr_public");
        wordMap.put("static","pr_static");
        wordMap.put("void","pr_void");
        wordMap.put("boolean","pr_boolean");
        wordMap.put("char","pr_char");
        wordMap.put("int","pr_int");
        wordMap.put("abstract","pr_abstract");
        wordMap.put("final","pr_final");
        wordMap.put("if","pr_if");
        wordMap.put("else","pr_else");
        wordMap.put("while","pr_while");
        wordMap.put("return","pr_return");
        wordMap.put("var","pr_var");
        wordMap.put("this","pr_this");
        wordMap.put("new","pr_new");
        wordMap.put("null","pr_null");
        wordMap.put("true","pr_true");
        wordMap.put("false","pr_false");
    }

    public String getOrDefault(String key,String def){
        return wordMap.getOrDefault(key,def);
    }
}
