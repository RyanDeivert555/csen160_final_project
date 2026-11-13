package csen160.core;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * Represents a 2D orthographic viewport camera supporting pan, zoom, and
 * coordinate
 * conversions between screen space and world space.
 */
public class Camera {

    /** The center position of the camera in world coordinates. */
    private Point2D.Double position;

    /** The zoom magnification factor (where {@code 1.0} represents 100% scale). */
    private double zoom;

    /** The width of the viewport in screen pixels. */
    private double viewWidth;

    /** The height of the viewport in screen pixels. */
    private double viewHeight;

    /**
     * Constructs a new {@code Camera} centered at the given world position with the
     * specified zoom level and viewport dimensions.
     *
     * @param position   the initial center coordinates of the camera in world space
     * @param zoom       the zoom scale factor applied when transforming world
     *                   coordinates
     * @param viewWidth  the width of the viewport in screen pixels
     * @param viewHeight the height of the viewport in screen pixels
     */
    public Camera(Point2D.Double position, double zoom, double viewWidth, double viewHeight) {
        this.position = position;
        this.zoom = zoom;
        this.viewWidth = viewWidth;
        this.viewHeight = viewHeight;
    }

    /**
     * Updates the center position of the camera in world coordinates.
     *
     * @param x the new x-coordinate in world space
     * @param y the new y-coordinate in world space
     */
    public void setPosition(double x, double y) {
        this.position.x = x;
        this.position.y = y;
    }

    /**
     * Sets the camera's zoom magnification factor.
     *
     * @param zoom the new zoom scale factor
     */
    public void setZoom(double zoom) {
        this.zoom = zoom;
    }

    /**
     * Updates the dimensions of the camera's viewport in screen pixels.
     *
     * @param width  the new viewport width in pixels
     * @param height the new viewport height in pixels
     */
    public void setViewSize(double width, double height) {
        viewWidth = width;
        viewHeight = height;
    }

    /**
     * Builds and returns an {@link AffineTransform} that maps world coordinates to
     * screen coordinates by centering on the viewport, scaling by {@link #zoom},
     * and translating relative to {@link #position}.
     *
     * @return the world-to-screen {@link AffineTransform}
     */
    public AffineTransform getTransform() {
        var at = new AffineTransform();
        at.translate(viewWidth / 2.0, viewHeight / 2.0);
        at.scale(zoom, zoom);
        at.translate(-position.x, -position.y);

        return at;
    }

    /**
     * Converts a point from screen (pixel) coordinates to world coordinates.
     *
     * @param sx the x-coordinate in screen space
     * @param sy the y-coordinate in screen space
     * @return the corresponding {@link Point2D.Double} in world space
     */
    public Point2D.Double screenToWorld(double sx, double sy) {
        return new Point2D.Double((sx - viewWidth / 2.0) / zoom + position.x,
                (sy - viewHeight / 2.0) / zoom + position.y);
    }

    /**
     * Converts a point from world coordinates to screen (pixel) coordinates.
     *
     * @param wx the x-coordinate in world space
     * @param wy the y-coordinate in world space
     * @return the corresponding {@link Point2D.Double} in screen space
     */
    public Point2D.Double worldToScreen(double wx, double wy) {
        return new Point2D.Double((wx - position.x) * zoom + viewWidth / 2.0,
                (wy - position.y) * zoom + viewHeight / 2.0);
    }

    /**
     * Calculates the rectangular region of the game world currently visible through
     * the viewport given the camera's {@link #position}, {@link #zoom}, and
     * viewport size.
     *
     * @return a {@link Rectangle2D.Double} representing the visible world bounds
     */
    public Rectangle2D.Double getVisibleBounds() {
        final var w = viewWidth / zoom;
        final var h = viewHeight / zoom;

        return new Rectangle2D.Double(position.x - w / 2, position.y - h / 2, w, h);
    }
}
