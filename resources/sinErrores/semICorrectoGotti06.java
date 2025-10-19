///[SinErrores]

abstract class A {
    void m1()
    {}

    abstract void m2();

}

abstract class B extends A{
    void m1()
    {}

    void m2(){}

}

class C extends B{
    void m2(){}
}

class Init {
    static void main()
    {  }
}