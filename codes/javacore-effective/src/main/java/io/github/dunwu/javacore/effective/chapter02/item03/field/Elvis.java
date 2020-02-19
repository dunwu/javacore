// Singleton with public final field - Page 17
package io.github.dunwu.javacore.effective.chapter02.item03.field;

public class Elvis {

    public static final Elvis INSTANCE = new Elvis();

    private Elvis() {
    }

    // This code would normally appear outside the class!
    public static void main(String[] args) {
        Elvis elvis = Elvis.INSTANCE;
        elvis.leaveTheBuilding();
    }

    public void leaveTheBuilding() {
        System.out.println("Whoa baby, I'm outta here!");
    }

}
