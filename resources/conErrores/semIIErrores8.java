//[Error:++|6]
// Error: '++' aplicado sobre tipo boolean.
class E8 {
    int m() {
        var x = true;
        var y = ++x; // error: ++ only for int
        return 0;
    }
}
