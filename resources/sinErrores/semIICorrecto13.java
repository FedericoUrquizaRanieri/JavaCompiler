// Test de encadenamiento de métodos y acceso a atributos válidos.
class C13 {
    int x;
    C13 getSelf() { return this; }
    int getX() { return x; }
    int test() {
        var c = new C13();
        var r = c.getSelf().getX(); // encadenado válido
        return r;
    }
}
