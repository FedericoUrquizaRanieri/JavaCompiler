//[Error:&&|5]
// Error: operador '&&' usado entre enteros.
class E2 {
    boolean m() {
        var x = 5 && 3; // error: && requires booleans
        return x;
    }
}
