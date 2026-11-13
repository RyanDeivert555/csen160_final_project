package csen160.core;

import csen160.entity.Entity;
import csen160.entity.Map;
import csen160.entity.VisualEffect;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

/**
 * A triple-buffered AWT {@link Canvas} responsible for managing frame rendering
 * contexts
 * and presenting drawn frames to the screen.
 */
public class Renderer extends Canvas {

    /**
     * Represents a single-frame drawing session backed by the canvas's
     * {@link BufferStrategy}.
     * <p>
     * Provides convenience methods for switching between world-space camera
     * transforms and
     * screen-space coordinates, drawing primitives, images, entities, maps, and
     * text, and
     * automatically flushing the buffer when closed in a try-with-resources block.
     */
    public class Context implements AutoCloseable {

        /**
         * The multi-buffer strategy used to present rendered frames without flickering.
         */
        private final BufferStrategy bufferStrategy;

        /** The active {@link Graphics2D} surface for the current frame buffer. */
        private final Graphics2D graphics;

        /**
         * The baseline screen-space {@link AffineTransform} captured when the context
         * is created.
         */
        private final AffineTransform screenTransform;

        /**
         * Initializes a rendering context for the current frame. Lazily creates a
         * triple-buffered {@link BufferStrategy} on the parent {@link Renderer} if none
         * exists, acquires the draw {@link Graphics2D} object, and records the default
         * screen-space transform.
         */
        public Context() {
            final var bs = getBufferStrategy();
            if (bs == null) {
                createBufferStrategy(3);
            }

            bufferStrategy = getBufferStrategy();
            graphics = (Graphics2D) bufferStrategy.getDrawGraphics();
            screenTransform = graphics.getTransform();
        }

        /**
         * Configures the graphics context to render in world coordinates by resetting
         * to
         * {@link #screenTransform} and applying the specified {@link Camera}'s
         * transform.
         *
         * @param camera the {@link Camera} whose world-to-screen transform should be
         *               applied
         */
        public void useCamera(Camera camera) {
            graphics.setTransform(screenTransform);
            graphics.transform(camera.getTransform());
        }

        /**
         * Resets the graphics transform back to raw screen space for drawing HUD and UI
         * elements.
         */
        public void useScreenSpace() {
            graphics.setTransform(screenTransform);
        }

        /**
         * Sets the active drawing color on the underlying {@link Graphics2D} context.
         *
         * @param color the {@link Color} to use for subsequent fill and draw operations
         */
        public void setColor(Color color) {
            graphics.setColor(color);
        }

        /**
         * Clears the entire canvas viewport by filling a rectangle covering the full
         * width
         * and height of the {@link Renderer} with the current color.
         */
        public void clearBackground() {
            graphics.fillRect(0, 0, getWidth(), getHeight());
        }

        /**
         * Draws a filled rectangle at the specified coordinates and dimensions using
         * the current color.
         *
         * @param x      the x-coordinate of the rectangle's top-left corner
         * @param y      the y-coordinate of the rectangle's top-left corner
         * @param width  the width of the rectangle in pixels
         * @param height the height of the rectangle in pixels
         */
        public void drawRect(int x, int y, int width, int height) {
            graphics.fillRect(x, y, width, height);
        }

        /**
         * Draws a {@link BufferedImage} at the specified top-left coordinates at its
         * native size.
         *
         * @param img the {@link BufferedImage} to render
         * @param x   the x-coordinate of the image's top-left corner
         * @param y   the y-coordinate of the image's top-left corner
         */
        public void drawImage(BufferedImage img, int x, int y) {
            graphics.drawImage(img, x, y, null);
        }

        /**
         * Draws a {@link BufferedImage} at the specified top-left coordinates scaled to
         * the given dimensions.
         *
         * @param img the {@link BufferedImage} to render
         * @param x   the x-coordinate of the image's top-left corner
         * @param y   the y-coordinate of the image's top-left corner
         * @param w   the target width in pixels
         * @param h   the target height in pixels
         */
        public void drawImage(BufferedImage img, int x, int y, int w, int h) {
            graphics.drawImage(img, x, y, w, h, null);
        }

        /**
         * Renders an {@link Entity} onto the active graphics context.
         *
         * @param e the {@link Entity} to render
         */
        public void drawImage(Entity e) {
            e.render(graphics);
        }

        /**
         * Renders a tile {@link Map} onto the active graphics context.
         *
         * @param m the {@link Map} to render
         */
        public void drawMap(Map m) {
            m.render(graphics);
        }

        /**
         * Renders a {@link VisualEffect} onto the active graphics context.
         *
         * @param ve the {@link VisualEffect} to render
         */
        public void drawImage(VisualEffect ve) {
            ve.render(graphics);
        }

        /**
         * Draws a text string at the specified baseline coordinates using bold white
         * 24-point Comic Sans MS font.
         *
         * @param t the string to render
         * @param x the x-coordinate of the text baseline origin
         * @param y the y-coordinate of the text baseline origin
         */
        public void drawString(String t, int x, int y) {
            graphics.setColor(Color.WHITE);
            graphics.setFont(new Font("comic sans ms", Font.BOLD, 24));
            graphics.drawString(t, x, y);
        }

        /**
         * Disposes of the frame's {@link Graphics2D} resources and swaps the back
         * buffer
         * to the screen via {@link BufferStrategy#show()}.
         */
        @Override
        public void close() {
            graphics.dispose();
            bufferStrategy.show();
        }
    }

    /**
     * Constructs a new {@code Renderer} canvas.
     */
    public Renderer() {
    }

    /**
     * Creates and returns a new {@link Context} for rendering a single frame.
     *
     * @return a new {@link Context} bound to this canvas's buffer strategy
     */
    public Context getContext() {
        return new Context();
    }
}
