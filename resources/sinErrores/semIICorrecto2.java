// Uso de método estático y operadores lógicos válidos.
class C2 {
    static int sm(int p) { return p + 1; }
    static void main() {
        var v = sm(3);
        var ok = (v > 2) && true;
        return;
    }
}
