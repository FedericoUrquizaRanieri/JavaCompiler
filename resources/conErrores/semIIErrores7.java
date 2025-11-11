//[Error:f|6]
// Error: llamada con número incorrecto de parámetros.
class E7 {
    int f() { return 1; }
    int g() {
        var r = f(1); // error: wrong number of parameters
        return r;
    }
}
