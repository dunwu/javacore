// StringList with a reasonable custom serialized form
package io.github.dunwu.javacore.effective.chapter11.item75;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public final class StringList implements Serializable {

    private static final long serialVersionUID = 93248094385L;

    private transient int size = 0;

    private transient Entry head = null;

    /**
     * Serialize this {@code StringList} instance.
     *
     * @serialData The size of the list (the number of strings it contains) is emitted ({@code int}), followed by all of
     * its elements (each a {@code String}), in the proper sequence.
     */
    private void writeObject(ObjectOutputStream s) throws IOException {
        s.defaultWriteObject();
        s.writeInt(size);

        // Write out all elements in the proper order.
        for (Entry e = head; e != null; e = e.next) {
            s.writeObject(e.data);
        }
    }

    private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        int numElements = s.readInt();

        // Read in all elements and insert them in list
        for (int i = 0; i < numElements; i++) {
            add((String) s.readObject());
        }
    }

    // Appends the specified string to the list
    public final void add(String s) {
        // Implementation omitted
    }

    // No longer Serializable!
    private static class Entry {

        String data;

        Entry next;

        Entry previous;

    }

    // Remainder omitted
}
