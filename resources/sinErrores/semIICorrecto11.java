// Operadores lógicos compuestos correctamente tipados.
class C11 {
    boolean logical() {
        var a = true || false && (false || true);
        return a;
    }
}
