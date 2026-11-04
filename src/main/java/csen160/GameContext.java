package csen160;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class GameContext extends Canvas {
    private boolean running = true;
    private boolean[] keyboardState = new boolean[256];
    private double fps;
    private double fixedDeltaTime;

    // testing
    double x;
    double y;

    public GameContext(double fps) {
        this.fps = fps;
        this.fixedDeltaTime = 1.0 / fps;

        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                keyboardState[e.getKeyCode()] = true;
            }

            @Override
            public void keyReleased(KeyEvent e) {
                keyboardState[e.getKeyCode()] = false;
            }
        });
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
        if (keyboardState[KeyEvent.VK_ESCAPE]) {
            running = false;
            return;
        }

        if (keyboardState[KeyEvent.VK_A]) {
            x -= 200 * getDeltaTime();
        }
        if (keyboardState[KeyEvent.VK_D]) {
            x += 200 * getDeltaTime();
        }
        if (keyboardState[KeyEvent.VK_S]) {
            y += 200 * getDeltaTime();
        }
        if (keyboardState[KeyEvent.VK_W]) {
            y -= 200 * getDeltaTime();
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

        g.setColor(Color.RED);
        g.fillRect((int) x, (int) y, 50, 50);

        g.dispose();
        bs.show();
    }
}
