// Singleton with static factory - Page 17
package io.github.dunwu.javacore.effective.chapter02.item03.method;

public class Elvis {

    private static final Elvis INSTANCE = new Elvis();

    private Elvis() {
    }

    // This code would normally appear outside the class!
    public static void main(String[] args) {
        Elvis elvis = Elvis.getInstance();
        elvis.leaveTheBuilding();
    }

    public static Elvis getInstance() {
        return INSTANCE;
    }

    public void leaveTheBuilding() {
        System.out.println("Whoa baby, I'm outta here!");
    }

}
