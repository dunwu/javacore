package io.github.dunwu.javacore.jdk8.lambda;

/**
 * @author Benjamin Winterberg
 */
public class Lambda2 {

    public static void main(String[] args) {
        Converter<String, Integer> integerConverter1 = (from) -> Integer.valueOf(from);
        Integer converted1 = integerConverter1.convert("123");
        System.out.println(converted1); // result: 123

        // method reference

        Converter<String, Integer> integerConverter2 = Integer::valueOf;
        Integer converted2 = integerConverter2.convert("123");
        System.out.println(converted2); // result: 123

        Something something = new Something();

        Converter<String, String> stringConverter = something::startsWith;
        String converted3 = stringConverter.convert("Java");
        System.out.println(converted3); // result J

        // constructor reference

        PersonFactory<Person> personFactory = Person::new;
        Person person = personFactory.create("Peter", "Parker");
    }

    @FunctionalInterface
    public interface Converter<F, T> {

        T convert(F from);

    }

    interface PersonFactory<P extends Person> {

        P create(String firstName, String lastName);

    }

    static class Something {

        String startsWith(String s) {
            return String.valueOf(s.charAt(0));
        }

    }

}
