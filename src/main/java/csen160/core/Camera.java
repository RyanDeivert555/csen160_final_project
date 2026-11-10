package csen160.core;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class Camera {
    private double x;
    private double y;

    private double zoom;

    private double viewWidth;
    private double viewHeight;

    public Camera(double x, double y, double zoom, double viewWidth, double viewHeight) {
        this.x = x;
        this.y = y;
        this.zoom = zoom;
        this.viewWidth = viewWidth;
        this.viewHeight = viewHeight;
    }

    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void setZoom(double zoom) {
        this.zoom = zoom;
    }

    public void setViewSize(double width, double height) {
        viewWidth = width;
        viewHeight = height;
    }

    public AffineTransform getTransform() {
        var at = new AffineTransform();
        at.translate(viewWidth / 2.0, viewHeight / 2.0);
        at.scale(zoom, zoom);
        at.translate(-x, -y);

        return at;
    }
    public Point2D.Double screenToWorld(double sx, double sy) {
        return new Point2D.Double((sx - viewWidth / 2.0) / zoom + x,
                                  (sy - viewHeight / 2.0) / zoom + y);
    }

    public Point2D.Double worldToScreen(double wx, double wy) {
        return new Point2D.Double((wx - x) * zoom + viewWidth / 2.0,
                                  (wy - y) * zoom + viewHeight / 2.0);
    }

    public Rectangle2D.Double getVisibleBounds() {
        final var w = viewWidth / zoom;
        final var h = viewHeight / zoom;

        return new Rectangle2D.Double(x - w / 2, y - h / 2, w, h);
    }
}
