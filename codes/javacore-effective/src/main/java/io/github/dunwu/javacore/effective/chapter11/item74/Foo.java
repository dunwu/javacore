// Serializable subclass of nonserializable stateful class - Pages 293-294
package io.github.dunwu.javacore.effective.chapter11.item74;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Foo extends AbstractFoo implements Serializable {

    private static final long serialVersionUID = 1856835860954L;

    // Constructor does not use the fancy mechanism
    public Foo(int x, int y) {
        super(x, y);
    }

    private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();

        // Manually deserialize and initialize superclass state
        int x = s.readInt();
        int y = s.readInt();
        initialize(x, y);
    }

    private void writeObject(ObjectOutputStream s) throws IOException {
        s.defaultWriteObject();

        // Manually serialize superclass state
        s.writeInt(getX());
        s.writeInt(getY());
    }

}
