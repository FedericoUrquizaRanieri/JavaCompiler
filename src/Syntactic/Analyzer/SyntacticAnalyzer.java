package Syntactic.Analyzer;

import Lexical.Analyzer.LexicalAnalyzer;
import Lexical.Analyzer.Token;
import Lexical.LexExceptions.LexicalException;
import Syntactic.SynExceptions.SyntacticException;

public class SyntacticAnalyzer {
    private final LexicalAnalyzer analyzer;
    private Token currentToken;
    private boolean errors;
    private boolean panicState;
    private final FirstsMap firstsMap;

    public SyntacticAnalyzer(LexicalAnalyzer analyzer, FirstsMap firstsMap) {
        this.analyzer = analyzer;
        this.firstsMap = firstsMap;
    }

    public boolean notErrorInFile() {
        return errors;
    }

    void match(String tokenName){
        if (tokenName.equals(currentToken.getTokenName())){
            try {
                currentToken = analyzer.getNextToken();
            } catch (LexicalException e) {
                e.printError(analyzer.getLine());
            }
        } else {
            //now we throw
        }
    }

    public void startAnalysis(){
        try {
            currentToken = analyzer.getNextToken();
        } catch (LexicalException e) {
            e.printError(analyzer.getLine());
        }
        start();
    }
    
    private void start() {
        classesList();
        match("EOF");
    }

    private void classesList() {
        if(firstsMap.getFirsts("<classState>").contains(currentToken.getTokenName())) {
            classState();
            classesList();
        } else {
            //TODO nada
        }
    }

    private void classState() {
        optionalModifier();
        match("pr_class");
        match("idClase");
        optionalInheritance();
        match("openBrace");
        membersList();
        match("closeBrace");
    }

    private void optionalInheritance() {
        if (firstsMap.getFirsts("<optionalModifier>").contains(currentToken.getTokenName())){
            match("pr_extends");
            match("idClase");
        } else {
            //TODO nada
        }
    }

    private void optionalModifier() {
        switch (currentToken.getTokenName()){
            case "pr_abstract" -> match("pr_abstract");
            case "pr_static" -> match("pr_static");
            case "pr_final" -> match("pr_final");
        }
    }

    private void membersList() {
        if (firstsMap.getFirsts("member").contains(currentToken.getTokenName())){
            member();
            membersList();
        } else {
            //TODO nada
        }
    }

    private void optionalMemberModifier() {
        switch (currentToken.getTokenName()){
            case "pr_abstract" -> match("pr_abstract");
            case "pr_static" -> match("pr_static");
            case "pr_final" -> match("pr_final");
            default -> System.out.println();//TODO aca tira error?
        }
    }

    private void member() {
        if (firstsMap.getFirsts("constructor").contains(currentToken.getTokenName())){
            constructor();
        } else if (firstsMap.getFirsts("optionalMemberModifier").contains(currentToken.getTokenName())){
            optionalMemberModifier();
            typeMethod();
            match("idMetVar");
            formalArgs();
            optionalBlock();
        } else if (firstsMap.getFirsts("type").contains(currentToken.getTokenName())){
            type();
            match("idMetVar");
            memberMethod();
        } else if(currentToken.getTokenName().equals("pr_void")){
            match("pr_void");
            match("idMetVar");
            formalArgs();
            optionalBlock();
        } else {
            //TODO error?
        }
    }

    private void memberMethod() {
        if (firstsMap.getFirsts("formalArgs").contains(currentToken.getTokenName())){
            formalArgs();
            optionalBlock();
        } else {
            //TODO nada
        }
    }

    private void constructor() {
        match("pr_public");
        match("idClase");
        formalArgs();
        block();
    }

    private void typeMethod() {
        if (firstsMap.getFirsts("type").contains(currentToken.getTokenName())){
            type();
        } else if (currentToken.getTokenName().equals("pr_void")){ //TODO esto esta en todos lados y esta mal creo
            match("pr_void");
        } else {
            //TODO error?
        }
    }

    private void type() {
        if (firstsMap.getFirsts("primitiveType").contains(currentToken.getTokenName())){
            primitiveType();
        } else if (currentToken.getTokenName().equals("idClase")){ //TODO esto esta en todos lados y esta mal creo
            match("idClase");
        } else {
            //TODO error?
        }
    }

