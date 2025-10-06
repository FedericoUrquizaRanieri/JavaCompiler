package Semantic;

import Lexical.Analyzer.Token;
import Semantic.SemExceptions.SemanticException;

import java.util.HashMap;

public class SymbolTable {
    public HashMap<String,Class> classes;
    public Method currentMethod;
    public Class currentClass;
    public Constructor currentConstructor;

    public SymbolTable(){
        classes = new HashMap<>();
        Class object = new Class(new Token("idClase","Object",0));
        classes.put("Object",object);
        Class string = new Class(new Token("idClase","String",0));
        classes.put("String",string);
        Class system = new Class(new Token("idClase","System",0));
        classes.put("System",system);

        Method debugPrint = new Method(new Token("idMetVar","debugPrint",0));
        debugPrint.returnType = null;
        debugPrint.modifier = new Token("pr_static","static",0);
        debugPrint.parameters.put("i",new Parameter(new PrimitiveType(new Token("pr_int","int",0)),new Token("idMetVar","i",0)));
        object.methods.put("debugPrint",debugPrint);

        Method read = new Method(new Token("idMetVar","read",0));
        read.returnType = new PrimitiveType(new Token("pr_int","int",0));
        read.modifier = new Token("pr_static","static",0);
        system.methods.put("read",read);

        Method printB = new Method(new Token("idMetVar","printB",0));
        printB.returnType = null;
        printB.modifier = new Token("pr_static","static",0);
        printB.parameters.put("b",new Parameter(new PrimitiveType(new Token("pr_boolean","boolean",0)),new Token("idMetVar","b",0)));
        system.methods.put("printB",printB);

        Method printC = new Method(new Token("idMetVar","printC",0));
        printC.returnType = null;
        printC.modifier = new Token("pr_static","static",0);
        printC.parameters.put("c",new Parameter(new PrimitiveType(new Token("pr_char","char",0)),new Token("idMetVar","c",0)));
        system.methods.put("printC",printC);

        Method printI = new Method(new Token("idMetVar","printI",0));
        printI.returnType = null;
        printI.modifier = new Token("pr_static","static",0);
        printI.parameters.put("i",new Parameter(new PrimitiveType(new Token("pr_int","int",0)),new Token("idMetVar","i",0)));
        system.methods.put("printI",printI);

        Method printS = new Method(new Token("idMetVar","printS",0));
        printS.returnType = null;
        printS.modifier = new Token("pr_static","static",0);
        printS.parameters.put("s",new Parameter(new PrimitiveType(new Token("idClase","String",0)),new Token("idMetVar","s",0)));
        system.methods.put("printS",printS);

        Method println = new Method(new Token("idMetVar","println",0));
        println.returnType = null;
        println.modifier = new Token("pr_static","static",0);
        system.methods.put("println",println);

        Method printBln = new Method(new Token("idMetVar","printBln",0));
        printBln.returnType = null;
        printBln.modifier = new Token("pr_static","static",0);
        printBln.parameters.put("b",new Parameter(new PrimitiveType(new Token("pr_boolean","boolean",0)),new Token("idMetVar","b",0)));
        system.methods.put("printBln",printBln);

        Method printCln = new Method(new Token("idMetVar","printCln",0));
        printCln.returnType = null;
        printCln.modifier = new Token("pr_static","static",0);
        printCln.parameters.put("c",new Parameter(new PrimitiveType(new Token("pr_char","char",0)),new Token("idMetVar","c",0)));
        system.methods.put("printCln",printCln);

        Method printIln = new Method(new Token("idMetVar","printIln",0));
        printIln.returnType = null;
        printIln.modifier = new Token("pr_static","static",0);
        printIln.parameters.put("i",new Parameter(new PrimitiveType(new Token("pr_int","int",0)),new Token("idMetVar","i",0)));
        system.methods.put("printIln",printI);

        Method printSln = new Method(new Token("idMetVar","printSln",0));
        printSln.returnType = null;
        printSln.modifier = new Token("pr_static","static",0);
        printSln.parameters.put("s",new Parameter(new PrimitiveType(new Token("idClase","String",0)),new Token("idMetVar","s",0)));
        system.methods.put("printSln",printSln);
    }

    /*
        TODO aca agregar lo siguiente:
        revisar si existe herencia circular                                                                             DONE
        clases abstractas pueden heredar metodos abstractos                                                             DONE CREO
        no tener metodos abstractos                                                                                     DONE
        este tudo correctamente declarado                                                                               DONE
        revisar que una variable de tipo clase exista                                                                   DONE
    */
    public void checkStatements() throws SemanticException{
        for (Class c : classes.values()) {
            c.checkStatements();
        }
    }
    /*
        TODO aca agregar lo siguiente:
        pasar metodos de clase padre a esta clase
        pasar atributos de clase padre a esta clase
        TUDO HEREDA DE OBJECT
        revisar que metodos final no se reescriban
        revisar si se implementan metodos abstactos de padre y si la redefinicion esta bien (?)
        revisar que no se reescriba un atributo
        si no tiene constructor asignarle uno vacio
     */
    public void consolidate() throws SemanticException{
        for (Class c : classes.values()) {
            c.consolidate();
        }
    }

    public void addClass(String name, Class classElement) throws SemanticException {
        if(classes.putIfAbsent(name,classElement)!=null)
            throw new SemanticException(name,"Se intento agregar una clase repetida llamada ",classElement.classToken.getLine());
    }

    public Class existsClass(Token className){
        if (classes.get(className.getLexeme())!=null){
            return classes.get(className.getLexeme());
        }
        return null;
    }
}