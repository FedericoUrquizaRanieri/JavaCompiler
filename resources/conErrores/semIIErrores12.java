//[Error:<|5]
// Error: operador relacional entre int y boolean.
class E12 {
    int m() {
        var b = (5 < true); // error: relational requires ints
        return 0;
    }
}
