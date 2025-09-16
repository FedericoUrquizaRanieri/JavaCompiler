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

    void match(String tokenName) throws SyntacticException {
        if (tokenName.equals(currentToken.getTokenName())) {
            try {
                currentToken = analyzer.getNextToken();
            } catch (LexicalException e) {
                e.printError(analyzer.getLine());
            }
        } else {
            throw new SyntacticException(currentToken.getLexeme(), tokenName, analyzer.getLineNumber());
        }
    }

    public void startAnalysis() throws SyntacticException {
        try {
            currentToken = analyzer.getNextToken();
        } catch (LexicalException e) {
            e.printError(analyzer.getLine());
        }
        start();
    }

    private void start() throws SyntacticException {
        classesList();
        match("EOF");
    }

    private void classesList() throws SyntacticException {
        if (firstsMap.getFirsts("classState").contains(currentToken.getTokenName())) {
            classState();
            classesList();
        } else {
            //TODO nada
        }
    }

    private void classState() throws SyntacticException {
        optionalModifier();
        match("pr_class");
        match("idClase");
        optionalInheritance();
        match("openBrace");
        membersList();
        match("closeBrace");
    }

    private void optionalModifier() throws SyntacticException {
        switch (currentToken.getTokenName()) {
            case "pr_abstract" -> match("pr_abstract");
            case "pr_static" -> match("pr_static");
            case "pr_final" -> match("pr_final");
        }
    }

    private void optionalInheritance() throws SyntacticException {
        if (firstsMap.getFirsts("optionalInheritance").contains(currentToken.getTokenName())) {
            match("pr_extends");
            match("idClase");
        } else {
            //TODO nada
        }
    }

    private void membersList() throws SyntacticException {
        if (firstsMap.getFirsts("member").contains(currentToken.getTokenName())) {
            member();
            membersList();
        } else {
            //TODO nada
        }
    }

    private void optionalMemberModifier() throws SyntacticException {
        switch (currentToken.getTokenName()) {
            case "pr_abstract" -> match("pr_abstract");
            case "pr_static" -> match("pr_static");
            case "pr_final" -> match("pr_final");
            default -> throw new SyntacticException(currentToken.getLexeme(), String.join(", ", firstsMap.getFirsts("optionalMemberModifier")), analyzer.getLineNumber());
        }
    }

    private void member() throws SyntacticException {
        if (firstsMap.getFirsts("constructor").contains(currentToken.getTokenName())) {
            constructor();
        } else if (firstsMap.getFirsts("optionalMemberModifier").contains(currentToken.getTokenName())) {
            optionalMemberModifier();
            typeMethod();
            match("idMetVar");
            formalArgs();
            optionalBlock();
        } else if (firstsMap.getFirsts("type").contains(currentToken.getTokenName())) {
            type();
            match("idMetVar");
            memberMethod();
        } else if (currentToken.getTokenName().equals("pr_void")) {
            match("pr_void");
            match("idMetVar");
            formalArgs();
            optionalBlock();
        } else {
            throw new SyntacticException(currentToken.getLexeme(), String.join(", ", firstsMap.getFirsts("member")), analyzer.getLineNumber());
        }
    }

    private void memberMethod() throws SyntacticException {
        if (firstsMap.getFirsts("formalArgs").contains(currentToken.getTokenName())) {
            formalArgs();
            optionalBlock();
        } else if (currentToken.getTokenName().equals("semicolon")) {
            match("semicolon");
        } else {
            throw new SyntacticException(currentToken.getLexeme(), String.join(", ", firstsMap.getFirsts("memberMethod")), analyzer.getLineNumber());
        }
    }

    private void constructor() throws SyntacticException {
        match("pr_public");
        match("idClase");
        formalArgs();
        block();
    }

    private void typeMethod() throws SyntacticException {
        if (firstsMap.getFirsts("type").contains(currentToken.getTokenName())) {
            type();
        } else if (currentToken.getTokenName().equals("pr_void")) {
            match("pr_void");
        } else {
            throw new SyntacticException(currentToken.getLexeme(), String.join(", ", firstsMap.getFirsts("typeMethod")), analyzer.getLineNumber());
        }
    }

    private void type() throws SyntacticException {
        if (firstsMap.getFirsts("primitiveType").contains(currentToken.getTokenName())) {
            primitiveType();
        } else if (currentToken.getTokenName().equals("idClase")) {
            match("idClase");
        } else {
            throw new SyntacticException(currentToken.getLexeme(), String.join(", ", firstsMap.getFirsts("type")), analyzer.getLineNumber());
        }
    }

    private void primitiveType() throws SyntacticException {
        switch (currentToken.getTokenName()) {
            case "pr_boolean" -> match("pr_boolean");
            case "pr_char" -> match("pr_char");
            case "pr_int" -> match("pr_int");
            default ->
                    throw new SyntacticException(currentToken.getLexeme(), String.join(", ", firstsMap.getFirsts("primitiveType")), analyzer.getLineNumber());
        }
    }

    private void formalArgs() throws SyntacticException {
        match("openParenthesis");
        optionalFormalArgsList();
        match("closeParenthesis");
    }

    private void optionalFormalArgsList() throws SyntacticException {
        if (firstsMap.getFirsts("formalArgsList").contains(currentToken.getTokenName())) {
            formalArgsList();
        } else {
            //TODO nada
        }
    }

    private void formalArgsList() throws SyntacticException {
        if (firstsMap.getFirsts("formalArg").contains(currentToken.getTokenName())) {
            formalArg();
            formalArgsLeft();
        } else {
            throw new SyntacticException(currentToken.getLexeme(), String.join(", ", firstsMap.getFirsts("formalArgsList")), analyzer.getLineNumber());
        }
    }

    private void formalArgsLeft() throws SyntacticException {
        if (currentToken.getTokenName().equals("comma")) {
            match("comma");
            formalArg();
            formalArgsLeft();
        } else {
            //TODO nada
        }
    }

    private void formalArg() throws SyntacticException {
        if (firstsMap.getFirsts("type").contains(currentToken.getTokenName())) {
            type();
            match("idMetVar");
        } else {
            throw new SyntacticException(currentToken.getLexeme(), String.join(", ", firstsMap.getFirsts("formalArg")), analyzer.getLineNumber());
        }
    }

    private void optionalBlock() throws SyntacticException {
        if (firstsMap.getFirsts("block").contains(currentToken.getTokenName())) {
            block();
        } else if (currentToken.getTokenName().equals("semicolon")){
            match("semicolon");
        } else {
            throw new SyntacticException(currentToken.getLexeme(), String.join(", ", firstsMap.getFirsts("optionalBlock")), analyzer.getLineNumber());

        }
    }

    private void block() throws SyntacticException {
        match("openBrace");
        sentenceList();
        match("closeBrace");
    }

    private void sentenceList() throws SyntacticException {
        if (firstsMap.getFirsts("sentence").contains(currentToken.getTokenName())) {
            sentence();
            sentenceList();
        } else {
            //TODO nada
        }
    }

    private void sentence() throws SyntacticException {
        if (currentToken.getTokenName().equals("semicolon")) {
            match("semicolon");
        } else if (firstsMap.getFirsts("assignCall").contains(currentToken.getTokenName())) {
            assignCall();
            match("semicolon");
        } else if (firstsMap.getFirsts("localVar").contains(currentToken.getTokenName())) {
            localVar();
            match("semicolon");
        } else if (firstsMap.getFirsts("returnState").contains(currentToken.getTokenName())) {
            returnState();
            match("semicolon");
        } else if (firstsMap.getFirsts("ifState").contains(currentToken.getTokenName())) {
            ifState();
        } else if (firstsMap.getFirsts("whileState").contains(currentToken.getTokenName())) {
            whileState();
        } else if (firstsMap.getFirsts("block").contains(currentToken.getTokenName())) {
            block();
        } else {
            throw new SyntacticException(currentToken.getLexeme(), String.join(", ", firstsMap.getFirsts("sentence")), analyzer.getLineNumber());
        }
    }

    private void assignCall() throws SyntacticException {
        if (firstsMap.getFirsts("expression").contains(currentToken.getTokenName())) {
            expression();
        } else {
            throw new SyntacticException(currentToken.getLexeme(), String.join(", ", firstsMap.getFirsts("assignCall")), analyzer.getLineNumber());
        }
    }

    private void localVar() throws SyntacticException {
        match("pr_var");
        match("idMetVar");
        match("equals");
        composedExpression();
    }

    private void returnState() throws SyntacticException {
        match("pr_return");
        optionalExpression();
    }

    private void optionalExpression() throws SyntacticException {
        if (firstsMap.getFirsts("expression").contains(currentToken.getTokenName())) {
            expression();
        } else {
            //TODO nada
        }
    }

    private void ifState() throws SyntacticException {
        match("pr_if");
        match("openParenthesis");
        expression();
        match("closeParenthesis");
        sentence();
        elseSentence();
    }

    private void elseSentence() throws SyntacticException {
        if (currentToken.getTokenName().equals("pr_else")) {
            match("pr_else");
            sentence();
        } else {
            //TODO nada
        }
    }

    private void whileState() throws SyntacticException {
        match("pr_while");
        match("openParenthesis");
        expression();
        match("closeParenthesis");
        sentence();
    }

    private void expression() throws SyntacticException {
        if (firstsMap.getFirsts("composedExpression").contains(currentToken.getTokenName())) {
            composedExpression();
            extraExpression();
        } else {
            throw new SyntacticException(currentToken.getLexeme(), String.join(", ", firstsMap.getFirsts("expression")), analyzer.getLineNumber());
        }
    }

    private void extraExpression() throws SyntacticException {
        if (firstsMap.getFirsts("assignOperator").contains(currentToken.getTokenName())) {
            assignOperator();
            composedExpression();
        } else {
            //TODO nada
        }
    }

    private void assignOperator() throws SyntacticException {
        match("equals");
    }

    private void composedExpression() throws SyntacticException {
        if (firstsMap.getFirsts("basicExpression").contains(currentToken.getTokenName())) {
            basicExpression();
            composedExpressionLeft();
        } else {
            throw new SyntacticException(currentToken.getLexeme(), String.join(", ", firstsMap.getFirsts("composedExpression")), analyzer.getLineNumber());
        }
    }

    private void composedExpressionLeft() throws SyntacticException {
        if (firstsMap.getFirsts("binaryOperator").contains(currentToken.getTokenName())) {
            binaryOperator();
            basicExpression();
            composedExpressionLeft();
        } else {
            //TODO nada
        }
    }

    private void binaryOperator() throws SyntacticException {
        switch (currentToken.getTokenName()) {
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
            default ->
                    throw new SyntacticException(currentToken.getLexeme(), String.join(", ", firstsMap.getFirsts("binaryOperator")), analyzer.getLineNumber());
        }
    }

    private void basicExpression() throws SyntacticException {
        if (firstsMap.getFirsts("unaryOperator").contains(currentToken.getTokenName())) {
            unaryOperator();
            operand();
        } else if (firstsMap.getFirsts("operand").contains(currentToken.getTokenName())) {
            operand();
        } else {
            throw new SyntacticException(currentToken.getLexeme(), String.join(", ", firstsMap.getFirsts("basicExpression")), analyzer.getLineNumber());
        }
    }

    private void unaryOperator() throws SyntacticException {
        switch (currentToken.getTokenName()) {
            case "plus" -> match("plus");
            case "minus" -> match("minus");
            case "decrement" -> match("decrement");
            case "increment" -> match("increment");
            case "not" -> match("not");
            default ->
                    throw new SyntacticException(currentToken.getLexeme(), String.join(", ", firstsMap.getFirsts("unaryOperator")), analyzer.getLineNumber());
        }
    }

    private void operand() throws SyntacticException {
        if (firstsMap.getFirsts("primitive").contains(currentToken.getTokenName())) {
            primitive();
        } else if (firstsMap.getFirsts("reference").contains(currentToken.getTokenName())) {
            reference();
        } else {
            throw new SyntacticException(currentToken.getLexeme(), String.join(", ", firstsMap.getFirsts("operand")), analyzer.getLineNumber());
        }
    }

    private void primitive() throws SyntacticException {
        switch (currentToken.getTokenName()) {
            case "pr_true" -> match("pr_true");
            case "pr_false" -> match("pr_false");
            case "intLiteral" -> match("intLiteral");
            case "charLiteral" -> match("charLiteral");
            case "pr_null" -> match("pr_null");
            default ->
                    throw new SyntacticException(currentToken.getLexeme(), String.join(", ", firstsMap.getFirsts("primitive")), analyzer.getLineNumber());
        }
    }

    private void reference() throws SyntacticException {
        if (firstsMap.getFirsts("primary").contains(currentToken.getTokenName())) {
            primary();
            chainReference();
        } else {
            throw new SyntacticException(currentToken.getLexeme(), String.join(", ", firstsMap.getFirsts("reference")), analyzer.getLineNumber());
        }
    }

    private void chainReference() throws SyntacticException {
        if (currentToken.getTokenName().equals("dot")) {
            match("dot");
            match("idMetVar");
            chainElement();
            chainReference();
        } else {
            //TODO nada?
        }
    }

    private void primary() throws SyntacticException {
        if (currentToken.getTokenName().equals("pr_this")) {
            match("pr_this");
        } else if (currentToken.getTokenName().equals("stringLiteral")) {
            match("stringLiteral");
        } else if (firstsMap.getFirsts("varMethodAccess").contains(currentToken.getTokenName())) {
            varMethodAccess();
        } else if (firstsMap.getFirsts("constructorCall").contains(currentToken.getTokenName())) {
            constructorCall();
        } else if (firstsMap.getFirsts("staticCall").contains(currentToken.getTokenName())) {
            staticCall();
        } else if (firstsMap.getFirsts("parenthesisExpression").contains(currentToken.getTokenName())) {
            parenthesisExpression();
        } else {
            throw new SyntacticException(currentToken.getLexeme(), String.join(", ", firstsMap.getFirsts("primary")), analyzer.getLineNumber());
        }
    }

    private void varMethodAccess() throws SyntacticException {
        match("idMetVar");
        possibleArgs();
    }

    private void possibleArgs() throws SyntacticException {
        if (firstsMap.getFirsts("currentArgs").contains(currentToken.getTokenName())) {
            currentArgs();
        } else {
            //TODO nada
        }
    }

    private void constructorCall() throws SyntacticException {
        match("pr_new");
        match("idClase");
        currentArgs();
    }

    private void parenthesisExpression() throws SyntacticException {
        match("openParenthesis");
        expression();
        match("closeParenthesis");
    }

    private void staticCall() throws SyntacticException {
        match("idClase");
        match("dot");
        match("idMetVar");
        currentArgs();
    }

    private void currentArgs() throws SyntacticException {
        match("openParenthesis");
        optionalExpressionList();
        match("closeParenthesis");
    }

    private void optionalExpressionList() throws SyntacticException {
        if (firstsMap.getFirsts("expression").contains(currentToken.getTokenName())) {
            expression();
            expressionList();
        } else {
            //TODO nada
        }
    }

    private void expressionList() throws SyntacticException {
        if (currentToken.getTokenName().equals("comma")) {
            match("comma");
            expression();
            expressionList();
        } else {
            //TODO nada
        }
    }

    private void chainElement() throws SyntacticException {
        if (firstsMap.getFirsts("currentArgs").contains(currentToken.getTokenName())) {
            currentArgs();
        } else {
            //TODO nada
        }
    }
}