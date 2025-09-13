package Syntactic.Analyzer;

import java.util.HashMap;
import java.util.Set;

public class FirstsMap {
    private final HashMap<String, Set<String>> firstHashMap;

    public FirstsMap() {
        firstHashMap = new HashMap<>();
        firstHashMap.put("<start>", Set.of("EOF","pr_abstract","pr_static","pr_final","pr_class"));
        firstHashMap.put("<classesList>", Set.of("pr_abstract","pr_static","pr_final","pr_class","empty"));
        firstHashMap.put("<classState>", Set.of("pr_abstract","pr_static","pr_final","pr_class"));
        firstHashMap.put("<optionalModifier>", Set.of("pr_abstract","pr_static","pr_final","empty"));
        firstHashMap.put("<optionalInheritance>", Set.of("pr_extends","empty"));
        firstHashMap.put("<membersList>", Set.of("EOF","pr_abstract","pr_static","pr_final","empty"));
        firstHashMap.put("<optionalMemberModifier>", Set.of("pr_abstract","pr_static","pr_final"));
        firstHashMap.put("<member>", Set.of("public","pr_abstract","pr_static","pr_final","pr_void","idClase","pr_boolean","pr_int","pr_char"));
        firstHashMap.put("<memberMethod>", Set.of("openParenthesis"));
        firstHashMap.put("<constructor>", Set.of("public"));
        firstHashMap.put("<typeMethod>", Set.of("idClase","pr_boolean","pr_int","pr_char","pr_void"));
        firstHashMap.put("<type>", Set.of("idClase","pr_boolean","pr_int","pr_char"));
        firstHashMap.put("<primitiveType>", Set.of("pr_boolean","pr_int","pr_char"));
        firstHashMap.put("<formalArgs>", Set.of("openParenthesis"));
        firstHashMap.put("<optionalFormalArgsList>", Set.of("idClase","pr_boolean","pr_int","pr_char","empty"));
        firstHashMap.put("<formalArgsList>", Set.of("idClase","pr_boolean","pr_int","pr_char"));
        firstHashMap.put("<formalArgsLeft>", Set.of("comma"));
        firstHashMap.put("<formalArg>", Set.of("idClase","pr_boolean","pr_int","pr_char"));
        firstHashMap.put("<optionalBlock>", Set.of("openBraces","semicolon"));
        firstHashMap.put("<block>", Set.of("openBraces"));
        firstHashMap.put("<sentenceList>", Set.of("minus","decrement","plus","increment","not","pr_true","pr_false","intLiteral","charLiteral","pr_null","pr_this","stringLiteral","idMetVar","pr_new","idClase","openParenthesis","pr_var","pr_return","pr_if","pr_while","openBraces","empty"));
        firstHashMap.put("<sentence>", Set.of("minus","decrement","plus","increment","not","pr_true","pr_false","intLiteral","charLiteral","pr_null","pr_this","stringLiteral","idMetVar","pr_new","idClase","openParenthesis","pr_var","pr_return","pr_if","pr_while","openBraces"));
        firstHashMap.put("<assignCall>", Set.of("minus","decrement","plus","increment","not","pr_true","pr_false","intLiteral","charLiteral","pr_null","pr_this","stringLiteral","idMetVar","pr_new","idClase","openParenthesis"));
        firstHashMap.put("<localVar>", Set.of("pr_var"));
        firstHashMap.put("<returnState>", Set.of("pr_return"));
        firstHashMap.put("<optionalExpression>", Set.of("minus","decrement","plus","increment","not","pr_true","pr_false","intLiteral","charLiteral","pr_null","pr_this","stringLiteral","idMetVar","pr_new","idClase","openParenthesis","empty"));
        firstHashMap.put("<ifState>", Set.of("pr_if"));
        firstHashMap.put("<elseSentence>", Set.of("pr_else"));
        firstHashMap.put("<whileState>", Set.of("pr_while"));
        firstHashMap.put("<expression>", Set.of("minus","decrement","plus","increment","not","pr_true","pr_false","intLiteral","charLiteral","pr_null","pr_this","stringLiteral","idMetVar","pr_new","idClase","openParenthesis"));
        firstHashMap.put("<extraExpression>", Set.of("equals","empty"));
        firstHashMap.put("<assignOperator>", Set.of("equals"));
        firstHashMap.put("<composedExpression>", Set.of("minus","decrement","plus","increment","not","pr_true","pr_false","intLiteral","charLiteral","pr_null","pr_this","stringLiteral","idMetVar","pr_new","idClase","openParenthesis"));
        firstHashMap.put("<composedExpressionLeft>", Set.of("or","and","equality","inequality","less","lessOrEqual","greater","greaterOrEqual","plus","minus","asterisk","slash","modulo","empty"));
        firstHashMap.put("<binaryOperator>", Set.of("or","and","equality","inequality","less","lessOrEqual","greater","greaterOrEqual","plus","minus","asterisk","slash","modulo"));
        firstHashMap.put("<basicExpression>", Set.of("minus","decrement","plus","increment","not","pr_true","pr_false","intLiteral","charLiteral","pr_null","pr_this","stringLiteral","idMetVar","pr_new","idClase","openParenthesis"));
        firstHashMap.put("<unaryOperator>", Set.of("minus","decrement","plus","increment","not"));
        firstHashMap.put("<operand>", Set.of("pr_this","stringLiteral","idMetVar","pr_new","idClase","openParenthesis"));
        firstHashMap.put("<primitive>", Set.of("pr_true","pr_false","intLiteral","charLiteral","pr_null"));
        firstHashMap.put("<reference>", Set.of("pr_this","stringLiteral","idMetVar","pr_new","idClase","openParenthesis"));
        firstHashMap.put("<chainReference>", Set.of("dot","empty"));
        firstHashMap.put("<primary>", Set.of("pr_this","stringLiteral","idMetVar","pr_new","idClase","openParenthesis"));
        firstHashMap.put("<varMethodAccess>", Set.of("idMetVar"));
        firstHashMap.put("<possibleArgs>", Set.of("openParenthesis","empty"));
        firstHashMap.put("<constructorCall>", Set.of("pr_new"));
        firstHashMap.put("<parenthesisExpression>", Set.of("openParenthesis"));
        firstHashMap.put("<staticCall>", Set.of("idClase"));
        firstHashMap.put("<currentArgs>", Set.of("openParenthesis"));
        firstHashMap.put("<optionalExpressionList>", Set.of("minus","decrement","plus","increment","not","pr_true","pr_false","intLiteral","charLiteral","pr_null","pr_this","stringLiteral","idMetVar","pr_new","idClase","openParenthesis","empty"));
        firstHashMap.put("<expressionList>", Set.of("comma","empty"));
        firstHashMap.put("<chainElement>", Set.of("openParenthesis","empty"));
    }

    public Set<String> getFirsts(String key) {
        return firstHashMap.get(key);
    }
}
