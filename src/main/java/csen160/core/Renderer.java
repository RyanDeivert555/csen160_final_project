package csen160.core;

import csen160.entity.Entity;
import csen160.entity.Map;
import csen160.entity.VisualEffect;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

public class Renderer extends Canvas {
    public class Context implements AutoCloseable {
        private final BufferStrategy bufferStrategy;
        private final Graphics2D graphics;
        private final AffineTransform screenTransform;

        public Context() {
            final var bs = getBufferStrategy();
            if (bs == null) {
                createBufferStrategy(3);
            }

            bufferStrategy = getBufferStrategy();
            graphics = (Graphics2D) bufferStrategy.getDrawGraphics();
            screenTransform = graphics.getTransform();
        }

        public void useCamera(Camera camera) {
            graphics.setTransform(screenTransform);
            graphics.transform(camera.getTransform());
        }

        public void useScreenSpace() {
            graphics.setTransform(screenTransform);
        }

        public void setColor(Color color) {
            graphics.setColor(color);
        }

        public void clearBackground() {
            graphics.fillRect(0, 0, getWidth(), getHeight());
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

        public void drawMap(Map m) {
            m.render(graphics);
        }

        public void drawImage(VisualEffect ve) {
            ve.render(graphics);
        }

        public void drawString(String t, int x, int y) {
            graphics.setColor(Color.WHITE);
            graphics.setFont(new Font("comic sans ms", Font.BOLD, 24));
            graphics.drawString(t, x, y);
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
        return new Context();
    }
}
