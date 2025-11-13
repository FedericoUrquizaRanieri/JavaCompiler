package Semantic.ST;

import Lexical.Analyzer.Token;
import Semantic.AST.Sentences.BlockNode;
import Semantic.AST.Sentences.NullBlockNode;
import Semantic.SemExceptions.SemanticException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SymbolTable {
    public HashMap<String,Class> classes;
    public Method currentMethod;
    public Class currentClass;
    public Constructor currentConstructor;
    public BlockNode currentBlock;
    public List<String> instructionsList;

    public SymbolTable(){
        classes = new HashMap<>();
        currentBlock = new NullBlockNode();
        putPredefinedClasses();
        instructionsList = new ArrayList<>();
    }

    public void checkStatements() throws SemanticException{
        for (Class c : classes.values()) {
            c.checkStatements();
        }
    }

    public void consolidate() throws SemanticException{
        for (Class c : classes.values()) {
            if (c.isNotConsolidated())
                c.consolidate();
        }
    }

    public void checkSentences() throws SemanticException{
        for (Class c : classes.values()) {
            c.checkSentences();
        }
    }

    public void setOffsets(){

    }

    public void generateCode(){
        initCode();
        heapCodeGenerator();
        for (Class c: classes.values()){
            c.generateCode();
        }
    }

    public void initCode(){
        instructionsList.add(".CODE");
        instructionsList.add("PUSH simple_heap_init");
        instructionsList.add("CALL");

        instructionsList.add("PUSH main");
        instructionsList.add("CALL");
        instructionsList.add("HALT");
    }

    public void heapCodeGenerator(){
        instructionsList.add("simple_heap_init:");
        instructionsList.add("RET 0");

        instructionsList.add("simple_malloc:");
        instructionsList.add("LOADFP");
        instructionsList.add("LOADSP");
        instructionsList.add("STOREFP");
        instructionsList.add("LOADHL");
        instructionsList.add("DUP");
        instructionsList.add("PUSH 1");
        instructionsList.add("ADD");
        instructionsList.add("STORE 4");
        instructionsList.add("LOAD 3");
        instructionsList.add("ADD");
        instructionsList.add("STOREHL");
        instructionsList.add("STOREFP");
        instructionsList.add("RET 1");
    }

    public void addClass(String name, Class classElement) throws SemanticException {
        if(classes.putIfAbsent(name,classElement)!=null)
            throw new SemanticException(name,"Se intento agregar una clase repetida llamada ",classElement.getClassToken().getLine());
    }

    public Class existsClass(Token className){
        if (classes.get(className.getLexeme())!=null){
            return classes.get(className.getLexeme());
        }
        return null;
    }

    private void putPredefinedClasses(){
        Class object = new Class(new Token("idClase","Object",0));
        classes.put("Object",object);
        Class string = new Class(new Token("idClase","String",0));
        classes.put("String",string);
        Class system = new Class(new Token("idClase","System",0));
        classes.put("System",system);

        Method debugPrint = new Method(new Token("idMetVar","debugPrint",0));
        debugPrint.setBlock(new NullBlockNode());
        debugPrint.setReturnType(null);
        debugPrint.setModifier(new Token("pr_static","static",0));
        debugPrint.getParameters().put("i",new Parameter(new PrimitiveType(new Token("pr_int","int",0)),new Token("idMetVar","i",0)));
        object.getMethods().put("debugPrint",debugPrint);

        Method read = new Method(new Token("idMetVar","read",0));
        read.setBlock(new NullBlockNode());
        read.setReturnType(new PrimitiveType(new Token("pr_int","int",0)));
        read.setModifier(new Token("pr_static","static",0));
        system.getMethods().put("read",read);

        Method printB = new Method(new Token("idMetVar","printB",0));
        printB.setBlock(new NullBlockNode());
        printB.setReturnType(null);
        printB.setModifier(new Token("pr_static","static",0));
        printB.getParameters().put("b",new Parameter(new PrimitiveType(new Token("pr_boolean","boolean",0)),new Token("idMetVar","b",0)));
        system.getMethods().put("printB",printB);

        Method printC = new Method(new Token("idMetVar","printC",0));
        printC.setBlock(new NullBlockNode());
        printC.setReturnType(null);
        printC.setModifier(new Token("pr_static","static",0));
        printC.getParameters().put("c",new Parameter(new PrimitiveType(new Token("pr_char","char",0)),new Token("idMetVar","c",0)));
        system.getMethods().put("printC",printC);

        Method printI = new Method(new Token("idMetVar","printI",0));
        printI.setBlock(new NullBlockNode());
        printI.setReturnType(null);
        printI.setModifier(new Token("pr_static","static",0));
        printI.getParameters().put("i",new Parameter(new PrimitiveType(new Token("pr_int","int",0)),new Token("idMetVar","i",0)));
        system.getMethods().put("printI",printI);

        Method printS = new Method(new Token("idMetVar","printS",0));
        printS.setBlock(new NullBlockNode());
        printS.setReturnType(null);
        printS.setModifier(new Token("pr_static","static",0));
        printS.getParameters().put("s",new Parameter(new PrimitiveType(new Token("idClase","String",0)),new Token("idMetVar","s",0)));
        system.getMethods().put("printS",printS);

        Method println = new Method(new Token("idMetVar","println",0));
        println.setBlock(new NullBlockNode());
        println.setReturnType(null);
        println.setModifier(new Token("pr_static","static",0));
        system.getMethods().put("println",println);

        Method printBln = new Method(new Token("idMetVar","printBln",0));
        printBln.setBlock(new NullBlockNode());
        printBln.setReturnType(null);
        printBln.setModifier(new Token("pr_static","static",0));
        printBln.getParameters().put("b",new Parameter(new PrimitiveType(new Token("pr_boolean","boolean",0)),new Token("idMetVar","b",0)));
        system.getMethods().put("printBln",printBln);

        Method printCln = new Method(new Token("idMetVar","printCln",0));
        printCln.setBlock(new NullBlockNode());
        printCln.setReturnType(null);
        printCln.setModifier(new Token("pr_static","static",0));
        printCln.getParameters().put("c",new Parameter(new PrimitiveType(new Token("pr_char","char",0)),new Token("idMetVar","c",0)));
        system.getMethods().put("printCln",printCln);

        Method printIln = new Method(new Token("idMetVar","printIln",0));
        printIln.setBlock(new NullBlockNode());
        printIln.setReturnType(null);
        printIln.setModifier(new Token("pr_static","static",0));
        printIln.getParameters().put("i",new Parameter(new PrimitiveType(new Token("pr_int","int",0)),new Token("idMetVar","i",0)));
        system.getMethods().put("printIln",printIln);

        Method printSln = new Method(new Token("idMetVar","printSln",0));
        printSln.setBlock(new NullBlockNode());
        printSln.setReturnType(null);
        printSln.setModifier(new Token("pr_static","static",0));
        printSln.getParameters().put("s",new Parameter(new PrimitiveType(new Token("idClase","String",0)),new Token("idMetVar","s",0)));
        system.getMethods().put("printSln",printSln);
    }
}