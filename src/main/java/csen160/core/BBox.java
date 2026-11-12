package csen160.core;

public record BBox(double x, double y, double w, double h) {
    // check the distance between is less than half the sum of their dimensions
    public boolean intersects(BBox other) {
        return Math.abs(x - other.x) <= (w + other.w) / 2
                && Math.abs(y - other.y) <= (h + other.h) / 2;
    }

    public boolean fullyContains(BBox inner) {
        return Math.abs(x - inner.x) + inner.w / 2.0 <= w / 2.0
                && Math.abs(y - inner.y) + inner.h / 2.0 <= h / 2.0;
    }
}
