// Llamadas encadenadas válidas con 'new' y suma posterior.
class C6 {
    int sum(int a, int b) { return a + b; }
    int callChain() {
        var c = new C6().sum(1,2) + 3;
        return c;
    }
}
