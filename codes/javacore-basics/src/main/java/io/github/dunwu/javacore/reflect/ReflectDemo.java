package io.github.dunwu.javacore.reflect;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;

@SuppressWarnings("deprecation")
public class ReflectDemo {

    public static void main(String[] args) throws Exception {
        testGetClassObject();
        testGetObjectByClass();
        testPrintClassInfo();
    }

    /**
     * @description 展示获取Class的不同方式
     * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
     * @since 2016年8月5日
     */
    public static void testGetClassObject() throws Exception {
        // 方法一
        Class<?> clazz1 = Class.forName("io.github.dunwu.javacore.reflect.ReflectDemo$Person");

        // 方法二
        // Java中每个类都有class属性
        Class<?> clazz2 = Person.class;

        // 方法三
        // Java中每个对象都有getClass方法
        Person demo = new Person();
        Class<?> clazz3 = demo.getClass();

        System.out.println("clazz1 的类名：" + clazz1.getName());
        System.out.println("clazz2 的类名：" + clazz2.getName());
        System.out.println("clazz3 的类名：" + clazz3.getName());
    }

    /**
     * @return void
     * @title testGetObjectByClass
     * @description 通过Class实例化对象
     * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
     * @since 2016年8月5日
     */
    public static void testGetObjectByClass() throws Exception {
        Class<?> clazz = Class.forName("io.github.dunwu.javacore.reflect.ReflectDemo$Person");
        Person person = (Person) clazz.newInstance();
        person.setName("Tom");
        person.setAge(20);
        System.out.println(person.toString());
    }

    /**
     * @return void
     * @throws Exception
     * @title testGetFieldsByClass
     * @description 获取类的所有的属性
     * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
     * @since 2016年8月5日
     */
    public static void testPrintClassInfo() throws Exception {
        StringBuffer sb = new StringBuffer();
        sb.append("============== 打印Person类声明信息 ==============\n");

        // 获取Class对象
        Class<?> clazz = Class.forName("io.github.dunwu.javacore.reflect.ReflectDemo$Person");

        // 获取类的所有注解类型
        Annotation[] annotations = clazz.getDeclaredAnnotations();
        for (Annotation annotation : annotations) {
            sb.append("@" + annotation.annotationType().getSimpleName() + "\n");
        }

        // 获取类的修饰符，例如public，final等
        sb.append(Modifier.toString(clazz.getModifiers()) + " class " + clazz.getSimpleName() + " {\n");

        // 获取类的所有域属性
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            sb.append("\t");// 空格
            sb.append(Modifier.toString(field.getModifiers()) + " "); // 获得属性的修饰符，例如public，static等
            sb.append(field.getType().getSimpleName() + " "); // 属性的类型的名字
            sb.append(field.getName() + ";\n"); // 获得属性名
        }

        // 获取类的所有构造方法
        Constructor<?>[] constructors = clazz.getDeclaredConstructors();
        for (Constructor<?> constructor : constructors) {
            sb.append("\t");
            sb.append(Modifier.toString(constructor.getModifiers()) + " "); // 获得构造方法的修饰符，例如public，static等等
            String fullname = constructor.getName(); // 获得方法名。此处的方法名是完整名，包括package路径
            String name = fullname.substring(fullname.lastIndexOf(".") + 1);
            sb.append(name + "(");

            // 获取方法的所有属性
            Parameter[] params = constructor.getParameters();
            for (int i = 0; i < params.length; i++) {
                sb.append(params[i].getType().getSimpleName() + " ");
                if (i == params.length - 1) {
                    sb.append(params[i].getName());
                } else {
                    sb.append(params[i].getName() + ", ");
                }
            }
            sb.append(");\n"); // 属性的名字+回车
        }

        // 获取类的所有的方法
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            sb.append("\t");
            sb.append(Modifier.toString(method.getModifiers()) + " "); // 获得方法的修饰符，例如public，static等等
            sb.append(method.getReturnType().getSimpleName() + " "); // 获得方法的返回值
            sb.append(method.getName() + "("); // 获得方法名

            // 获取构造方法的所有属性
            Parameter[] params = method.getParameters();
            for (int i = 0; i < params.length; i++) {
                sb.append(params[i].getType().getSimpleName() + " ");
                if (i == params.length - 1) {
                    sb.append(params[i].getName());
                } else {
                    sb.append(params[i].getName() + ", ");
                }
            }
            sb.append(");\n"); // 属性的名字+回车
        }

        sb.append("}");
        System.out.println(sb.toString());
    }

    static class Person {

        private String name;

        private int age;

        private Job job;

        public Person() {
        }

        public Person(String name, int age) {
            this.name = name;
            this.age = age;
        }

        @Override
        public String toString() {
            return "[Person]name = " + this.name + ", age = " + this.age;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public final boolean checkPersonInfo(String name, int age) {
            return null != name && 0 != name.length() && age > 0;
        }

    }

    class Job {

    }

}
