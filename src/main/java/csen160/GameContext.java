package csen160;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class GameContext extends Canvas {
    private boolean running = true;
    private double fps;
    private double fixedDeltaTime;
    private final InputManager input = new InputManager();

    public GameContext(double fps) {
        this.fps = fps;
        this.fixedDeltaTime = 1.0 / fps;
        this.addKeyListener(input);
    }

    public GameContext() {
        this(60.0);
    }

    public static BufferedImage loadSprite(String path) {
        try (final var in = App.class.getResourceAsStream(path)) {
            if (in == null) {
                throw new NullPointerException("Sprite not found: " + path);
            }

            return ImageIO.read(in);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load " + path, e);
        }
    }

    public double getDeltaTime() {
        return fixedDeltaTime;
    }

    public void run() {
        this.requestFocus();
        long lastTime = System.nanoTime();
        double accum = 0.0;
        final double nsPerTick = 1_000_000_000.0 / fps;

        while (running) {
            final long now = System.nanoTime();
            final var elapsed = (now - lastTime) / nsPerTick;
            lastTime = now;

            accum += elapsed;

            while (accum >= 1.0) {
                input.update();
                update();
                accum -= 1.0;
            }

            render();
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                running = false;
            }
        }
    }

    private void update() {
        if (input.isKeyDown(KeyEvent.VK_ESCAPE)) {
            running = false;
            return;
        }
    }

    private void render() {
        final var bs = this.getBufferStrategy();
        if (bs == null) {
            this.createBufferStrategy(3);
            return;
        }

        final var g = (Graphics2D) bs.getDrawGraphics();

        g.setColor(Color.DARK_GRAY);
        g.fillRect(0, 0, getWidth(), getHeight());
        // Draw stuff here

        g.dispose();
        bs.show();
    }
}
