package io.github.dunwu.javacore.concurrent.atomic;

import java.util.concurrent.atomic.AtomicReference;

/**
 * {@link java.util.concurrent.atomic.AtomicReference} 的常用 API 示例
 * <p>
 * 两个线程分别用三种方式修改引用：
 * <ul>
 *     <li>{@code compareAndSet(expect, update)}：仅当当前值等于 expect 时才更新，是原子的</li>
 *     <li>{@code getAndSet(update)}：无条件原子地设新值并返回旧值</li>
 *     <li>{@code lazySet(update)}：最终可见但不保证立即可见（内部用 StoreStore 屏障而非 StoreLoad），
 *     因此其他线程可能短时间内仍读到旧值，仅适用于不影响正确性的统计类场景</li>
 * </ul>
 * <p>
 * 另一个关键认识：{@code AtomicReference} 只保证<b>引用本身</b>的原子替换，并不会拷贝对象。
 * 因此用 {@code person.setName()} / {@code setAge()} 就地修改时，普通引用 {@code person}
 * 与 {@code personAtomicReference.get()} 指向的是同一个对象，两边会一起变；
 * 而 {@code getAndSet(new Person(...))} 换掉的是引用，旧的 {@code person} 变量仍指向原对象，于是两者开始不一致。
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2018/5/24
 */
public class AtomicReferenceDemo3 {

    private static String message;

    private static Person person;

    private static AtomicReference<String> stringAtomicReference;

    private static AtomicReference<Person> personAtomicReference;

    public static void main(String[] args) throws InterruptedException {
        demo();
    }

    /**
     * 先打印初始状态，再启动两个线程并发修改，最后打印修改后的状态。
     * 两个线程的执行顺序不固定，所以中间和末尾的具体值每次运行可能不同，
     * 但开头四行（线程启动前的初始状态）是确定的
     */
    public static void demo() throws InterruptedException {
        Thread t1 = new Thread(new MyThread());
        Thread t2 = new Thread(new MyThread2());
        message = "hello";
        person = new Person("Phillip", 23);
        stringAtomicReference = new AtomicReference<String>(message);
        personAtomicReference = new AtomicReference<Person>(person);
        System.out.println("Message is: " + message + "\nPerson is " + person.toString());
        System.out.println(
            "Atomic Reference of Message is: " + stringAtomicReference.get() + "\nAtomic Reference of Person is "
                + personAtomicReference.get().toString());
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println("\nNow Message is: " + message + "\nPerson is " + person.toString());
        System.out.println(
            "Atomic Reference of Message is: " + stringAtomicReference.get() + "\nAtomic Reference of Person is "
                + personAtomicReference.get().toString());
    }

    static class MyThread implements Runnable {

        @Override
        public void run() {
            stringAtomicReference.compareAndSet(message, "Thread 1");
            message = message.concat("-Thread 1!");
            person.setAge(person.getAge() + 1);
            person.setName("Thread 1");
            personAtomicReference.getAndSet(new Person("Thread 1", 1));
            System.out.println(
                "\n" + Thread.currentThread().getName() + " Values " + message + " - " + person.toString());
            System.out.println("\n" + Thread.currentThread().getName() + " Atomic References " + message + " - "
                + person.toString());
        }

    }

    static class MyThread2 implements Runnable {

        @Override
        public void run() {
            message = message.concat("-Thread 2");
            person.setAge(person.getAge() + 2);
            person.setName("Thread 2");
            stringAtomicReference.lazySet("Thread 2");
            personAtomicReference.set(new Person("Thread 2", 2));
            System.out.println(
                "\n" + Thread.currentThread().getName() + " Values: " + message + " - " + person.toString());
            System.out
                .println("\n" + Thread.currentThread().getName() + " Atomic References: " + stringAtomicReference.get()
                    + " - " + personAtomicReference.get().toString());
        }

    }

    static class Person {

        private String name;

        private int age;

        Person(String name, int age) {
            this.name = name;
            this.age = age;
        }

        @Override
        public String toString() {
            return "[name " + this.name + ", age " + this.age + "]";
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        int getAge() {
            return age;
        }

        void setAge(int age) {
            this.age = age;
        }

    }

}
