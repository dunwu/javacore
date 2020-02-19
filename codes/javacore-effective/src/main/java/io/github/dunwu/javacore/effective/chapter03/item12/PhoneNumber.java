// Making PhoneNumber comparable - Pages 65-66
package io.github.dunwu.javacore.effective.chapter03.item12;

import java.util.NavigableSet;
import java.util.Random;
import java.util.TreeSet;

public final class PhoneNumber implements Cloneable, Comparable<PhoneNumber> {

    private static final Random rnd = new Random();

    private final short areaCode;

    private final short prefix;

    private final short lineNumber;

    public PhoneNumber(int areaCode, int prefix, int lineNumber) {
        rangeCheck(areaCode, 999, "area code");
        rangeCheck(prefix, 999, "prefix");
        rangeCheck(lineNumber, 9999, "line number");
        this.areaCode = (short) areaCode;
        this.prefix = (short) prefix;
        this.lineNumber = (short) lineNumber;
    }

    private static void rangeCheck(int arg, int max, String name) {
        if (arg < 0 || arg > max) { throw new IllegalArgumentException(name + ": " + arg); }
    }

    public static void main(String[] args) {
        NavigableSet<PhoneNumber> s = new TreeSet<PhoneNumber>();
        for (int i = 0; i < 10; i++) {
            s.add(randomPhoneNumber());
        }
        System.out.println(s);
    }

    private static PhoneNumber randomPhoneNumber() {
        return new PhoneNumber((short) rnd.nextInt(1000), (short) rnd.nextInt(1000), (short) rnd.nextInt(10000));
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + areaCode;
        result = 31 * result + prefix;
        result = 31 * result + lineNumber;
        return result;
    }

    // Works fine, but can be made faster
    // public int compareTo(PhoneNumber pn) {
    // // Compare area codes
    // if (areaCode < pn.areaCode)
    // return -1;
    // if (areaCode > pn.areaCode)
    // return 1;
    //
    // // Area codes are equal, compare prefixes
    // if (prefix < pn.prefix)
    // return -1;
    // if (prefix > pn.prefix)
    // return 1;
    //
    // // Area codes and prefixes are equal, compare line numbers
    // if (lineNumber < pn.lineNumber)
    // return -1;
    // if (lineNumber > pn.lineNumber)
    // return 1;
    //
    // return 0; // All fields are equal
    // }

    @Override
    public boolean equals(Object o) {
        if (o == this) { return true; }
        if (!(o instanceof PhoneNumber)) { return false; }
        PhoneNumber pn = (PhoneNumber) o;
        return pn.lineNumber == lineNumber && pn.prefix == prefix && pn.areaCode == areaCode;
    }

    @Override
    public PhoneNumber clone() {
        try {
            return (PhoneNumber) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(); // Can't happen
        }
    }

    /**
     * Returns the string representation of this phone number. The string consists of fourteen characters whose format
     * is "(XXX) YYY-ZZZZ", where XXX is the area code, YYY is the prefix, and ZZZZ is the line number. (Each of the
     * capital letters represents a single decimal digit.)
     * <p>
     * If any of the three parts of this phone number is too small to fill up its field, the field is padded with
     * leading zeros. For example, if the value of the line number is 123, the last four characters of the string
     * representation will be "0123".
     * <p>
     * Note that there is a single space separating the closing parenthesis after the area code from the first digit of
     * the prefix.
     */
    @Override
    public String toString() {
        return String.format("(%03d) %03d-%04d", areaCode, prefix, lineNumber);
    }

    @Override
    public int compareTo(PhoneNumber pn) {
        // Compare area codes
        int areaCodeDiff = areaCode - pn.areaCode;
        if (areaCodeDiff != 0) { return areaCodeDiff; }

        // Area codes are equal, compare prefixes
        int prefixDiff = prefix - pn.prefix;
        if (prefixDiff != 0) { return prefixDiff; }

        // Area codes and prefixes are equal, compare line numbers
        return lineNumber - pn.lineNumber;
    }

}
