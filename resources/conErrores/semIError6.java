///[Error:f|5]

class Overload {
    int f() { return 1; }
    int f(int x) { return x; } // MiniJava: no method overloading allowed within same class
}
