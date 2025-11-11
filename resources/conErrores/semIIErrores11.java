//[Error:==|6]
// Error: comparación '==' entre int y boolean.
class E11 {
    boolean m() {
        var a = 3;
        var b = (a == true); // equality between int and boolean - not conformant
        return b;
    }
}