    private void primitiveType(){
        switch (currentToken.getTokenName()){
            case "pr_boolean" -> match("pr_boolean");
            case "pr_char" -> match("pr_char");
            case "pr_int" -> match("pr_int");
            default -> System.out.println();//TODO aca tira error?
        }
    }

    private void formalArgs() {
        match("openParenthesis");
        optionalFormalArgsList();
        match("closeParenthesis");
    }

    private void optionalFormalArgsList(){
        if (firstsMap.getFirsts("formalArgsList").contains(currentToken.getTokenName())){
            formalArgsList();
        } else {
            //TODO nada
        }
    }

    private void formalArgsList(){
        if (firstsMap.getFirsts("formalArgs").contains(currentToken.getTokenName())){
            formalArgs();
            formalArgListLeft();
        } else {
            //TODO error?
        }
    }

    private void formalArgListLeft(){
        if (currentToken.getTokenName().equals("comma")){ //TODO esto esta para el tuge
            match("comma");
            formalArgs();
            formalArgListLeft();
        } else {
            //TODO nada
        }
    }

    private void formalArg(){
        if (firstsMap.getFirsts("type").contains(currentToken.getTokenName())){
            type();
            match("idMetVar");
        } else {
            //TODO error?
        }
    }

    private void optionalBlock() {
        if (firstsMap.getFirsts("block").contains(currentToken.getTokenName())){
            block();
        } else {
            match("semicolon");
        }
    }

    private void block() {
        match("openBrace");
        sentenceList();
        match("openBrace");
    }

    private void sentenceList() {
        if (firstsMap.getFirsts("sentence").contains(currentToken.getTokenName())){
            sentence();
            sentenceList();
        } else {
            //TODO nada
        }
    }

    private void sentence() {
        if (currentToken.getTokenName().equals("semicolon")){
            match("semicolon");
        } else if (firstsMap.getFirsts("assignCall").contains(currentToken.getTokenName())){
            assignCall();
        } else if (firstsMap.getFirsts("localVar").contains(currentToken.getTokenName())){
            localVar();
        } else if (firstsMap.getFirsts("returnState").contains(currentToken.getTokenName())){
            returnState();
        } else if (firstsMap.getFirsts("ifState").contains(currentToken.getTokenName())){
            ifState();
        } else if (firstsMap.getFirsts("whileState").contains(currentToken.getTokenName())){
            whileState();
        } else if (firstsMap.getFirsts("block").contains(currentToken.getTokenName())){
            block();
        } else {
            //TODO error?
        }
    }

    private void assignCall() {
        if (firstsMap.getFirsts("expression").contains(currentToken.getTokenName())){
            expression();
        } else {
            //TODO error?
        }
    }

    private void localVar() {
        match("pr_var");
        match("idMetVar");
        match("equals");
        composedExpression();
    }

    private void returnState() {
        match("pr_return");
        optionalExpression();
    }

    private void optionalExpression() {
        if (firstsMap.getFirsts("expression").contains(currentToken.getTokenName())){
            expression();
        } else {
            //TODO nada
        }
    }

    private void ifState() {
        match("ifState");
        match("openParenthesis");
        expression();
        match("closeParenthesis");
        sentence();
        elseSentence();
    }

    private void elseSentence() {
        if (currentToken.getTokenName().equals("pr_else")){
            match("pr_else");
            sentence();
        } else {
            //TODO nada
        }
    }

    private void whileState() {
        match("pr_while");
        match("openParenthesis");
        expression();
        match("closeParenthesis");
        sentence();
    }

    private void expression() {
        if (firstsMap.getFirsts("composedExpression").contains(currentToken.getTokenName())){
            composedExpression();
            extraExpression();
        } else {
            //TODO error?
        }
    }

    private void extraExpression() {
        if (firstsMap.getFirsts("assignOperator").contains(currentToken.getTokenName())){
            assignOperator();
            composedExpression();
        } else {
            //TODO nada
        }
    }

    private void assignOperator() {
        match("equals");
    }

    private void composedExpression() {
        if (firstsMap.getFirsts("basicExpression").contains(currentToken.getTokenName())){
            basicExpression();
            composedExpressionLeft();
        } else {
            //TODO error?
        }
    }

    private void composedExpressionLeft() {
        if (firstsMap.getFirsts("binaryOperator").contains(currentToken.getTokenName())){
            binaryOperator();
            basicExpression();
        } else {
            //TODO nada
        }
    }

