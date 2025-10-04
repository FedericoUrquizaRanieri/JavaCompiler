package Syntactic.Analyzer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class ProductionsMap {
    private final HashMap<String, Set<String>> firstHashMap;
    private final HashMap<String, Set<String>> followHashMap;

    public ProductionsMap() {
        firstHashMap = new HashMap<>();
        completeFirsts();
        followHashMap = new HashMap<>();
        completeFollow();
    }

    private void completeFirsts() {
        firstHashMap.put("start", new HashSet<>(Set.of("EOF")));
        firstHashMap.put("classesList", new HashSet<>(Set.of()));
        firstHashMap.put("classState", new HashSet<>(Set.of("pr_class","pr_interface")));
        firstHashMap.put("optionalGenerics", new HashSet<>(Set.of("less")));
        firstHashMap.put("optionalModifier", new HashSet<>(Set.of("pr_abstract", "pr_static", "pr_final")));
        firstHashMap.put("optionalInheritance", new HashSet<>(Set.of("pr_extends","pr_implements")));
        firstHashMap.put("optionalInheritanceInterface", new HashSet<>(Set.of("pr_extends")));
        firstHashMap.put("membersList", new HashSet<>(Set.of()));
        firstHashMap.put("optionalMemberModifier", new HashSet<>(Set.of("pr_abstract", "pr_static", "pr_final")));
        firstHashMap.put("member", new HashSet<>(Set.of()));
        firstHashMap.put("memberMethod", new HashSet<>(Set.of("semicolon")));
        firstHashMap.put("constructor", new HashSet<>(Set.of("pr_public")));
        firstHashMap.put("typeMethod", new HashSet<>(Set.of("pr_void")));
        firstHashMap.put("type", new HashSet<>(Set.of("idClase")));
        firstHashMap.put("primitiveType", new HashSet<>(Set.of("pr_boolean", "pr_int", "pr_char")));
        firstHashMap.put("formalArgs", new HashSet<>(Set.of("openParenthesis")));
        firstHashMap.put("optionalFormalArgsList", new HashSet<>(Set.of()));
        firstHashMap.put("formalArgsList", new HashSet<>(Set.of()));
        firstHashMap.put("formalArgsLeft", new HashSet<>(Set.of("comma")));
        firstHashMap.put("formalArg", new HashSet<>(Set.of()));
        firstHashMap.put("optionalBlock", new HashSet<>(Set.of("semicolon")));
        firstHashMap.put("block", new HashSet<>(Set.of("openBrace")));
        firstHashMap.put("sentenceList", new HashSet<>(Set.of()));
        firstHashMap.put("sentence", new HashSet<>(Set.of("semicolon","pr_for")));
        firstHashMap.put("assignCall", new HashSet<>(Set.of()));
        firstHashMap.put("localVar", new HashSet<>(Set.of("pr_var")));
        firstHashMap.put("returnState", new HashSet<>(Set.of("pr_return")));
        firstHashMap.put("optionalExpression", new HashSet<>(Set.of()));
        firstHashMap.put("ifState", new HashSet<>(Set.of("pr_if")));
        firstHashMap.put("elseSentence", new HashSet<>(Set.of("pr_else")));
        firstHashMap.put("whileState", new HashSet<>(Set.of("pr_while")));
        firstHashMap.put("expression", new HashSet<>(Set.of()));
        firstHashMap.put("extraExpression", new HashSet<>(Set.of("questionMark")));
        firstHashMap.put("assignOperator", new HashSet<>(Set.of("equals")));
        firstHashMap.put("composedExpression", new HashSet<>(Set.of()));
        firstHashMap.put("composedExpressionLeft", new HashSet<>(Set.of("questionMark")));
        firstHashMap.put("binaryOperator", new HashSet<>(Set.of("or", "and", "equality", "inequality", "less", "lessOrEqual", "greater", "greaterOrEqual", "plus", "minus", "asterisk", "slash", "modulo")));
        firstHashMap.put("basicExpression", new HashSet<>(Set.of()));
        firstHashMap.put("unaryOperator", new HashSet<>(Set.of("minus", "decrement", "plus", "increment", "not")));
        firstHashMap.put("operand", new HashSet<>(Set.of()));
        firstHashMap.put("primitive", new HashSet<>(Set.of("pr_true", "pr_false", "intLiteral", "charLiteral", "pr_null")));
        firstHashMap.put("reference", new HashSet<>(Set.of()));
        firstHashMap.put("chainReference", new HashSet<>(Set.of("dot")));
        firstHashMap.put("primary", new HashSet<>(Set.of("pr_this", "stringLiteral")));
        firstHashMap.put("varMethodAccess", new HashSet<>(Set.of("idMetVar")));
        firstHashMap.put("possibleArgs", new HashSet<>(Set.of()));
        firstHashMap.put("constructorCall", new HashSet<>(Set.of("pr_new")));
        firstHashMap.put("parenthesisExpression", new HashSet<>(Set.of("openParenthesis")));
        firstHashMap.put("staticCall", new HashSet<>(Set.of("idClase")));
        firstHashMap.put("currentArgs", new HashSet<>(Set.of("openParenthesis")));
        firstHashMap.put("optionalExpressionList", new HashSet<>(Set.of()));
        firstHashMap.put("expressionList", new HashSet<>(Set.of("comma")));
        firstHashMap.put("chainElement", new HashSet<>(Set.of("openParenthesis")));

        firstHashMap.get("chainElement").addAll(firstHashMap.get("currentArgs"));
        firstHashMap.get("possibleArgs").addAll(firstHashMap.get("currentArgs"));
        firstHashMap.get("primary").addAll(firstHashMap.get("parenthesisExpression"));
        firstHashMap.get("primary").addAll(firstHashMap.get("staticCall"));
        firstHashMap.get("primary").addAll(firstHashMap.get("constructorCall"));
        firstHashMap.get("primary").addAll(firstHashMap.get("varMethodAccess"));
        firstHashMap.get("reference").addAll(firstHashMap.get("primary"));
        firstHashMap.get("operand").addAll(firstHashMap.get("reference"));
        firstHashMap.get("operand").addAll(firstHashMap.get("primitive"));
        firstHashMap.get("basicExpression").addAll(firstHashMap.get("operand"));
        firstHashMap.get("basicExpression").addAll(firstHashMap.get("unaryOperator"));
        firstHashMap.get("composedExpressionLeft").addAll(firstHashMap.get("binaryOperator"));
        firstHashMap.get("composedExpression").addAll(firstHashMap.get("basicExpression"));
        firstHashMap.get("extraExpression").addAll(firstHashMap.get("assignOperator"));
        firstHashMap.get("expression").addAll(firstHashMap.get("composedExpression"));
        firstHashMap.get("optionalExpressionList").addAll(firstHashMap.get("expression"));
        firstHashMap.get("optionalExpression").addAll(firstHashMap.get("expression"));
        firstHashMap.get("assignCall").addAll(firstHashMap.get("expression"));
        firstHashMap.get("sentence").addAll(firstHashMap.get("block"));
        firstHashMap.get("sentence").addAll(firstHashMap.get("whileState"));
        firstHashMap.get("sentence").addAll(firstHashMap.get("ifState"));
        firstHashMap.get("sentence").addAll(firstHashMap.get("returnState"));
        firstHashMap.get("sentence").addAll(firstHashMap.get("localVar"));
        firstHashMap.get("sentence").addAll(firstHashMap.get("assignCall"));
        firstHashMap.get("sentenceList").addAll(firstHashMap.get("sentence"));
        firstHashMap.get("optionalBlock").addAll(firstHashMap.get("block"));
        firstHashMap.get("type").addAll(firstHashMap.get("primitiveType"));
        firstHashMap.get("formalArg").addAll(firstHashMap.get("type"));
        firstHashMap.get("formalArgsList").addAll(firstHashMap.get("formalArg"));
        firstHashMap.get("optionalFormalArgsList").addAll(firstHashMap.get("formalArgsList"));
        firstHashMap.get("typeMethod").addAll(firstHashMap.get("type"));
        firstHashMap.get("memberMethod").addAll(firstHashMap.get("formalArgs"));
        firstHashMap.get("member").addAll(firstHashMap.get("typeMethod"));
        firstHashMap.get("member").addAll(firstHashMap.get("constructor"));
        firstHashMap.get("member").addAll(firstHashMap.get("optionalMemberModifier"));
        firstHashMap.get("membersList").addAll(firstHashMap.get("member"));
        firstHashMap.get("classState").addAll(firstHashMap.get("optionalModifier"));
        firstHashMap.get("classesList").addAll(firstHashMap.get("classState"));
        firstHashMap.get("start").addAll(firstHashMap.get("classesList"));

        firstHashMap.put("optionalDeclaration",new HashSet<>(Set.of("equals")));
    }

    private void completeFollow() {
        followHashMap.put("classesList", new HashSet<>(Set.of("EOF")));
        followHashMap.put("optionalModifier", new HashSet<>(Set.of("pr_class")));
        followHashMap.put("optionalInheritance", new HashSet<>(Set.of("openBrace")));
        followHashMap.put("optionalInheritanceInterface", new HashSet<>(Set.of("openBrace")));
        followHashMap.put("membersList", new HashSet<>(Set.of("closeBrace")));
        followHashMap.put("optionalFormalArgsList", new HashSet<>(Set.of("closeParenthesis")));
        followHashMap.put("formalArgsList", new HashSet<>(Set.of()));
        followHashMap.get("formalArgsList").addAll(followHashMap.get("optionalFormalArgsList"));
        followHashMap.put("formalArgsLeft", new HashSet<>(Set.of()));
        followHashMap.get("formalArgsLeft").addAll(followHashMap.get("formalArgsList"));
        followHashMap.put("sentenceList", new HashSet<>(Set.of("closeBrace")));
        followHashMap.put("returnState", new HashSet<>(Set.of("semicolon")));
        followHashMap.put("optionalExpressionList", new HashSet<>(Set.of("closeParenthesis")));
        followHashMap.put("expressionList", new HashSet<>(Set.of()));
        followHashMap.get("expressionList").addAll(followHashMap.get("optionalExpressionList"));

        followHashMap.put("optionalExpression", new HashSet<>(Set.of()));
        followHashMap.get("optionalExpression").addAll(followHashMap.get("returnState"));
        followHashMap.put("sentence", new HashSet<>(Set.of()));
        followHashMap.get("sentence").addAll(firstHashMap.get("sentenceList"));
        followHashMap.get("sentence").addAll(followHashMap.get("sentenceList"));
        followHashMap.get("sentence").addAll(firstHashMap.get("elseSentence"));
        followHashMap.put("ifState", new HashSet<>(Set.of()));
        followHashMap.get("ifState").addAll(followHashMap.get("sentence"));
        followHashMap.put("elseSentence", new HashSet<>(Set.of()));
        followHashMap.get("elseSentence").addAll(followHashMap.get("ifState"));

        followHashMap.put("assignCall", new HashSet<>(Set.of("semicolon")));
        followHashMap.get("expressionList").addAll(followHashMap.get("optionalExpressionList"));
        followHashMap.put("expression", new HashSet<>(Set.of("closeParenthesis","colon")));
        followHashMap.get("expression").addAll(followHashMap.get("assignCall"));
        followHashMap.get("expression").addAll(followHashMap.get("optionalExpression"));
        followHashMap.get("expression").addAll(followHashMap.get("optionalExpressionList"));
        followHashMap.get("expression").addAll(firstHashMap.get("expressionList"));
        followHashMap.get("expression").addAll(followHashMap.get("expressionList"));
        followHashMap.put("extraExpression", new HashSet<>(Set.of()));
        followHashMap.get("extraExpression").addAll(followHashMap.get("expression"));

        followHashMap.put("optionalDeclaration", new HashSet<>(Set.of("semicolon")));

        followHashMap.put("localVar", new HashSet<>(Set.of("semicolon")));
        followHashMap.put("composedExpression", new HashSet<>(Set.of("colon")));
        followHashMap.get("composedExpression").addAll(followHashMap.get("extraExpression"));
        followHashMap.get("composedExpression").addAll(firstHashMap.get("extraExpression"));
        followHashMap.get("composedExpression").addAll(followHashMap.get("expression"));
        followHashMap.get("composedExpression").addAll(followHashMap.get("localVar"));
        followHashMap.get("composedExpression").addAll(followHashMap.get("optionalDeclaration"));
        followHashMap.put("composedExpressionLeft", new HashSet<>(Set.of()));
        followHashMap.get("composedExpressionLeft").addAll(followHashMap.get("composedExpression"));
        followHashMap.put("basicExpression", new HashSet<>(Set.of()));
        followHashMap.get("basicExpression").addAll(firstHashMap.get("composedExpressionLeft"));
        followHashMap.get("basicExpression").addAll(followHashMap.get("composedExpression"));
        followHashMap.put("operand", new HashSet<>(Set.of()));
        followHashMap.get("operand").addAll(followHashMap.get("basicExpression"));
        followHashMap.put("reference", new HashSet<>(Set.of()));
        followHashMap.get("reference").addAll(followHashMap.get("operand"));
        followHashMap.put("chainReference", new HashSet<>(Set.of()));
        followHashMap.get("chainReference").addAll(followHashMap.get("reference"));

        followHashMap.put("primary", new HashSet<>(Set.of()));
        followHashMap.get("primary").addAll(firstHashMap.get("chainReference"));
        followHashMap.get("primary").addAll(followHashMap.get("reference"));
        followHashMap.put("varMethodAccess", new HashSet<>(Set.of()));
        followHashMap.get("varMethodAccess").addAll(followHashMap.get("primary"));
        followHashMap.put("possibleArgs", new HashSet<>(Set.of()));
        followHashMap.get("possibleArgs").addAll(followHashMap.get("varMethodAccess"));

        followHashMap.put("chainElement", new HashSet<>(Set.of()));
        followHashMap.get("chainElement").addAll(followHashMap.get("chainReference"));
        followHashMap.get("chainElement").addAll(firstHashMap.get("chainReference"));

        followHashMap.put("typeMethod", new HashSet<>(Set.of("idMetVar")));
        followHashMap.put("type", new HashSet<>(Set.of("idMetVar")));
        followHashMap.get("type").addAll(followHashMap.get("typeMethod"));
        followHashMap.put("classState", new HashSet<>(Set.of()));
        followHashMap.get("classState").addAll(firstHashMap.get("classesList"));
        followHashMap.get("classState").addAll(followHashMap.get("classesList"));
        followHashMap.put("optionalGenerics", new HashSet<>(Set.of()));
        followHashMap.get("optionalGenerics").addAll(followHashMap.get("type"));
        followHashMap.get("optionalGenerics").addAll(followHashMap.get("optionalInheritance"));
        followHashMap.get("optionalGenerics").addAll(followHashMap.get("optionalInheritanceInterface"));
        followHashMap.get("optionalGenerics").addAll(firstHashMap.get("optionalInheritance"));
        followHashMap.get("optionalGenerics").addAll(firstHashMap.get("optionalInheritanceInterface"));
        followHashMap.get("optionalGenerics").addAll(followHashMap.get("classState"));

        followHashMap.put("optionalGenericsConstructor", new HashSet<>(Set.of()));
        followHashMap.get("optionalGenericsConstructor").addAll(firstHashMap.get("currentArgs"));
    }

    public Set<String> getFirsts(String key) {
        return firstHashMap.get(key);
    }

    public Set<String> getFollow(String key) {
        return followHashMap.get(key);
    }
}
