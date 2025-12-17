package Semantic.ST;

import Main.MainGen;

public class DefaultGenerator {
    public static void generateDefaults(){
        MainGen.symbolTable.instructionsList.add(".DATA");
        MainGen.symbolTable.instructionsList.add("lblVTObject: NOP");
        MainGen.symbolTable.instructionsList.add("");
        MainGen.symbolTable.instructionsList.add(".CODE");
        generateObjectMethod();
        MainGen.symbolTable.instructionsList.add(".DATA");
        MainGen.symbolTable.instructionsList.add("lblVTString: NOP");
        MainGen.symbolTable.instructionsList.add("");
        MainGen.symbolTable.instructionsList.add(".CODE");
        generateStringMethod();
        MainGen.symbolTable.instructionsList.add(".DATA");
        MainGen.symbolTable.instructionsList.add("lblVTSystem: NOP");
        MainGen.symbolTable.instructionsList.add("");
        MainGen.symbolTable.instructionsList.add(".CODE");
        generateSystemMethod();
    }

    private static void generateObjectMethod() {
        MainGen.symbolTable.instructionsList.add("lblConstructor@Object: LOADFP");
        MainGen.symbolTable.instructionsList.add("LOADSP");
        MainGen.symbolTable.instructionsList.add("STOREFP");
        MainGen.symbolTable.instructionsList.add("FMEM 0");
        MainGen.symbolTable.instructionsList.add("STOREFP");
        MainGen.symbolTable.instructionsList.add("RET "+ 1 );
        MainGen.symbolTable.instructionsList.add("");

        MainGen.symbolTable.instructionsList.add("lblMetdebugPrint@Object: LOADFP");
        MainGen.symbolTable.instructionsList.add("LOADSP");
        MainGen.symbolTable.instructionsList.add("STOREFP");
        MainGen.symbolTable.instructionsList.add("LOAD 3 ; Lee i");
        MainGen.symbolTable.instructionsList.add("IPRINT ; Imprime i");
        MainGen.symbolTable.instructionsList.add("STOREFP");
        MainGen.symbolTable.instructionsList.add("RET 1");
        MainGen.symbolTable.instructionsList.add("");
    }

