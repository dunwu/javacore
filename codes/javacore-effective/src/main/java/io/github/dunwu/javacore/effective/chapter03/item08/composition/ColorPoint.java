// Adds a value component without violating the equals contract - Page 40
package io.github.dunwu.javacore.effective.chapter03.item08.composition;

public class ColorPoint {

    private final Point point;

    private final Color color;

    public ColorPoint(int x, int y, Color color) {
        if (color == null) { throw new NullPointerException(); }
        point = new Point(x, y);
        this.color = color;
    }

    @Override
    public int hashCode() {
        return point.hashCode() * 33 + color.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ColorPoint)) { return false; }
        ColorPoint cp = (ColorPoint) o;
        return cp.point.equals(point) && cp.color.equals(color);
    }

    /**
     * Returns the point-view of this color point.
     */
    public Point asPoint() {
        return point;
    }

}
