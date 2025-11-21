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
    public int stringMark;
    public int conditionalsMark;
    public String mainLabel;

    public SymbolTable(){
        classes = new HashMap<>();
        currentBlock = new NullBlockNode();
        putPredefinedClasses();
        instructionsList = new ArrayList<>();
        stringMark = 0;
        conditionalsMark = 0;
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
        for (Class c : classes.values()) {
            c.setAttributeOffsets();
        }
        for (Class c : classes.values()) {
            c.setConstructorOffset();
        }
        for (Class c : classes.values()) {
            if(c.getMethods().get("main") != null){
                mainLabel = "lblMetmain@"+c.getClassName();
                System.out.println(mainLabel);
            }
            c.setMethodOffsets();
        }
    }

    public void generateCode(){
        initCode();
        heapCodeGenerator();
        generateDefaultClasses();
        for (Class c: classes.values()){
            if (!c.getClassName().equals("Object") && !c.getClassName().equals("System") && !c.getClassName().equals("String"))
                c.generateCode();
        }
    }

    public void initCode(){
        instructionsList.add(".CODE");
        instructionsList.add("PUSH simple_heap_init");
        instructionsList.add("CALL");
        instructionsList.add("PUSH "+mainLabel);
        instructionsList.add("CALL");
        instructionsList.add("HALT");
        instructionsList.add("");
    }

    public void heapCodeGenerator(){
        instructionsList.add("simple_heap_init: RET 0 ; Retorna inmediatamente");
        instructionsList.add("");
        instructionsList.add("simple_malloc: LOADFP     ; Inicializacion unidad");
        instructionsList.add("LOADSP");
        instructionsList.add("STOREFP ; Finaliza inicializacion del RA");
        instructionsList.add("LOADHL ; hl");
        instructionsList.add("DUP ; hl");
        instructionsList.add("PUSH 1 ; 1");
        instructionsList.add("ADD ; hl+1");
        instructionsList.add("STORE 4 ; Guarda el resultado (un puntero a la primer celda de la region de memoria)");
        instructionsList.add("LOAD 3 ; Carga la cantidad de celdas a alojar (parametro que debe ser positivo)");
        instructionsList.add("ADD");
        instructionsList.add("STOREHL ; Mueve el heap limit (hl). Expande el heap");
        instructionsList.add("STOREFP");
        instructionsList.add("RET 1     ; Retorna eliminando el parametro");
        instructionsList.add("");
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
        object.attributesNumbered();
        object.methodsNumbered();
        Class string = new Class(new Token("idClase","String",0));
        classes.put("String",string);
        string.attributesNumbered();
        string.methodsNumbered();
        Class system = new Class(new Token("idClase","System",0));
        classes.put("System",system);
        system.attributesNumbered();
        system.methodsNumbered();

        Method debugPrint = new Method(new Token("idMetVar","debugPrint",0),object);
        debugPrint.setBlock(new NullBlockNode());
        debugPrint.setReturnType(null);
        debugPrint.setModifier(new Token("pr_static","static",0));
        debugPrint.getParameters().put("i",new Parameter(new PrimitiveType(new Token("pr_int","int",0)),new Token("idMetVar","i",0)));
        object.getMethods().put("debugPrint",debugPrint);

        Method read = new Method(new Token("idMetVar","read",0),system);
        read.setBlock(new NullBlockNode());
        read.setReturnType(new PrimitiveType(new Token("pr_int","int",0)));
        read.setModifier(new Token("pr_static","static",0));
        system.getMethods().put("read",read);

        Method printB = new Method(new Token("idMetVar","printB",0),system);
        printB.setBlock(new NullBlockNode());
        printB.setReturnType(null);
        printB.setModifier(new Token("pr_static","static",0));
        printB.getParameters().put("b",new Parameter(new PrimitiveType(new Token("pr_boolean","boolean",0)),new Token("idMetVar","b",0)));
        system.getMethods().put("printB",printB);

        Method printC = new Method(new Token("idMetVar","printC",0),system);
        printC.setBlock(new NullBlockNode());
        printC.setReturnType(null);
        printC.setModifier(new Token("pr_static","static",0));
        printC.getParameters().put("c",new Parameter(new PrimitiveType(new Token("pr_char","char",0)),new Token("idMetVar","c",0)));
        system.getMethods().put("printC",printC);

        Method printI = new Method(new Token("idMetVar","printI",0),system);
        printI.setBlock(new NullBlockNode());
        printI.setReturnType(null);
        printI.setModifier(new Token("pr_static","static",0));
        printI.getParameters().put("i",new Parameter(new PrimitiveType(new Token("pr_int","int",0)),new Token("idMetVar","i",0)));
        system.getMethods().put("printI",printI);

        Method printS = new Method(new Token("idMetVar","printS",0),system);
        printS.setBlock(new NullBlockNode());
        printS.setReturnType(null);
        printS.setModifier(new Token("pr_static","static",0));
        printS.getParameters().put("s",new Parameter(new PrimitiveType(new Token("idClase","String",0)),new Token("idMetVar","s",0)));
        system.getMethods().put("printS",printS);

        Method println = new Method(new Token("idMetVar","println",0),system);
        println.setBlock(new NullBlockNode());
        println.setReturnType(null);
        println.setModifier(new Token("pr_static","static",0));
        system.getMethods().put("println",println);

        Method printBln = new Method(new Token("idMetVar","printBln",0),system);
        printBln.setBlock(new NullBlockNode());
        printBln.setReturnType(null);
        printBln.setModifier(new Token("pr_static","static",0));
        printBln.getParameters().put("b",new Parameter(new PrimitiveType(new Token("pr_boolean","boolean",0)),new Token("idMetVar","b",0)));
        system.getMethods().put("printBln",printBln);

        Method printCln = new Method(new Token("idMetVar","printCln",0),system);
        printCln.setBlock(new NullBlockNode());
        printCln.setReturnType(null);
        printCln.setModifier(new Token("pr_static","static",0));
        printCln.getParameters().put("c",new Parameter(new PrimitiveType(new Token("pr_char","char",0)),new Token("idMetVar","c",0)));
        system.getMethods().put("printCln",printCln);

        Method printIln = new Method(new Token("idMetVar","printIln",0),system);
        printIln.setBlock(new NullBlockNode());
        printIln.setReturnType(null);
        printIln.setModifier(new Token("pr_static","static",0));
        printIln.getParameters().put("i",new Parameter(new PrimitiveType(new Token("pr_int","int",0)),new Token("idMetVar","i",0)));
        system.getMethods().put("printIln",printIln);

        Method printSln = new Method(new Token("idMetVar","printSln",0),system);
        printSln.setBlock(new NullBlockNode());
        printSln.setReturnType(null);
        printSln.setModifier(new Token("pr_static","static",0));
        printSln.getParameters().put("s",new Parameter(new PrimitiveType(new Token("idClase","String",0)),new Token("idMetVar","s",0)));
        system.getMethods().put("printSln",printSln);
    }

    public void generateDefaultClasses(){
        DefaultGenerator.generateDefaults();
    }

    public int getStringMark(){
        return stringMark++;
    }

    public int getConditionalsMark(){
        return stringMark++;
    }
}