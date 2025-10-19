///[Error:m1|12]
abstract class B{
    abstract void m1();
}

class A extends B{
    final void m1(){}

}

class C extends A{
    void m1(){}
}

class Init{
    static void main()
    { }
}



