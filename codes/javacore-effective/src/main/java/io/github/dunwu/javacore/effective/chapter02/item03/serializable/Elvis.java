// Serializable singleton with public final field - Page 18
package io.github.dunwu.javacore.effective.chapter02.item03.serializable;

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

    private Object readResolve() {
        // Return the one true Elvis and let the garbage collector
        // take care of the Elvis impersonator.
        return INSTANCE;
    }

}
