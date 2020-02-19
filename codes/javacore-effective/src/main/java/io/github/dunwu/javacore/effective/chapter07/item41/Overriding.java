// Overriding demonstration - Page 192
package io.github.dunwu.javacore.effective.chapter07.item41;

class Wine {

    String name() {
        return "wine";
    }

}

class SparklingWine extends Wine {

    @Override
    String name() {
        return "sparkling wine";
    }

}

class Champagne extends SparklingWine {

    @Override
    String name() {
        return "champagne";
    }

}

public class Overriding {

    public static void main(String[] args) {
        Wine[] wines = { new Wine(), new SparklingWine(), new Champagne() };
        for (Wine wine : wines) {
            System.out.println(wine.name());
        }
    }

}
