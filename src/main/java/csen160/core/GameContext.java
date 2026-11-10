package csen160.core;

import csen160.entity.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.HashSet;

public class GameContext {
    private boolean running;
    private double fps;
    private double fixedDeltaTime;
    private final InputManager input = new InputManager();
    private final Renderer renderer = new Renderer();

    private Camera camera;
    private Player player = new Player();
    private HashSet<Enemy> enemies = new HashSet<>();
    private HashSet<Projectile> playerProjectiles = new HashSet<>();

    public GameContext(double fps) {
        running = true;
        this.fps = fps;
        fixedDeltaTime = 1.0 / fps;
        renderer.addKeyListener(input);
        renderer.addMouseListener(input);
        renderer.addMouseMotionListener(input);

        camera = new Camera(0.0, 0.0, 1.0, renderer.getWidth(), renderer.getHeight());
    }

    public GameContext() {
        this(60.0);
    }

    public Renderer getRenderer() {
        return renderer;
    }

    public double getDeltaTime() {
        return fixedDeltaTime;
    }

    public void run() {
        renderer.requestFocus();
        long lastTime = System.nanoTime();
        double accum = 0.0;
        final double nsPerTick = 1_000_000_000.0 / fps;

        Enemy e = new Enemy();
        enemies.add(e);
        e.teleport(200, 200);

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
            // Prevent busy-waiting
            sleep();
        }
    }

    private void update() {
        if (input.isKeyDown(KeyEvent.VK_ESCAPE)) {
            running = false;
            return;
        }

        final var dt = getDeltaTime();

        // move player
        if (input.isKeyDown(KeyEvent.VK_W)) {
            player.move(0, -1, dt);
        }
        if (input.isKeyDown(KeyEvent.VK_A)) {
            player.move(-1, 0, dt);
        }
        if (input.isKeyDown(KeyEvent.VK_S)) {
            player.move(0, 1, dt);
        }
        if (input.isKeyDown(KeyEvent.VK_D)) {
            player.move(1, 0, dt);
        }

        final var bbox = player.getBbox();
        final var centerX = player.getX() + bbox.w() / 2.0;
        final var centerY = player.getY() + bbox.h() / 2.0;

        camera.setViewSize(renderer.getWidth(), renderer.getHeight());
        camera.setPosition(centerX, centerY);

        final var mouseWorld = camera.screenToWorld(input.getMouseX(), input.getMouseY());
        final var dx = mouseWorld.x - centerX;
        final var dy = mouseWorld.y - centerY;
        player.rotation = Math.toDegrees(Math.atan2(dy, dx));

        // move all projectiles, destroy if lifetime elapsed
        playerProjectiles.removeIf(p -> {
            boolean expired = p.travel(dt);
            boolean hit = enemies.removeIf(e -> e.getBbox().intersects(p.getBbox())); // todo: change removeIf -> for
                                                                                      // loop when implementing enemy
                                                                                      // health
            return expired || hit;
        });

        // shoot
        if (input.isMousePressed(MouseEvent.BUTTON1)) {
            Projectile p = player.shootProjectile(player.rotation);
            playerProjectiles.add(p);
        }
    }

    private void render() {
        try (final var r = renderer.getContext()) {
            r.setColor(Color.BLACK);
            r.clearBackground();

            // world drawing
            r.useCamera(camera);

            r.drawImage(player);
            for (final var e : enemies) {
                r.drawImage(e);
            }
            for (final var p : playerProjectiles) {
                r.drawImage(p);
            }

            // ui drawing
            r.useScreenSpace();
            int x = input.getMouseX();
            int y = input.getMouseY();

            r.setColor(Color.BLUE);
            r.drawRect(x - 10, y - 10, 20, 20);
        }
    }

    private void sleep() {
        try {
            // How long the thread sleeps for doesn't really matter, it can be inaccurate
            Thread.sleep(1);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            running = false;
        }
    }
}
