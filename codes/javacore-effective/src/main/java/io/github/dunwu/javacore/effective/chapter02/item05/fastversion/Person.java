// Doesn't creates unnecessary duplicate objects - page 21
package io.github.dunwu.javacore.effective.chapter02.item05.fastversion;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

class Person {

    /**
     * The starting and ending dates of the baby boom.
     */
    private static final Date BOOM_START;

    private static final Date BOOM_END;

    // Other fields, methods
    static {
        Calendar gmtCal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        gmtCal.set(1946, Calendar.JANUARY, 1, 0, 0, 0);
        BOOM_START = gmtCal.getTime();
        gmtCal.set(1965, Calendar.JANUARY, 1, 0, 0, 0);
        BOOM_END = gmtCal.getTime();
    }

    private final Date birthDate;

    public Person(Date birthDate) {
        // Defensive copy - see Item 39
        this.birthDate = new Date(birthDate.getTime());
    }

    public boolean isBabyBoomer() {
        return birthDate.compareTo(BOOM_START) >= 0 && birthDate.compareTo(BOOM_END) < 0;
    }

}
