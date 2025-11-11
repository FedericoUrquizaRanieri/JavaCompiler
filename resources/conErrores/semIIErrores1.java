//[Error:+|5]
// Error: operador '+' usado entre boolean e int.
class E1 {
    int m() {
        var x = true + 1; // error: + requires ints
        return 0;
    }
}
