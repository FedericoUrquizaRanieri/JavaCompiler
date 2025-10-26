// Uso de operadores unarios válidos (+, -, ++).
class C8 {
    int inc(int x) { return ++x; } // ++ on int allowed
    int test() {
        var a = 1;
        var b = +a + -a;
        return b;
    }
}
