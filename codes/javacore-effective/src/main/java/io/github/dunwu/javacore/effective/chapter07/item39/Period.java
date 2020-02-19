// Broken "immutable" time period class - Page 184
package io.github.dunwu.javacore.effective.chapter07.item39;

import java.util.Date;

public final class Period {

    private final Date start;

    private final Date end;

    /**
     * @param start the beginning of the period
     * @param end   the end of the period; must not precede start
     * @throws IllegalArgumentException if start is after end
     * @throws NullPointerException     if start or end is null
     */
    public Period(Date start, Date end) {
        if (start.compareTo(end) > 0) { throw new IllegalArgumentException(start + " after " + end); }
        this.start = start;
        this.end = end;
    }

    // Repaired constructor - makes defensive copies of parameters - Page 185
    // Stops first attack
    // public Period(Date start, Date end) {
    // this.start = new Date(start.getTime());
    // this.end = new Date(end.getTime());
    //
    // if (this.start.compareTo(this.end) > 0)
    // throw new IllegalArgumentException(start +" after "+ end);
    // }

    @Override
    public String toString() {
        return start + " - " + end;
    }

    public Date start() {
        return start;
    }

    // Repaired accessors - make defensive copies of internal fields - Page 186
    // Stops second attack
    // public Date start() {
    // return new Date(start.getTime());
    // }
    //
    // public Date end() {
    // return new Date(end.getTime());
    // }

    public Date end() {
        return end;
    }

    // Remainder omitted
}
