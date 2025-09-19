///[SinErrores]
class ForClassicNoBlock {
    void f() {
        for (i = 0; i < 10; i = i + 1) i = i + 2;
    }
}