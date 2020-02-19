// Period class with serialization proxy - Pages 312-313
package io.github.dunwu.javacore.effective.chapter11.item78;

import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Date;

public final class Period implements Serializable {

    private final Date start;

    private final Date end;

    /**
     * @param start the beginning of the period
     * @param end   the end of the period; must not precede start
     * @throws IllegalArgumentException if start is after end
     * @throws NullPointerException     if start or end is null
     */
    public Period(Date start, Date end) {
        this.start = new Date(start.getTime());
        this.end = new Date(end.getTime());
        if (this.start.compareTo(this.end) > 0) { throw new IllegalArgumentException(start + " after " + end); }
    }

    @Override
    public String toString() {
        return start + " - " + end;
    }

    public Date start() {
        return new Date(start.getTime());
    }

    public Date end() {
        return new Date(end.getTime());
    }

    // writeReplace method for the serialization proxy pattern - page 312
    private Object writeReplace() {
        return new SerializationProxy(this);
    }

    // readObject method for the serialization proxy pattern - Page 313
    private void readObject(ObjectInputStream stream) throws InvalidObjectException {
        throw new InvalidObjectException("Proxy required");
    }

    // Serialization proxy for Period class - page 312
    private static class SerializationProxy implements Serializable {

        private static final long serialVersionUID = 234098243823485285L; // Any

        private final Date start;

        private final Date end;

        SerializationProxy(Period p) {
            this.start = p.start;
            this.end = p.end;
        }
        // number
        // will
        // do
        // (Item
        // 75)

        // readResolve method for Period.SerializationProxy - Page 313
        private Object readResolve() {
            return new Period(start, end); // Uses public constructor
        }

    }

}
