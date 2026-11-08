package csen160.core;

public record BBox(double x, double y, double w, double h) {
    public boolean intersects(BBox other) {
        return (x <= (other.x + other.w) && (x + w) >= other.x)
                && (y <= (other.y + other.h) && (y + h) >= other.y);
    }

    public boolean fullyContains(BBox inner) {
        return inner.x >= x
                && inner.y >= y
                && inner.x + inner.w <= x + w
                && inner.x + inner.h <= y + h;
    }
}