    private void binaryOperator() {
        switch (currentToken.getTokenName()){
            case "or" -> match("or");
            case "and" -> match("and");
            case "equality" -> match("equality");
            case "inequality" -> match("inequality");
            case "less" -> match("less");
            case "lessOrEqual" -> match("lessOrEqual");
            case "greater" -> match("greater");
            case "greaterOrEqual" -> match("greaterOrEqual");
            case "plus" -> match("plus");
            case "minus" -> match("minus");
            case "asterisk" -> match("asterisk");
            case "slash" -> match("slash");
            case "modulo" -> match("modulo");
            default -> System.out.println();//TODO aca tira error?
        }
    }

    private void basicExpression() {
        if (firstsMap.getFirsts("unaryOperator").contains(currentToken.getTokenName())){
            unaryOperator();
            operand();
        } else if (firstsMap.getFirsts("operand").contains(currentToken.getTokenName())){
            operand();
        } else {
            //TODO error?
        }
    }

    private void unaryOperator() {
        switch (currentToken.getTokenName()){
            case "plus" -> match("plus");
            case "minus" -> match("minus");
            case "decrement" -> match("decrement");
            case "increment" -> match("increment");
            case "not" -> match("not");
            default -> System.out.println();//TODO aca tira error?
        }
    }

    private void operand() {
        if (firstsMap.getFirsts("primitive").contains(currentToken.getTokenName())){
            primitive();
        } else if (firstsMap.getFirsts("reference").contains(currentToken.getTokenName())){
            reference();
        } else {
            //TODO error?
        }
    }

    private void primitive() {
        switch (currentToken.getTokenName()){
            case "pr_true" -> match("pr_true");
            case "pr_false" -> match("pr_false");
            case "intLiteral" -> match("intLiteral");
            case "charLiteral" -> match("charLiteral");
            case "pr_null" -> match("pr_null");
            default -> System.out.println();//TODO aca tira error?
        }
    }

    private void reference() {
        if (firstsMap.getFirsts("primary").contains(currentToken.getTokenName())){
            primary();
            chainReference();
        } else {
            //TODO error?
        }
    }

    private void chainReference() {
        if (currentToken.getTokenName().equals("dot")){
            match("dot");
            match("idMetVar");
            chainElement();
            chainReference();
        } else {
            //TODO nada?
        }
    }

    private void primary() {
        if (currentToken.getTokenName().equals("pr_this")){
            match("pr_this");
        } else if (currentToken.getTokenName().equals("stringLiteral")){
            match("stringLiteral");
        }else if (firstsMap.getFirsts("varMethodAccess").contains(currentToken.getTokenName())){
            varMethodAccess();
        } else if (firstsMap.getFirsts("constructorCall").contains(currentToken.getTokenName())){
            constructorCall();
        } else if (firstsMap.getFirsts("staticCall").contains(currentToken.getTokenName())){
            staticCall();
        } else if (firstsMap.getFirsts("parenthesisExpression").contains(currentToken.getTokenName())){
            parenthesisExpression();
        } else {
            //TODO error?
        }
    }

    private void varMethodAccess() {
        match("idMetVar");
        possibleArgs();
    }

    private void possibleArgs() {
        if (firstsMap.getFirsts("currentArgs").contains(currentToken.getTokenName())){
            currentArgs();
        } else {
            //TODO nada
        }
    }

    private void constructorCall() {
        match("pr_new");
        match("idClase");
        currentArgs();
    }

    private void parenthesisExpression() {
        match("openParenthesis");
        expression();
        match("closeParenthesis");
    }

    private void staticCall() {
        match("idClase");
        match("dot");
        match("idMetVar");
        currentArgs();
    }

    private void currentArgs() {
        match("openParenthesis");
        optionalExpressionList();
        match("closeParenthesis");
    }

    private void optionalExpressionList() {
        if (firstsMap.getFirsts("expression").contains(currentToken.getTokenName())){
            expression();
            expressionList();
        } else {
            //TODO nada
        }
    }

    private void expressionList() {
        if (currentToken.getTokenName().equals("dot")){
            match("dot");
            expression();
            expressionList();
        } else {
            //TODO nada
        }
    }

    private void chainElement() {
        if (firstsMap.getFirsts("currentArgs").contains(currentToken.getTokenName())){
            currentArgs();
        } else {
            //TODO nada
        }
    }
}