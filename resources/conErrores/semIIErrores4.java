//[Error:=|6]
// Error: asignación de boolean a variable int.
class E4 {
    int m() {
        var x = 1;
        x = true; // error: assignment type mismatch (boolean to int)
        return x;
    }
}
