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
    private final ProductionsMap productionsMap;

    public SyntacticAnalyzer(LexicalAnalyzer analyzer, ProductionsMap productionsMap) {
        this.analyzer = analyzer;
        this.productionsMap = productionsMap;
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
        if (productionsMap.getFirsts("classState").contains(currentToken.getTokenName())) {
            classState();
            classesList();
        } else if (!productionsMap.getFollow("classesList").contains(currentToken.getTokenName())) {
            throw new SyntacticException(currentToken.getLexeme(), String.join(", ", productionsMap.getFollow("classesList")), analyzer.getLineNumber());
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
        } //TODO aca va
    }

    private void optionalInheritance() throws SyntacticException {
        if (productionsMap.getFirsts("optionalInheritance").contains(currentToken.getTokenName())) {
            match("pr_extends");
            match("idClase");
        } else if (!productionsMap.getFollow("optionalInheritance").contains(currentToken.getTokenName())) {
            throw new SyntacticException(currentToken.getLexeme(), String.join(", ", productionsMap.getFollow("optionalInheritance")), analyzer.getLineNumber());
        }
    }

    private void membersList() throws SyntacticException {
        if (productionsMap.getFirsts("member").contains(currentToken.getTokenName())) {
            member();
            membersList();
        } else if (!productionsMap.getFollow("membersList").contains(currentToken.getTokenName())) {
            throw new SyntacticException(currentToken.getLexeme(), String.join(", ", productionsMap.getFollow("membersList")), analyzer.getLineNumber());
        }
    }

    private void optionalMemberModifier() throws SyntacticException {
        switch (currentToken.getTokenName()) {
            case "pr_abstract" -> match("pr_abstract");
            case "pr_static" -> match("pr_static");
            case "pr_final" -> match("pr_final");
            default -> throw new SyntacticException(currentToken.getLexeme(), String.join(", ", productionsMap.getFirsts("optionalMemberModifier")), analyzer.getLineNumber());
        }
    }

    private void member() throws SyntacticException {
        if (productionsMap.getFirsts("constructor").contains(currentToken.getTokenName())) {
            constructor();
        } else if (productionsMap.getFirsts("optionalMemberModifier").contains(currentToken.getTokenName())) {
            optionalMemberModifier();
            typeMethod();
            match("idMetVar");
            formalArgs();
            optionalBlock();
        } else if (productionsMap.getFirsts("type").contains(currentToken.getTokenName())) {
            type();
            match("idMetVar");
            memberMethod();
        } else if (currentToken.getTokenName().equals("pr_void")) {
            match("pr_void");
            match("idMetVar");
            formalArgs();
            optionalBlock();
        } else {
            throw new SyntacticException(currentToken.getLexeme(), String.join(", ", productionsMap.getFirsts("member")), analyzer.getLineNumber());
        }
    }

    private void memberMethod() throws SyntacticException {
        if (productionsMap.getFirsts("formalArgs").contains(currentToken.getTokenName())) {
            formalArgs();
            optionalBlock();
        } else if (currentToken.getTokenName().equals("semicolon")) {
            match("semicolon");
        } else {
            throw new SyntacticException(currentToken.getLexeme(), String.join(", ", productionsMap.getFirsts("memberMethod")), analyzer.getLineNumber());
        }
    }

    private void constructor() throws SyntacticException {
        match("pr_public");
        match("idClase");
        formalArgs();
        block();
    }

    private void typeMethod() throws SyntacticException {
        if (productionsMap.getFirsts("type").contains(currentToken.getTokenName())) {
            type();
        } else if (currentToken.getTokenName().equals("pr_void")) {
            match("pr_void");
        } else {
            throw new SyntacticException(currentToken.getLexeme(), String.join(", ", productionsMap.getFirsts("typeMethod")), analyzer.getLineNumber());
        }
    }

    private void type() throws SyntacticException {
        if (productionsMap.getFirsts("primitiveType").contains(currentToken.getTokenName())) {
            primitiveType();
        } else if (currentToken.getTokenName().equals("idClase")) {
            match("idClase");
        } else {
            throw new SyntacticException(currentToken.getLexeme(), String.join(", ", productionsMap.getFirsts("type")), analyzer.getLineNumber());
        }
    }

    private void primitiveType() throws SyntacticException {
        switch (currentToken.getTokenName()) {
            case "pr_boolean" -> match("pr_boolean");
            case "pr_char" -> match("pr_char");
            case "pr_int" -> match("pr_int");
            default ->
                    throw new SyntacticException(currentToken.getLexeme(), String.join(", ", productionsMap.getFirsts("primitiveType")), analyzer.getLineNumber());
        }
    }

    private void formalArgs() throws SyntacticException {
        match("openParenthesis");
        optionalFormalArgsList();
        match("closeParenthesis");
    }

    private void optionalFormalArgsList() throws SyntacticException {
        if (productionsMap.getFirsts("formalArgsList").contains(currentToken.getTokenName())) {
            formalArgsList();
        } else if (!productionsMap.getFollow("optionalFormalArgsList").contains(currentToken.getTokenName())) {
            throw new SyntacticException(currentToken.getLexeme(), String.join(", ", productionsMap.getFollow("optionalFormalArgsList")), analyzer.getLineNumber());
        }
    }

    private void formalArgsList() throws SyntacticException {
        if (productionsMap.getFirsts("formalArg").contains(currentToken.getTokenName())) {
            formalArg();
            formalArgsLeft();
        } else {
            throw new SyntacticException(currentToken.getLexeme(), String.join(", ", productionsMap.getFirsts("formalArgsList")), analyzer.getLineNumber());
        }
    }

    private void formalArgsLeft() throws SyntacticException {
        if (currentToken.getTokenName().equals("comma")) {
            match("comma");
            formalArg();
            formalArgsLeft();
        } else if (!productionsMap.getFollow("formalArgsLeft").contains(currentToken.getTokenName())) {
            throw new SyntacticException(currentToken.getLexeme(), String.join(", ", productionsMap.getFollow("formalArgsLeft")), analyzer.getLineNumber());
        }
    }

    private void formalArg() throws SyntacticException {
        if (productionsMap.getFirsts("type").contains(currentToken.getTokenName())) {
            type();
            match("idMetVar");
        } else {
            throw new SyntacticException(currentToken.getLexeme(), String.join(", ", productionsMap.getFirsts("formalArg")), analyzer.getLineNumber());
        }
    }

    private void optionalBlock() throws SyntacticException {
        if (productionsMap.getFirsts("block").contains(currentToken.getTokenName())) {
            block();
        } else if (currentToken.getTokenName().equals("semicolon")){
            match("semicolon");
        } else {
            throw new SyntacticException(currentToken.getLexeme(), String.join(", ", productionsMap.getFirsts("optionalBlock")), analyzer.getLineNumber());

        }
    }

    private void block() throws SyntacticException {
        match("openBrace");
        sentenceList();
        match("closeBrace");
    }

    private void sentenceList() throws SyntacticException {
        if (productionsMap.getFirsts("sentence").contains(currentToken.getTokenName())) {
            sentence();
            sentenceList();
        } else if (!productionsMap.getFollow("sentenceList").contains(currentToken.getTokenName())) {
            throw new SyntacticException(currentToken.getLexeme(), String.join(", ", productionsMap.getFollow("sentenceList")), analyzer.getLineNumber());
        }
    }

    private void sentence() throws SyntacticException {
        if (currentToken.getTokenName().equals("semicolon")) {
            match("semicolon");
        } else if (productionsMap.getFirsts("assignCall").contains(currentToken.getTokenName())) {
            assignCall();
            match("semicolon");
        } else if (productionsMap.getFirsts("localVar").contains(currentToken.getTokenName())) {
            localVar();
            match("semicolon");
        } else if (productionsMap.getFirsts("returnState").contains(currentToken.getTokenName())) {
            returnState();
            match("semicolon");
        } else if (productionsMap.getFirsts("ifState").contains(currentToken.getTokenName())) {
            ifState();
        } else if (productionsMap.getFirsts("whileState").contains(currentToken.getTokenName())) {
            whileState();
        } else if (productionsMap.getFirsts("block").contains(currentToken.getTokenName())) {
            block();
        } else {
            throw new SyntacticException(currentToken.getLexeme(), String.join(", ", productionsMap.getFirsts("sentence")), analyzer.getLineNumber());
        }
    }

    private void assignCall() throws SyntacticException {
        if (productionsMap.getFirsts("expression").contains(currentToken.getTokenName())) {
            expression();
        } else {
            throw new SyntacticException(currentToken.getLexeme(), String.join(", ", productionsMap.getFirsts("assignCall")), analyzer.getLineNumber());
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
        if (productionsMap.getFirsts("expression").contains(currentToken.getTokenName())) {
            expression();
        } else if (!productionsMap.getFollow("optionalExpression").contains(currentToken.getTokenName())) {
            throw new SyntacticException(currentToken.getLexeme(), String.join(", ", productionsMap.getFollow("optionalExpression")), analyzer.getLineNumber());
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
        } else if (!productionsMap.getFollow("elseSentence").contains(currentToken.getTokenName())) {
            throw new SyntacticException(currentToken.getLexeme(), String.join(", ", productionsMap.getFollow("elseSentence")), analyzer.getLineNumber());
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
        if (productionsMap.getFirsts("composedExpression").contains(currentToken.getTokenName())) {
            composedExpression();
            extraExpression();
        } else {
            throw new SyntacticException(currentToken.getLexeme(), String.join(", ", productionsMap.getFirsts("expression")), analyzer.getLineNumber());
        }
    }

    private void extraExpression() throws SyntacticException {
        if (productionsMap.getFirsts("assignOperator").contains(currentToken.getTokenName())) {
            assignOperator();
            composedExpression();
        } else if (!productionsMap.getFollow("extraExpression").contains(currentToken.getTokenName())) {
            throw new SyntacticException(currentToken.getLexeme(), String.join(", ", productionsMap.getFollow("extraExpression")), analyzer.getLineNumber());
        }
    }

    private void assignOperator() throws SyntacticException {
        match("equals");
    }

    private void composedExpression() throws SyntacticException {
        if (productionsMap.getFirsts("basicExpression").contains(currentToken.getTokenName())) {
            basicExpression();
            composedExpressionLeft();
        } else {
            throw new SyntacticException(currentToken.getLexeme(), String.join(", ", productionsMap.getFirsts("composedExpression")), analyzer.getLineNumber());
        }
    }

    private void composedExpressionLeft() throws SyntacticException {
        if (productionsMap.getFirsts("binaryOperator").contains(currentToken.getTokenName())) {
            binaryOperator();
            basicExpression();
            composedExpressionLeft();
        } else if (!productionsMap.getFollow("composedExpressionLeft").contains(currentToken.getTokenName())) {
            throw new SyntacticException(currentToken.getLexeme(), String.join(", ", productionsMap.getFollow("composedExpressionLeft")), analyzer.getLineNumber());
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
                    throw new SyntacticException(currentToken.getLexeme(), String.join(", ", productionsMap.getFirsts("binaryOperator")), analyzer.getLineNumber());
        }
    }

    private void basicExpression() throws SyntacticException {
        if (productionsMap.getFirsts("unaryOperator").contains(currentToken.getTokenName())) {
            unaryOperator();
            operand();
        } else if (productionsMap.getFirsts("operand").contains(currentToken.getTokenName())) {
            operand();
        } else {
            throw new SyntacticException(currentToken.getLexeme(), String.join(", ", productionsMap.getFirsts("basicExpression")), analyzer.getLineNumber());
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
                    throw new SyntacticException(currentToken.getLexeme(), String.join(", ", productionsMap.getFirsts("unaryOperator")), analyzer.getLineNumber());
        }
    }

    private void operand() throws SyntacticException {
        if (productionsMap.getFirsts("primitive").contains(currentToken.getTokenName())) {
            primitive();
        } else if (productionsMap.getFirsts("reference").contains(currentToken.getTokenName())) {
            reference();
        } else {
            throw new SyntacticException(currentToken.getLexeme(), String.join(", ", productionsMap.getFirsts("operand")), analyzer.getLineNumber());
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
                    throw new SyntacticException(currentToken.getLexeme(), String.join(", ", productionsMap.getFirsts("primitive")), analyzer.getLineNumber());
        }
    }

    private void reference() throws SyntacticException {
        if (productionsMap.getFirsts("primary").contains(currentToken.getTokenName())) {
            primary();
            chainReference();
        } else {
            throw new SyntacticException(currentToken.getLexeme(), String.join(", ", productionsMap.getFirsts("reference")), analyzer.getLineNumber());
        }
    }

    private void chainReference() throws SyntacticException {
        if (currentToken.getTokenName().equals("dot")) {
            match("dot");
            match("idMetVar");
            chainElement();
            chainReference();
        } else if (!productionsMap.getFollow("chainReference").contains(currentToken.getTokenName())) {
            throw new SyntacticException(currentToken.getLexeme(), String.join(", ", productionsMap.getFollow("chainReference")), analyzer.getLineNumber());
        }
    }

    private void primary() throws SyntacticException {
        if (currentToken.getTokenName().equals("pr_this")) {
            match("pr_this");
        } else if (currentToken.getTokenName().equals("stringLiteral")) {
            match("stringLiteral");
        } else if (productionsMap.getFirsts("varMethodAccess").contains(currentToken.getTokenName())) {
            varMethodAccess();
        } else if (productionsMap.getFirsts("constructorCall").contains(currentToken.getTokenName())) {
            constructorCall();
        } else if (productionsMap.getFirsts("staticCall").contains(currentToken.getTokenName())) {
            staticCall();
        } else if (productionsMap.getFirsts("parenthesisExpression").contains(currentToken.getTokenName())) {
            parenthesisExpression();
        } else {
            throw new SyntacticException(currentToken.getLexeme(), String.join(", ", productionsMap.getFirsts("primary")), analyzer.getLineNumber());
        }
    }

    private void varMethodAccess() throws SyntacticException {
        match("idMetVar");
        possibleArgs();
    }

    private void possibleArgs() throws SyntacticException {
        if (productionsMap.getFirsts("currentArgs").contains(currentToken.getTokenName())) {
            currentArgs();
        } else if (!productionsMap.getFollow("possibleArgs").contains(currentToken.getTokenName())) {
            throw new SyntacticException(currentToken.getLexeme(), String.join(", ", productionsMap.getFollow("possibleArgs")), analyzer.getLineNumber());
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
        if (productionsMap.getFirsts("expression").contains(currentToken.getTokenName())) {
            expression();
            expressionList();
        } else if (!productionsMap.getFollow("optionalExpressionList").contains(currentToken.getTokenName())) {
            throw new SyntacticException(currentToken.getLexeme(), String.join(", ", productionsMap.getFollow("optionalExpressionList")), analyzer.getLineNumber());
        }
    }

    private void expressionList() throws SyntacticException {
        if (currentToken.getTokenName().equals("comma")) {
            match("comma");
            expression();
            expressionList();
        } else if (!productionsMap.getFollow("expressionList").contains(currentToken.getTokenName())) {
            throw new SyntacticException(currentToken.getLexeme(), String.join(", ", productionsMap.getFollow("expressionList")), analyzer.getLineNumber());
        }
    }

    private void chainElement() throws SyntacticException {
        if (productionsMap.getFirsts("currentArgs").contains(currentToken.getTokenName())) {
            currentArgs();
        } else if (!productionsMap.getFollow("chainElement").contains(currentToken.getTokenName())) {
            throw new SyntacticException(currentToken.getLexeme(), String.join(", ", productionsMap.getFollow("chainElement")), analyzer.getLineNumber());
        }
    }
}