package Syntactic.Analyzer;

import Lexical.Analyzer.LexicalAnalyzer;
import Lexical.Analyzer.Token;
import Lexical.LexExceptions.LexicalException;
import Main.CompiException;
import Semantic.AST.Expressions.*;
import Semantic.AST.Expressions.References.*;
import Semantic.AST.Expressions.TypeNode.*;
import Semantic.AST.Sentences.*;
import Semantic.ST.*;
import Semantic.ST.Class;
import Syntactic.SynExceptions.SyntacticException;
import Main.MainSemantic;

import java.util.ArrayList;
import java.util.EmptyStackException;
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
            match("pr_interface");
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
            MainSemantic.symbolTable.currentClass.setInheritance(optionalInheritance());
            MainSemantic.symbolTable.currentClass.setModifierClass(modify);
            MainSemantic.symbolTable.currentClass.setGenerics(generic);
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
            MainSemantic.symbolTable.currentMethod.setReturnType(t);
            MainSemantic.symbolTable.currentMethod.setModifier(mod);
            List<Parameter> args = formalArgs();
            for (Parameter m : args){
                MainSemantic.symbolTable.currentMethod.addParam(m);
            }
            MainSemantic.symbolTable.currentMethod.setBlock(optionalBlock());
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
            MainSemantic.symbolTable.currentMethod.setReturnType(null);
            List<Parameter> args = formalArgs();
            for (Parameter m : args){
                MainSemantic.symbolTable.currentMethod.addParam(m);
            }
            MainSemantic.symbolTable.currentMethod.setBlock(optionalBlock());
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
            a.setType(t);
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
            m.setReturnType(t);
            MainSemantic.symbolTable.currentMethod = m;
            List<Parameter> args = formalArgs();
            for (Parameter p : args){
                MainSemantic.symbolTable.currentMethod.addParam(p);
            }
            MainSemantic.symbolTable.currentMethod.setBlock(optionalBlock());
            MainSemantic.symbolTable.currentClass.addMethod(MainSemantic.symbolTable.currentMethod);
        } else if (currentToken.getTokenName().equals("semicolon")) {
            Attribute a = new Attribute(name);
            a.setType(t);
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
        MainSemantic.symbolTable.currentConstructor.setBlock(block());
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
        Type t;
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
        Parameter p;
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

    private BlockNode optionalBlock() throws SyntacticException {
        if (productionsMap.getFirsts("block").contains(currentToken.getTokenName())) {
            return block();
        } else if (currentToken.getTokenName().equals("semicolon")){
            match("semicolon");
            return new NullBlockNode();
        } else {
            throw new SyntacticException(currentToken.getLexeme(), String.join(", ", productionsMap.getFirsts("optionalBlock")), analyzer.getLineNumber());
        }
    }

    private BlockNode block() throws SyntacticException {
        BlockNode b = new BlockNode();
        BlockNode container;
        try {
            container = MainSemantic.symbolTable.currentBlock.peek();
        } catch (EmptyStackException e){
            container = new NullBlockNode();
        }
        MainSemantic.symbolTable.currentBlock.push(b);
        for (LocalVarNode l : container.getLocalVarList()){
            b.addLocalVar(l);
        }
        match("openBrace");
        List<SentenceNode> sentences = sentenceList();
        for (SentenceNode s : sentences){
            b.addSentence(s);
            if (s instanceof LocalVarNode){
                b.addLocalVar(s);
            }
        }
        match("closeBrace");
        MainSemantic.symbolTable.currentBlock.pop();
        return b;
    }

    private List<SentenceNode> sentenceList() throws SyntacticException {
        List<SentenceNode> sent = new ArrayList<>();
        if (productionsMap.getFirsts("sentence").contains(currentToken.getTokenName())) {
            sent.addLast(sentence());
            sent.addAll(sentenceList());
        } else if (!productionsMap.getFollow("sentenceList").contains(currentToken.getTokenName())) {
            throw new SyntacticException(currentToken.getLexeme(), String.join(", ", productionsMap.getFollow("sentenceList")), analyzer.getLineNumber());
        }
        return sent;
    }

    private SentenceNode sentence() throws SyntacticException {
        SentenceNode sent = new EmptySentenceNode();
        if (currentToken.getTokenName().equals("semicolon")) {
            match("semicolon");
        } else if (productionsMap.getFirsts("assignCall").contains(currentToken.getTokenName())) {
            sent = assignCall();
            match("semicolon");
        } else if (productionsMap.getFirsts("localVar").contains(currentToken.getTokenName())) {
            sent = localVar();
            match("semicolon");
        } else if (productionsMap.getFirsts("returnState").contains(currentToken.getTokenName())) {
            sent = returnState();
            match("semicolon");
        } else if (productionsMap.getFirsts("ifState").contains(currentToken.getTokenName())) {
            sent = ifState();
        } else if (productionsMap.getFirsts("whileState").contains(currentToken.getTokenName())) {
            sent = whileState();
        } else if (productionsMap.getFirsts("block").contains(currentToken.getTokenName())) {
            sent = block();
        } else if (currentToken.getTokenName().equals("pr_for")) {
            forState(); //TODO completar por logro
        } else {
            throw new SyntacticException(currentToken.getLexeme(), String.join(", ", productionsMap.getFirsts("sentence")), analyzer.getLineNumber());
        }
        return sent;
    }

    private SentenceNode assignCall() throws SyntacticException {
        if (productionsMap.getFirsts("expression").contains(currentToken.getTokenName())) {
            ExpressionNode exp = expression();
            return new AssignCallSentNode(exp); //TODO revisar los tipos de las expresiones
        } else {
            throw new SyntacticException(currentToken.getLexeme(), String.join(", ", productionsMap.getFirsts("assignCall")), analyzer.getLineNumber());
        }
    }

    private SentenceNode localVar() throws SyntacticException {
        match("pr_var");
        match("idMetVar");
        match("equals");
        return new LocalVarNode(composedExpression());
    }

    private SentenceNode returnState() throws SyntacticException {
        match("pr_return");
        return new ReturnNode(optionalExpression(),MainSemantic.symbolTable.currentMethod.getReturnType());
    }

    private ExpressionNode optionalExpression() throws SyntacticException {
        if (productionsMap.getFirsts("expression").contains(currentToken.getTokenName())) {
            return expression();
        } else if (!productionsMap.getFollow("optionalExpression").contains(currentToken.getTokenName())) {
            throw new SyntacticException(currentToken.getLexeme(), String.join(", ", productionsMap.getFollow("optionalExpression")), analyzer.getLineNumber());
        }
        return new EmptyExpressionNode();
    }

    private IfNode ifState() throws SyntacticException {
        match("pr_if");
        match("openParenthesis");
        ExpressionNode e = expression();
        match("closeParenthesis");
        SentenceNode s = sentence();
        SentenceNode es = elseSentence();
        return new IfNode(e,s,es);
    }

    private SentenceNode elseSentence() throws SyntacticException {
        if (currentToken.getTokenName().equals("pr_else")) {
            match("pr_else");
            return sentence();
        } else if (!productionsMap.getFollow("elseSentence").contains(currentToken.getTokenName())) {
            throw new SyntacticException(currentToken.getLexeme(), String.join(", ", productionsMap.getFollow("elseSentence")), analyzer.getLineNumber());
        }
        return new EmptySentenceNode();
    }

    private SentenceNode whileState() throws SyntacticException {
        match("pr_while");
        match("openParenthesis");
        ExpressionNode e = expression();
        match("closeParenthesis");
        SentenceNode s = sentence();
        return new WhileNode(e,s);
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

    private ExpressionNode expression() throws SyntacticException {
        ExpressionNode c;
        if (productionsMap.getFirsts("composedExpression").contains(currentToken.getTokenName())) {
            c = composedExpression();
            c = extraExpression(c);
        } else {
            throw new SyntacticException(currentToken.getLexeme(), String.join(", ", productionsMap.getFirsts("expression")), analyzer.getLineNumber());
        }
        return c;
    }

    private ExpressionNode extraExpression(ExpressionNode e) throws SyntacticException {
        if (productionsMap.getFirsts("assignOperator").contains(currentToken.getTokenName())) {
            assignOperator();
            ExpressionNode c = composedExpression();
            return new AssignNodeExpNode(e,c);
        } else if (currentToken.getTokenName().equals("questionMark")) {
            match("questionMark");
            composedExpression();
            match("colon");
            composedExpression();
        } else if (!productionsMap.getFollow("extraExpression").contains(currentToken.getTokenName())) {
            throw new SyntacticException(currentToken.getLexeme(), String.join(", ", productionsMap.getFollow("extraExpression")), analyzer.getLineNumber());
        }
        return e;
    }

    private void assignOperator() throws SyntacticException {
        match("equals");
    }

    private ExpressionNode composedExpression() throws SyntacticException {
        ExpressionNode exp;
        ExpressionNode basicExp;
        if (productionsMap.getFirsts("basicExpression").contains(currentToken.getTokenName())) {
            basicExp = basicExpression();
            exp = composedExpressionLeft(basicExp);
            if (exp instanceof EmptyExpressionNode)
                return basicExp;
        } else {
            throw new SyntacticException(currentToken.getLexeme(), String.join(", ", productionsMap.getFirsts("composedExpression")), analyzer.getLineNumber());
        }
        return exp;
    }

    private ExpressionNode composedExpressionLeft(ExpressionNode e) throws SyntacticException {
        if (productionsMap.getFirsts("binaryOperator").contains(currentToken.getTokenName())) {
            Token op = binaryOperator();
            ExpressionNode leftSideExp = basicExpression();
            ExpressionNode rightSideExp = composedExpressionLeft(leftSideExp);
            if (rightSideExp instanceof EmptyExpressionNode){
                return new BinaryExpressionNode(e,op,leftSideExp);
            } else {
                return new BinaryExpressionNode(e,op,rightSideExp);
            }
        } else if (currentToken.getTokenName().equals("questionMark")) {
            match("questionMark");
            composedExpression();
            match("colon");
            composedExpression();
        } else if (!productionsMap.getFollow("composedExpressionLeft").contains(currentToken.getTokenName())) {
            throw new SyntacticException(currentToken.getLexeme(), String.join(", ", productionsMap.getFollow("composedExpressionLeft")), analyzer.getLineNumber());
        }
        return new EmptyExpressionNode();
    }

    private Token binaryOperator() throws SyntacticException {
        Token ct = currentToken;
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
        return ct;
    }

    private ExpressionNode basicExpression() throws SyntacticException {
        if (productionsMap.getFirsts("unaryOperator").contains(currentToken.getTokenName())) {
            Token op = unaryOperator();
            return new UnaryExpressionNode(operand(),op);
        } else if (productionsMap.getFirsts("operand").contains(currentToken.getTokenName())) {
            return operand();
        } else {
            throw new SyntacticException(currentToken.getLexeme(), String.join(", ", productionsMap.getFirsts("basicExpression")), analyzer.getLineNumber());
        }
    }

    private Token unaryOperator() throws SyntacticException {
        Token ct = currentToken;
        switch (currentToken.getTokenName()) {
            case "plus" -> match("plus");
            case "minus" -> match("minus");
            case "decrement" -> match("decrement");
            case "increment" -> match("increment");
            case "not" -> match("not");
            default ->
                    throw new SyntacticException(currentToken.getLexeme(), String.join(", ", productionsMap.getFirsts("unaryOperator")), analyzer.getLineNumber());
        }
        return ct;
    }

    private ExpressionNode operand() throws SyntacticException {
        if (productionsMap.getFirsts("primitive").contains(currentToken.getTokenName())) {
            return primitive();
        } else if (productionsMap.getFirsts("reference").contains(currentToken.getTokenName())) {
            return reference();
        } else {
            throw new SyntacticException(currentToken.getLexeme(), String.join(", ", productionsMap.getFirsts("operand")), analyzer.getLineNumber());
        }
    }

    private ExpressionNode primitive() throws SyntacticException {
        ExpressionNode literal;
        switch (currentToken.getTokenName()) {
            case "pr_true" -> {
                literal = new BooleanLiteralNode(new PrimitiveType(currentToken));
                match("pr_true");
            }
            case "pr_false" -> {
                literal = new BooleanLiteralNode(new PrimitiveType(currentToken));
                match("pr_false");
            }
            case "intLiteral" -> {
                literal = new IntLiteralNode(new PrimitiveType(currentToken));
                match("intLiteral");
            }
            case "charLiteral" -> {
                literal = new CharLiteralNode(new PrimitiveType(currentToken));
                match("charLiteral");
            }
            case "pr_null" -> {
                match("pr_null");
                literal = new NullTypeNode();
            }
            default ->
                    throw new SyntacticException(currentToken.getLexeme(), String.join(", ", productionsMap.getFirsts("primitive")), analyzer.getLineNumber());
        }
        return literal;
    }

    private ExpressionNode reference() throws SyntacticException {
        ReferenceNode e;
        List<ReferenceNode> chainedExp;
        if (productionsMap.getFirsts("primary").contains(currentToken.getTokenName())) {
            e = primary();
            chainedExp = chainReference();
            e.setChainedElements(chainedExp);
        } else {
            throw new SyntacticException(currentToken.getLexeme(), String.join(", ", productionsMap.getFirsts("reference")), analyzer.getLineNumber());
        }
        return e;
    }

    private List<ReferenceNode> chainReference() throws SyntacticException {
        List<ReferenceNode> retList = new ArrayList<>();
        ReferenceNode reference;
        if (currentToken.getTokenName().equals("dot")) {
            match("dot");
            Token ct = currentToken;
            match("idMetVar");
            List<ExpressionNode> params = chainElement();
            if (params.isEmpty()){
                reference = new AccessVarNode(ct,null);
            } else {
                reference = new AccessMethodNode(params,ct,null);
            }
            retList.add(reference);
            retList.addAll(chainReference());
        } else if (!productionsMap.getFollow("chainReference").contains(currentToken.getTokenName())) {
            throw new SyntacticException(currentToken.getLexeme(), String.join(", ", productionsMap.getFollow("chainReference")), analyzer.getLineNumber());
        }
        return retList;
    }

    private ReferenceNode primary() throws SyntacticException {
        ReferenceNode p;
        if (currentToken.getTokenName().equals("pr_this")) {
            p = new ThisCallNode(MainSemantic.symbolTable.currentClass);
            match("pr_this");
        } else if (currentToken.getTokenName().equals("stringLiteral")) {
            p = new StringLiteralNode(currentToken);
            match("stringLiteral");
        } else if (productionsMap.getFirsts("varMethodAccess").contains(currentToken.getTokenName())) {
            p = varMethodAccess();
        } else if (productionsMap.getFirsts("constructorCall").contains(currentToken.getTokenName())) {
            p = constructorCall();
        } else if (productionsMap.getFirsts("staticCall").contains(currentToken.getTokenName())) {
            p = staticCall();
        } else if (productionsMap.getFirsts("parenthesisExpression").contains(currentToken.getTokenName())) {
            p = parenthesisExpression();
        } else {
            throw new SyntacticException(currentToken.getLexeme(), String.join(", ", productionsMap.getFirsts("primary")), analyzer.getLineNumber());
        }
        return p;
    }

    private ReferenceNode varMethodAccess() throws SyntacticException {
        Token ct = currentToken;
        match("idMetVar");
        List<ExpressionNode> l = possibleArgs();
        if (l.isEmpty())
            return new AccessVarNode(ct,null);
        else
            return new AccessMethodNode(l,ct,null); //TODO revisar este null
    }

    private List<ExpressionNode> possibleArgs() throws SyntacticException {
        List<ExpressionNode> l = new ArrayList<>();
        if (productionsMap.getFirsts("currentArgs").contains(currentToken.getTokenName())) {
            l = currentArgs();
        } else if (!productionsMap.getFollow("possibleArgs").contains(currentToken.getTokenName())) {
            throw new SyntacticException(currentToken.getLexeme(), String.join(", ", productionsMap.getFollow("possibleArgs")), analyzer.getLineNumber());
        }
        return l;
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

    private ReferenceNode constructorCall() throws SyntacticException {
        match("pr_new");
        Token ct = currentToken;
        match("idClase");
        optionalGenericsConstructor();
        List<ExpressionNode> params = currentArgs();
        return new ConstructorCallNode(ct,params);
    }

    private ReferenceNode parenthesisExpression() throws SyntacticException {
        match("openParenthesis");
        ExpressionNode e = expression();
        match("closeParenthesis");
        return new ParamExpressionNode(e);
    }

    private ReferenceNode staticCall() throws SyntacticException {
        Token ctClass = currentToken;
        match("idClase");
        match("dot");
        Token ctMethod = currentToken;
        match("idMetVar");
        List<ExpressionNode> params = currentArgs();
        return new StaticMethodNode(ctClass,ctMethod,params);
    }

    private List<ExpressionNode> currentArgs() throws SyntacticException {
        match("openParenthesis");
        List<ExpressionNode> l = optionalExpressionList();
        match("closeParenthesis");
        return l;
    }

    private List<ExpressionNode> optionalExpressionList() throws SyntacticException {
        List<ExpressionNode> retList = new ArrayList<>();
        if (productionsMap.getFirsts("expression").contains(currentToken.getTokenName())) {
            retList.add(expression());
            retList.addAll(expressionList());
        } else if (!productionsMap.getFollow("optionalExpressionList").contains(currentToken.getTokenName())) {
            throw new SyntacticException(currentToken.getLexeme(), String.join(", ", productionsMap.getFollow("optionalExpressionList")), analyzer.getLineNumber());
        }
        return retList;
    }

    private List<ExpressionNode> expressionList() throws SyntacticException {
        List<ExpressionNode> retList = new ArrayList<>();
        if (currentToken.getTokenName().equals("comma")) {
            match("comma");
            retList.add(expression());
            retList.addAll(expressionList());
        } else if (!productionsMap.getFollow("expressionList").contains(currentToken.getTokenName())) {
            throw new SyntacticException(currentToken.getLexeme(), String.join(", ", productionsMap.getFollow("expressionList")), analyzer.getLineNumber());
        }
        return retList;
    }

    private List<ExpressionNode> chainElement() throws SyntacticException {
        List<ExpressionNode> retList = new ArrayList<>();
        if (productionsMap.getFirsts("currentArgs").contains(currentToken.getTokenName())) {
            retList = currentArgs();
        } else if (!productionsMap.getFollow("chainElement").contains(currentToken.getTokenName())) {
            throw new SyntacticException(currentToken.getLexeme(), String.join(", ", productionsMap.getFollow("chainElement")), analyzer.getLineNumber());
        }
        return retList;
    }
}