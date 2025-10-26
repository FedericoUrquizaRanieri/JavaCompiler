//[Error:this|5]
// Error: uso de 'this' en método estático.
class E6 {
    static int sm() {
        var t = this; // error: this not allowed in static method
        return 0;
    }
}
