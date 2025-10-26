//[Error:noExiste|13]
// Error: llamada encadenada inválida, método inexistente en tipo retornado.
class A {
    B getB() {
        return new B();
    }
}
class B {
    int val;

    int test() {
        var a = new A();
        var n = a.getB().noExiste(); // método noExiste no existe en Breturn n;
    }
}


