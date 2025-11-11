//[Error:+|6]
// Error: operador '+' entre clase e int.
class E10 {
    int m() {
        var a = new E10();
        var r = a + 1; // error: + requires ints (left is class)
        return 0;
    }
}
