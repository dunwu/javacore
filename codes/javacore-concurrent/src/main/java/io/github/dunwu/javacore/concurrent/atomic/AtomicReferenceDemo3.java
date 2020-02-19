package io.github.dunwu.javacore.concurrent.atomic;

import java.util.concurrent.atomic.AtomicReference;

/**
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2018/5/24
 */
public class AtomicReferenceDemo3 {

    private static String message;

    private static Person person;

    private static AtomicReference<String> stringAtomicReference;

    private static AtomicReference<Person> personAtomicReference;

    public static void main(String[] args) throws InterruptedException {
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
