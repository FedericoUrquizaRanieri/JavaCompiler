//[Error:-|6]
// Error: operador '-' no definido para strings.
class E9 {
    int m() {
        var s = "hi";
        var z = s - "a"; // error: - not defined for strings
        return 0;
    }
}
