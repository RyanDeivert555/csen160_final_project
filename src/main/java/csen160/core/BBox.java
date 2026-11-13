package csen160.core;

/**
 * Represents a center-anchored 2D axis-aligned bounding box (AABB) used for
 * collision detection and spatial containment checks.
 *
 * @param x the horizontal center coordinate of the bounding box in world space
 * @param y the vertical center coordinate of the bounding box in world space
 * @param w the total width of the bounding box
 * @param h the total height of the bounding box
 */
public record BBox(double x, double y, double w, double h) {

    /**
     * Checks whether this bounding box overlaps or touches another bounding box
     * along both the X and Y axes.
     *
     * @param other the other {@code BBox} to test for intersection
     * @return {@code true} if the distance between centers on each axis is less
     *         than or
     *         equal to half the sum of their dimensions; {@code false} otherwise
     */
    // check the distance between is less than half the sum of their dimensions
    public boolean intersects(BBox other) {
        return Math.abs(x - other.x) <= (w + other.w) / 2
                && Math.abs(y - other.y) <= (h + other.h) / 2;
    }

    /**
     * Checks whether another bounding box lies completely within (or flush with the
     * boundaries of) this bounding box.
     *
     * @param inner the inner {@code BBox} to test for full containment
     * @return {@code true} if {@code inner} is entirely enclosed within this
     *         bounding box;
     *         {@code false} otherwise
     */
    public boolean fullyContains(BBox inner) {
        return Math.abs(x - inner.x) + inner.w / 2.0 <= w / 2.0
                && Math.abs(y - inner.y) + inner.h / 2.0 <= h / 2.0;
    }
}