    private static void generateSystemMethod() {
        MainGen.symbolTable.instructionsList.add("lblConstructor@System: LOADFP");
        MainGen.symbolTable.instructionsList.add("LOADSP");
        MainGen.symbolTable.instructionsList.add("STOREFP");
        MainGen.symbolTable.instructionsList.add("FMEM 0");
        MainGen.symbolTable.instructionsList.add("STOREFP");
        MainGen.symbolTable.instructionsList.add("RET "+ 1 );
        MainGen.symbolTable.instructionsList.add("");

        MainGen.symbolTable.instructionsList.add("lblMetread@System: LOADFP");
        MainGen.symbolTable.instructionsList.add("LOADSP");
        MainGen.symbolTable.instructionsList.add("STOREFP");
        MainGen.symbolTable.instructionsList.add("READ ; Lee tope de la pila");
        MainGen.symbolTable.instructionsList.add("PUSH 48 ; Por ASCII");
        MainGen.symbolTable.instructionsList.add("SUB");
        MainGen.symbolTable.instructionsList.add("STORE 3 ; Guarda en retorno el tope");
        MainGen.symbolTable.instructionsList.add("STOREFP");
        MainGen.symbolTable.instructionsList.add("RET 0");
        MainGen.symbolTable.instructionsList.add("");

        MainGen.symbolTable.instructionsList.add("lblMetprintB@System: LOADFP");
        MainGen.symbolTable.instructionsList.add("LOADSP");
        MainGen.symbolTable.instructionsList.add("STOREFP");
        MainGen.symbolTable.instructionsList.add("LOAD 3 ");
        MainGen.symbolTable.instructionsList.add("BPRINT ");
        MainGen.symbolTable.instructionsList.add("STOREFP");
        MainGen.symbolTable.instructionsList.add("RET 1");
        MainGen.symbolTable.instructionsList.add("");

        MainGen.symbolTable.instructionsList.add("lblMetprintC@System: LOADFP");
        MainGen.symbolTable.instructionsList.add("LOADSP");
        MainGen.symbolTable.instructionsList.add("STOREFP");
        MainGen.symbolTable.instructionsList.add("LOAD 3 ");
        MainGen.symbolTable.instructionsList.add("CPRINT ");
        MainGen.symbolTable.instructionsList.add("STOREFP");
        MainGen.symbolTable.instructionsList.add("RET 1");
        MainGen.symbolTable.instructionsList.add("");

        MainGen.symbolTable.instructionsList.add("lblMetprintI@System: LOADFP");
        MainGen.symbolTable.instructionsList.add("LOADSP");
        MainGen.symbolTable.instructionsList.add("STOREFP");
        MainGen.symbolTable.instructionsList.add("LOAD 3 ");
        MainGen.symbolTable.instructionsList.add("IPRINT ");
        MainGen.symbolTable.instructionsList.add("STOREFP");
        MainGen.symbolTable.instructionsList.add("RET 1");
        MainGen.symbolTable.instructionsList.add("");

        MainGen.symbolTable.instructionsList.add("lblMetprintS@System: LOADFP");
        MainGen.symbolTable.instructionsList.add("LOADSP");
        MainGen.symbolTable.instructionsList.add("STOREFP");
        MainGen.symbolTable.instructionsList.add("LOAD 3 ");
        MainGen.symbolTable.instructionsList.add("SPRINT ");
        MainGen.symbolTable.instructionsList.add("STOREFP");
        MainGen.symbolTable.instructionsList.add("RET 1");
        MainGen.symbolTable.instructionsList.add("");

        MainGen.symbolTable.instructionsList.add("lblMetprintln@System: LOADFP");
        MainGen.symbolTable.instructionsList.add("LOADSP");
        MainGen.symbolTable.instructionsList.add("STOREFP");
        MainGen.symbolTable.instructionsList.add("PRNLN");
        MainGen.symbolTable.instructionsList.add("STOREFP");
        MainGen.symbolTable.instructionsList.add("RET 0");
        MainGen.symbolTable.instructionsList.add("");

        MainGen.symbolTable.instructionsList.add("lblMetprintBln@System: LOADFP");
        MainGen.symbolTable.instructionsList.add("LOADSP");
        MainGen.symbolTable.instructionsList.add("STOREFP");
        MainGen.symbolTable.instructionsList.add("LOAD 3 ");
        MainGen.symbolTable.instructionsList.add("BPRINT ");
        MainGen.symbolTable.instructionsList.add("PRNLN");
        MainGen.symbolTable.instructionsList.add("STOREFP");
        MainGen.symbolTable.instructionsList.add("RET 1");
        MainGen.symbolTable.instructionsList.add("");

        MainGen.symbolTable.instructionsList.add("lblMetprintCln@System: LOADFP");
        MainGen.symbolTable.instructionsList.add("LOADSP");
        MainGen.symbolTable.instructionsList.add("STOREFP");
        MainGen.symbolTable.instructionsList.add("LOAD 3 ");
        MainGen.symbolTable.instructionsList.add("CPRINT ");
        MainGen.symbolTable.instructionsList.add("PRNLN");
        MainGen.symbolTable.instructionsList.add("STOREFP");
        MainGen.symbolTable.instructionsList.add("RET 1");
        MainGen.symbolTable.instructionsList.add("");

        MainGen.symbolTable.instructionsList.add("lblMetprintIln@System:LOADFP");
        MainGen.symbolTable.instructionsList.add("LOADSP");
        MainGen.symbolTable.instructionsList.add("STOREFP");
        MainGen.symbolTable.instructionsList.add("LOAD 3 ");
        MainGen.symbolTable.instructionsList.add("IPRINT ");
        MainGen.symbolTable.instructionsList.add("PRNLN");
        MainGen.symbolTable.instructionsList.add("STOREFP");
        MainGen.symbolTable.instructionsList.add("RET 1");
        MainGen.symbolTable.instructionsList.add("");

        MainGen.symbolTable.instructionsList.add("lblMetprintSln@System: LOADFP");
        MainGen.symbolTable.instructionsList.add("LOADSP");
        MainGen.symbolTable.instructionsList.add("STOREFP");
        MainGen.symbolTable.instructionsList.add("LOAD 3 ");
        MainGen.symbolTable.instructionsList.add("SPRINT ");
        MainGen.symbolTable.instructionsList.add("PRNLN");
        MainGen.symbolTable.instructionsList.add("STOREFP");
        MainGen.symbolTable.instructionsList.add("RET 1");
        MainGen.symbolTable.instructionsList.add("");
    }

    private static void generateStringMethod(){
        MainGen.symbolTable.instructionsList.add("lblConstructor@String: LOADFP");
        MainGen.symbolTable.instructionsList.add("LOADSP");
        MainGen.symbolTable.instructionsList.add("STOREFP");
        MainGen.symbolTable.instructionsList.add("FMEM 0");
        MainGen.symbolTable.instructionsList.add("STOREFP");
        MainGen.symbolTable.instructionsList.add("RET "+ 1 );
        MainGen.symbolTable.instructionsList.add("");
    }
}
