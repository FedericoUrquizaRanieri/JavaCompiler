// Combinación múltiple de if y while válidos.
class C22 {
    void m() {
        if (true) {
            while (false) {
                var x = 1;
            }
        } else {
            return;
        }
    }
}
