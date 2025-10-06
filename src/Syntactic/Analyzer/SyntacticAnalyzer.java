package Syntactic.Analyzer;

import Lexical.Analyzer.LexicalAnalyzer;
import Lexical.Analyzer.Token;
import Lexical.LexExceptions.LexicalException;
import Main.CompiException;
import Semantic.*;
import Semantic.Class;
import Syntactic.SynExceptions.SyntacticException;
import Main.MainSemantic;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SyntacticAnalyzer {
    private final LexicalAnalyzer analyzer;
    private Token currentToken;
    private final ProductionsMap productionsMap;

    public SyntacticAnalyzer(LexicalAnalyzer analyzer, ProductionsMap productionsMap) {
        this.analyzer = analyzer;
        this.productionsMap = productionsMap;
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

    public void startAnalysis() throws CompiException {
        try {
            currentToken = analyzer.getNextToken();
        } catch (LexicalException e) {
            e.printError(analyzer.getLine());
        }
        start();
    }

    private void start() throws CompiException {
        classesList();
        match("EOF");
    }

    private void classesList() throws CompiException {
        if (productionsMap.getFirsts("classState").contains(currentToken.getTokenName())) {
            classState();
            classesList();
        } else if (!productionsMap.getFollow("classesList").contains(currentToken.getTokenName())) {
            throw new SyntacticException(currentToken.getLexeme(), String.join(", ", productionsMap.getFollow("classesList")), analyzer.getLineNumber());
        }
    }

    private void classState() throws CompiException {
        if (currentToken.getTokenName().equals("pr_interface")){
            match("pr_interface"); //TODO aca va el logro
            match("idClase");
            optionalGenerics();
            optionalInheritanceInterface();
            match("openBrace");
            membersList();
            match("closeBrace");
        } else {
            Token modify = optionalModifier();
            match("pr_class");
            Token name = currentToken;
            match("idClase");
            MainSemantic.symbolTable.currentClass = new Class(name);
            Token generic = optionalGenerics();
            MainSemantic.symbolTable.currentClass.inheritance = optionalInheritance();
            MainSemantic.symbolTable.currentClass.modifierClass = modify;
            MainSemantic.symbolTable.currentClass.generics = generic;
            match("openBrace");
            membersList();
            match("closeBrace");
            MainSemantic.symbolTable.addClass(name.getLexeme(),MainSemantic.symbolTable.currentClass);
        }
    }

    private Token optionalGenerics() throws SyntacticException {
        Token genericType = null;
        if (productionsMap.getFirsts("optionalGenerics").contains(currentToken.getTokenName())) {
            match("less");
            genericType = currentToken;
            match("idClase");
            match("greater");
        } else if (!productionsMap.getFollow("optionalGenerics").contains(currentToken.getTokenName())) {
            throw new SyntacticException(currentToken.getLexeme(), String.join(", ", productionsMap.getFollow("optionalGenerics")), analyzer.getLineNumber());
        }
        return genericType;
    }

    private Token optionalModifier() throws SyntacticException {
        Token modifier = currentToken;
        switch (currentToken.getTokenName()) {
            case "pr_abstract" -> match("pr_abstract");
            case "pr_static" -> match("pr_static");
            case "pr_final" -> match("pr_final");
            default -> {
                if (!productionsMap.getFollow("optionalModifier").contains(currentToken.getTokenName())) {
                    throw new SyntacticException(currentToken.getLexeme(), String.join(", ", productionsMap.getFollow("optionalModifier")), analyzer.getLineNumber());
                }
            }
        }
        if (Objects.equals(modifier.getTokenName(), "pr_class")){
            modifier = null;
        }
        return modifier;
    }

    private void optionalInheritanceInterface() throws SyntacticException {
        if (productionsMap.getFirsts("optionalInheritanceInterface").contains(currentToken.getTokenName())) {
            match("pr_extends");
            match("idClase");
            optionalGenerics();
        } else if (!productionsMap.getFollow("optionalInheritanceInterface").contains(currentToken.getTokenName())) {
            throw new SyntacticException(currentToken.getLexeme(), String.join(", ", productionsMap.getFollow("optionalInheritance")), analyzer.getLineNumber());
        }
    }

    private Token optionalInheritance() throws SyntacticException {
        Token father=null;
        if (productionsMap.getFirsts("optionalInheritance").contains(currentToken.getTokenName())) {
            if (currentToken.getTokenName().equals("pr_extends")){
                match("pr_extends");
                father = currentToken;
                match("idClase");
                optionalGenerics();
            } else if (currentToken.getTokenName().equals("pr_implements")){
                match("pr_implements");
                father = currentToken;
                match("idClase");
                optionalGenerics();
            }
        } else if (!productionsMap.getFollow("optionalInheritance").contains(currentToken.getTokenName())) {
            throw new SyntacticException(currentToken.getLexeme(), String.join(", ", productionsMap.getFollow("optionalInheritance")), analyzer.getLineNumber());
        }
        return father;
    }

    private void membersList() throws CompiException {
        if (productionsMap.getFirsts("member").contains(currentToken.getTokenName())) {
            member();
            membersList();
        } else if (!productionsMap.getFollow("membersList").contains(currentToken.getTokenName())) {
            throw new SyntacticException(currentToken.getLexeme(), String.join(", ", productionsMap.getFollow("membersList")), analyzer.getLineNumber());
        }
    }

    private Token optionalMemberModifier() throws SyntacticException {
        Token modifier = currentToken;
        switch (currentToken.getTokenName()) {
            case "pr_abstract" -> match("pr_abstract");
            case "pr_static" -> match("pr_static");
            case "pr_final" -> match("pr_final");
            default -> throw new SyntacticException(currentToken.getLexeme(), String.join(", ", productionsMap.getFirsts("optionalMemberModifier")), analyzer.getLineNumber());
        }
        return modifier;
    }

    private void member() throws CompiException {
        if (productionsMap.getFirsts("constructor").contains(currentToken.getTokenName())) {
            constructor();
        } else if (productionsMap.getFirsts("optionalMemberModifier").contains(currentToken.getTokenName())) {
            Token mod = optionalMemberModifier();
            Type t = typeMethod();
            Token name = currentToken;
            match("idMetVar");
            MainSemantic.symbolTable.currentMethod = new Method(name);
            MainSemantic.symbolTable.currentMethod.returnType = t;
            MainSemantic.symbolTable.currentMethod.modifier = mod;
            List<Parameter> args = formalArgs();
            for (Parameter m : args){
                MainSemantic.symbolTable.currentMethod.addParam(m);
            }
            MainSemantic.symbolTable.currentMethod.block = optionalBlock();
            MainSemantic.symbolTable.currentClass.addMethod(MainSemantic.symbolTable.currentMethod);
        } else if (productionsMap.getFirsts("type").contains(currentToken.getTokenName())) {
            Type t = type();
            Token name = currentToken;
            match("idMetVar");
            varOrMethod(t,name);
        } else if (currentToken.getTokenName().equals("pr_void")) {
            match("pr_void");
            Token name = currentToken;
            match("idMetVar");
            MainSemantic.symbolTable.currentMethod = new Method(name);
            MainSemantic.symbolTable.currentMethod.returnType = null;
            List<Parameter> args = formalArgs();
            for (Parameter m : args){
                MainSemantic.symbolTable.currentMethod.addParam(m);
            }
            MainSemantic.symbolTable.currentMethod.block = optionalBlock();
            MainSemantic.symbolTable.currentClass.addMethod(MainSemantic.symbolTable.currentMethod);
        } else {
            throw new SyntacticException(currentToken.getLexeme(), String.join(", ", productionsMap.getFirsts("member")), analyzer.getLineNumber());
        }
    }

    private void varOrMethod(Type t, Token name) throws CompiException {
        if (productionsMap.getFirsts("memberMethod").contains(currentToken.getTokenName())) {
            memberMethod(t,name);
        } else if (productionsMap.getFirsts("optionalDeclaration").contains(currentToken.getTokenName())) {
            Attribute a = new Attribute(name);
            a.type = t;
            optionalDeclaration();
            match("semicolon");
            MainSemantic.symbolTable.currentClass.addAttribute(a);
        } else {
            throw new SyntacticException(currentToken.getLexeme(), String.join(", ", productionsMap.getFirsts("memberMethod")), analyzer.getLineNumber());
        }
    }

    private void optionalDeclaration() throws SyntacticException {
        if (currentToken.getTokenName().equals("equals")) {
            match("equals");
            composedExpression();
        } else if (!productionsMap.getFollow("optionalDeclaration").contains(currentToken.getTokenName())) {
            throw new SyntacticException(currentToken.getLexeme(), String.join(", ", productionsMap.getFollow("optionalFormalArgsList")), analyzer.getLineNumber());
        }
    }

    private void memberMethod(Type t , Token name) throws CompiException {
        if (productionsMap.getFirsts("formalArgs").contains(currentToken.getTokenName())) {
            Method m = new Method(name);
            m.returnType = t;
            MainSemantic.symbolTable.currentMethod = m;
            List<Parameter> args = formalArgs();
            for (Parameter p : args){
                MainSemantic.symbolTable.currentMethod.addParam(p);
            }
            optionalBlock();
            MainSemantic.symbolTable.currentClass.addMethod(MainSemantic.symbolTable.currentMethod);
        } else if (currentToken.getTokenName().equals("semicolon")) {
            Attribute a = new Attribute(name);
            a.type = t;
            match("semicolon");
            MainSemantic.symbolTable.currentClass.addAttribute(a);
        } else {
            throw new SyntacticException(currentToken.getLexeme(), String.join(", ", productionsMap.getFirsts("memberMethod")), analyzer.getLineNumber());
        }
    }

    private void constructor() throws CompiException {
        match("pr_public");
        Token nom = currentToken;
        match("idClase");
        MainSemantic.symbolTable.currentConstructor = new Constructor(nom);
        List<Parameter> args = formalArgs();
        for (Parameter m : args){
            MainSemantic.symbolTable.currentConstructor.addParam(m);
        }
        block();
        MainSemantic.symbolTable.currentClass.addConstructor(MainSemantic.symbolTable.currentConstructor);
    }

    private Type typeMethod() throws SyntacticException {
        Type t = null;
        if (productionsMap.getFirsts("type").contains(currentToken.getTokenName())) {
            t = type();
        } else if (currentToken.getTokenName().equals("pr_void")) {
            match("pr_void");
        } else {
            throw new SyntacticException(currentToken.getLexeme(), String.join(", ", productionsMap.getFirsts("typeMethod")), analyzer.getLineNumber());
        }
        return t;
    }

    private Type type() throws SyntacticException {
        Type t = null;
        Token name;
        if (productionsMap.getFirsts("primitiveType").contains(currentToken.getTokenName())) {
            name = currentToken;
            primitiveType();
            t = new PrimitiveType(name);
        } else if (currentToken.getTokenName().equals("idClase")) {
            name = currentToken;
            match("idClase");
            Token g = optionalGenerics();
            t = new ReferenceType(name,g);
        } else {
            throw new SyntacticException(currentToken.getLexeme(), String.join(", ", productionsMap.getFirsts("type")), analyzer.getLineNumber());
        }
        return t;
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

    private List<Parameter> formalArgs() throws SyntacticException {
        match("openParenthesis");
        List<Parameter> args = optionalFormalArgsList();
        match("closeParenthesis");
        return args;
    }

    private List<Parameter> optionalFormalArgsList() throws SyntacticException {
        List<Parameter> args = new ArrayList<>();
        if (productionsMap.getFirsts("formalArgsList").contains(currentToken.getTokenName())) {
            args = formalArgsList();
        } else if (!productionsMap.getFollow("optionalFormalArgsList").contains(currentToken.getTokenName())) {
            throw new SyntacticException(currentToken.getLexeme(), String.join(", ", productionsMap.getFollow("optionalFormalArgsList")), analyzer.getLineNumber());
        }
        return args;
    }

    private List<Parameter> formalArgsList() throws SyntacticException {
        List<Parameter> args = new ArrayList<>();
        if (productionsMap.getFirsts("formalArg").contains(currentToken.getTokenName())) {
            args.addLast(formalArg());
            args.addAll(formalArgsLeft());
        } else {
            throw new SyntacticException(currentToken.getLexeme(), String.join(", ", productionsMap.getFirsts("formalArgsList")), analyzer.getLineNumber());
        }
        return args;
    }

    private List<Parameter> formalArgsLeft() throws SyntacticException {
        List<Parameter> args = new ArrayList<>();
        if (currentToken.getTokenName().equals("comma")) {
            match("comma");
            args.addLast(formalArg());
            args.addAll(formalArgsLeft());
        } else if (!productionsMap.getFollow("formalArgsLeft").contains(currentToken.getTokenName())) {
            throw new SyntacticException(currentToken.getLexeme(), String.join(", ", productionsMap.getFollow("formalArgsLeft")), analyzer.getLineNumber());
        }
        return args;
    }

    private Parameter formalArg() throws SyntacticException {
        Parameter p = null;
        if (productionsMap.getFirsts("type").contains(currentToken.getTokenName())) {
            Type t = type();
            Token name  = currentToken;
            match("idMetVar");
            p = new Parameter(t,name);
        } else {
            throw new SyntacticException(currentToken.getLexeme(), String.join(", ", productionsMap.getFirsts("formalArg")), analyzer.getLineNumber());
        }
        return p;
    }

    private boolean optionalBlock() throws SyntacticException {
        if (productionsMap.getFirsts("block").contains(currentToken.getTokenName())) {
            block();
            return true;
        } else if (currentToken.getTokenName().equals("semicolon")){
            match("semicolon");
            return false;
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
        } else if (currentToken.getTokenName().equals("pr_for")) {
            forState();
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

    private void forState() throws SyntacticException {
        match("pr_for");
        match("openParenthesis");
        forExpression();
        match("closeParenthesis");
        sentence();
    }

    private void forExpression() throws SyntacticException {
        if (currentToken.getTokenName().equals("pr_var")) {
            match("pr_var");
            match("idMetVar");
            forDivision();
        } else if (productionsMap.getFirsts("expression").contains(currentToken.getTokenName())) {
            expression();
            basicFor();
        } else {
            throw new SyntacticException(currentToken.getLexeme(), String.join(", ", productionsMap.getFirsts("expression")), analyzer.getLineNumber());
        }
    }

    private void forDivision() throws SyntacticException {
        if (currentToken.getTokenName().equals("colon")){
            iteratorFor();
        } else if (currentToken.getTokenName().equals("equals")) {
            match("equals");
            composedExpression();
            basicFor();
        } else {
            throw new SyntacticException(currentToken.getLexeme(), String.join(", ", productionsMap.getFirsts("composedExpression")), analyzer.getLineNumber());
        }
    }

    private void basicFor() throws SyntacticException {
        match("semicolon");
        expression();
        match("semicolon");
        expression();
    }

    private void iteratorFor() throws SyntacticException {
        match("colon");
        expression();
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
        } else if (currentToken.getTokenName().equals("questionMark")) {
            match("questionMark");
            composedExpression();
            match("colon");
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
        } else if (currentToken.getTokenName().equals("questionMark")) {
            match("questionMark");
            composedExpression();
            match("colon");
            composedExpression();
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

    private void optionalGenericsConstructor() throws SyntacticException {
        if (productionsMap.getFirsts("optionalGenerics").contains(currentToken.getTokenName())) {
            match("less");
            optionalGenericsConstructorLeft();
        } else if (!productionsMap.getFollow("optionalGenericsConstructor").contains(currentToken.getTokenName())) {
            throw new SyntacticException(currentToken.getLexeme(), String.join(", ", productionsMap.getFollow("optionalGenericsConstructor")), analyzer.getLineNumber());
        }
    }

    private void optionalGenericsConstructorLeft() throws SyntacticException {
        if (currentToken.getTokenName().equals("idClase")) {
            match("idClase");
            match("greater");
        } else if (currentToken.getTokenName().equals("greater")) {
            match("greater");
        } else {
            throw new SyntacticException(currentToken.getLexeme(), "idClase, greater", analyzer.getLineNumber());
        }
    }

    private void constructorCall() throws SyntacticException {
        match("pr_new");
        match("idClase");
        optionalGenericsConstructor();
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