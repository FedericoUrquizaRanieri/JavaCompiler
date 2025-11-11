// Test de subtipado simple (objeto asignado a variable de su superclase).
class Super { int a; }
class Sub extends Super {
    int get() { return a; }
}
class C14 {
    Super m() {
        var s = new Sub();
        return s;
    }
}
