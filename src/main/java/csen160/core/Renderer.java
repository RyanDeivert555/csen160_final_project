package csen160.core;

import csen160.entity.Entity;

import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.Canvas;
import java.awt.Color;

public class Renderer extends Canvas {
    public class Context implements AutoCloseable {
        private final BufferStrategy bufferStrategy;
        private final Graphics2D graphics;
        private final Canvas canvas;

        public Context(Canvas canvas) {
            final var bs = getBufferStrategy();
            if (bs == null) {
                createBufferStrategy(3);
            }

            bufferStrategy = getBufferStrategy();
            graphics = (Graphics2D) bufferStrategy.getDrawGraphics();
            this.canvas = canvas;
        }

        public void setColor(Color color) {
            graphics.setColor(color);
        }

        public void clearBackground() {
            graphics.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        }

        public void drawRect(int x, int y, int width, int height) {
            graphics.fillRect(x, y, width, height);
        }

        public void drawImage(BufferedImage img, int x, int y) {
            graphics.drawImage(img, x, y, null);
        }

        public void drawImage(BufferedImage img, int x, int y, int w, int h) {
            graphics.drawImage(img, x, y, w, h, null);
        }

        public void drawImage(Entity e) {
            e.render(graphics);
        }

        @Override
        public void close() {
            graphics.dispose();
            bufferStrategy.show();
        }
    }

    public Renderer() {
    }

    public Context getContext() {
        return new Context(this);
    }
}
