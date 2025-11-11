//[Error:this|5]
// Error: uso de 'this' en método estático.
class E22 {
    static void m() {
        var x = this;
    }
}
