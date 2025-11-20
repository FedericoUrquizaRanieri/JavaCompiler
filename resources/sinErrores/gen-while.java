///10&exitosamente
class WhileLoop {
    int countTo10() {
        var i = 0;
        while (i < 10) {
            i = i + 1;
        }
        return i;
    }
}

class Init {
    static void main() {
        var wl = new WhileLoop();
        Object.debugPrint(wl.countTo10());
    }
}