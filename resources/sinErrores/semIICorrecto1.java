// Test básico de expresiones aritméticas, booleanas y de objetos.
class C1 {
    int a;
    int m() {
        var x = 5;
        var y = (x + 2) * 3;
        var b = true && false;
        var s = "hola";
        var obj = new C1();
        var r = obj.m();
        return y;
    }
}
