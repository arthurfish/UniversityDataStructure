//编写程序，实现顺序表、单链表、双链表、单循环链表和双循环链表的操作。
//（2）在（1）的基础上，编写程序，实现两个长整数的加、减和乘运算。

import java.util.function.Supplier;

public class Expr4_PList_java_ver1 {
    sealed interface List {
    }
    static final class Nil implements List {};
    static final class Cons implements List {
        public final Object head;
        public final List tail;
        public Cons(Object head, List tail) {
            this.head = head;
            this.tail = tail;
        }
    };
    static public List Cons(Object head, List tail) {
        return new Cons(head, tail);
    }
    static sealed interface Tailrec{
        public Done eval();
    }
    static final class Recursion implements Tailrec {
        public final Supplier<Tailrec> fn;
        public Recursion(Supplier<Tailrec> fn) {
            this.fn = fn;
        }

        @Override
        public Done eval(){
            Tailrec result = fn.get();
            while(true) {
                if(result instanceof Done){
                    return (Done)result;
                }else{
                    result = result.eval();
                }
            }
        }
    }
    static final class Done implements Tailrec {
        public final Object value;
        public Done(Object value) {this.value = value;}
        @Override
        public Done eval() {
            return this;
        }
    }

    static private Tailrec factorialRec(int n, int acc){
        if(n == 1) {
            return new Done(acc);
        }else{
            return new Recursion(()->factorialRec(n-1, n * acc));
        }
    }

    static int factorial(int n){
        return (int)factorialRec(n, 1).eval().value;
    }

    private static Tailrec reverseRec(List list, List accList){
        if(list instanceof Cons){
            var head = ((Cons)list).head;
            var tail = ((Cons)list).tail;
            return new Recursion(()->reverseRec(tail, Cons(head, accList)));
        }else{
            return new Done(accList);
        }
    }

    static Tailrec listToStringRec(List list, String accString){
        if(list instanceof Cons){
            var head = ((Cons)list).head;
            var tail = ((Cons)list).tail;
            return listToStringRec(tail, head.toString() + accString);
        }else{
            return new Done(accString);
        }
    }

    static String listToString(List list){
        return listToStringRec(list, "").eval().value.toString();
    }

    public static void main(String[] args) {
        var list = Cons(1, Cons(2, Cons(3, Cons(4, new Nil()))));
        System.out.println(listToString(list));
    }

}