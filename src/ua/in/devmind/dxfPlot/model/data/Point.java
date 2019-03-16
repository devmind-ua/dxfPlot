package ua.in.devmind.dxfPlot.model.data;

import java.math.BigDecimal;
import java.util.Objects;

public class Point {

    public static final String COORDINATES_DELIMITER = ",";

    private BigDecimal x;
    private BigDecimal y;

    public Point(BigDecimal x, BigDecimal y) {
        this.x = x;
        this.y = y;
    }

    public BigDecimal getX() {
        return x;
    }

    public void setX(BigDecimal x) {
        this.x = x;
    }

    public BigDecimal getY() {
        return y;
    }

    public void setY(BigDecimal y) {
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return x.equals(point.x) &&
                y.equals(point.y);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "[ " + x.toPlainString() + ", " + y.toPlainString() + " ]";
    }

    public String toRawString() {
        return x.toPlainString() + COORDINATES_DELIMITER + y.toPlainString();
    }
}
