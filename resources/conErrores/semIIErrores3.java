//[Error:a|5]
// Error: inicialización de var con 'null'.
class E3 {
    int m() {
        var a = null; // error: var initialization with null is disallowed (null special)
        return 0;
    }
}
