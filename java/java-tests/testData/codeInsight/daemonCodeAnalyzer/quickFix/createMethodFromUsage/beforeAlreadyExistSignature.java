// "Create method 'f'" "false"
public class Foo {
    void foo(Bar f) {
        f.<caret>f(2);
    }
}
class Bar {
    private void f(int i){}
    public void f(String s){}
}
