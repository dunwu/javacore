// Creates lots of unnecessary duplicate objects - page 20-21
package io.github.dunwu.javacore.effective.chapter02.item05.slowversion;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class Person {

    private final Date birthDate;

    public Person(Date birthDate) {
        // Defensive copy - see Item 39
        this.birthDate = new Date(birthDate.getTime());
    }

    // Other fields, methods omitted

    // DON'T DO THIS!
    public boolean isBabyBoomer() {
        // Unnecessary allocation of expensive object
        Calendar gmtCal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        gmtCal.set(1946, Calendar.JANUARY, 1, 0, 0, 0);
        Date boomStart = gmtCal.getTime();
        gmtCal.set(1965, Calendar.JANUARY, 1, 0, 0, 0);
        Date boomEnd = gmtCal.getTime();
        return birthDate.compareTo(boomStart) >= 0 && birthDate.compareTo(boomEnd) < 0;
    }

}
