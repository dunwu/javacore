package io.github.dunwu.javacore.concurrent.chapter03;

public class FinalReferenceExample {
    final int[]                  intArray; //final是引用类型
    static FinalReferenceExample obj;

    public FinalReferenceExample() { //构造函数
        intArray = new int[1]; //1
        intArray[0] = 1; //2
    }

    public static void writerOne() { //写线程A执行
        obj = new FinalReferenceExample(); //3
    }

    public static void writerTwo() { //写线程B执行
        obj.intArray[0] = 2; //4
    }

    public static void reader() { //读线程C执行
        if (obj != null) { //5
            int temp1 = obj.intArray[0]; //6
        }
    }
}
