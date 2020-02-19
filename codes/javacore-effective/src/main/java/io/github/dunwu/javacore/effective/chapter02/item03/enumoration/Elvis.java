// Enum singleton - the preferred approach - page 18
package io.github.dunwu.javacore.effective.chapter02.item03.enumoration;

public enum Elvis {

    INSTANCE;

    // This code would normally appear outside the class!
    public static void main(String[] args) {
        Elvis elvis = Elvis.INSTANCE;
        elvis.leaveTheBuilding();
    }

    public void leaveTheBuilding() {
        System.out.println("Whoa baby, I'm outta here!");
    }
}
