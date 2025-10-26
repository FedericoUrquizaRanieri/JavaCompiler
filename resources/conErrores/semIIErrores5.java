//[Error:<|6]
// Error: operador '<' aplicado entre char y int.
class E5 {
    boolean m() {
        var a = 'c';
        var b = a < 5; // error: < requires ints
        return b;
    }
}
