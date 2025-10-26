// Comparación entre objetos permitida con '=='.
class C7 {
    String s() {
        return "ok";
    }
    boolean eqCheck() {
        var a = new C7();
        var b = new C7();
        // equality allowed for class types (object references)
        var eq = a == b;
        return eq;
    }
}
