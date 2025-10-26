// Verifica retorno y llamadas con parámetros correctos.
class C3 {
    int f(int x, boolean y) { return x + 1; }
    boolean g() {
        var t = f(2, true) > 0;
        return t;
    }
}
