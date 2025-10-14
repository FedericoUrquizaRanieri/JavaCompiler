///[SinErrores]

class A {
    int a;
    int f(int x) { return x + a; }
}
class B extends A {
    int b;
    int f(int x) { return x + b; }
}
