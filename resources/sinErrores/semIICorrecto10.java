// Uso correcto de llamada estática a método que devuelve clase.
class C10 {
    static C10 make() { return new C10(); }
    int useStaticCtor() {
        var x = C10.make();
        var y = x;
        return 0;
    }
}
