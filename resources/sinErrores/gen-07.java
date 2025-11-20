///22&exitosamente

class A{
    int x;
    void m1(int p1){
        Object.debugPrint(p1);
    }

    int m2(){
        m1(22);
    }
}

class Init{

    static void main()
    {
        var a = new A();
        a.m2();
    }
}