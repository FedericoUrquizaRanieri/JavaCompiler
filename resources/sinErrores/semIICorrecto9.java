// Return vacío en método void y llamadas sin retorno.
class C9 {
    void v() { return; }
    int caller() {
        v();
        var x = 0;
        return x;
    }
}
