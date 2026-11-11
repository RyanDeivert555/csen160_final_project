package csen160.core;

import csen160.entity.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicBoolean;

public class GameContext {
    private boolean running;
    private double fps;
    private double fixedDeltaTime;
    private final InputManager input = new InputManager();
    private final Renderer renderer = new Renderer();

    private final BufferedImage bg = AssetLoader.loadTexture("bg.png");

    private Camera camera;
    private Player player = new Player();
    private HashSet<Enemy> enemies = new HashSet<>();
    private HashSet<Projectile> playerProjectiles = new HashSet<>();
    private HashSet<Projectile> enemyProjectiles = new HashSet<>();

    private int points = 0;

    public GameContext(double fps) {
        running = true;
        this.fps = fps;
        fixedDeltaTime = 1.0 / fps;
        renderer.addKeyListener(input);
        renderer.addMouseListener(input);
        renderer.addMouseMotionListener(input);

        camera = new Camera(new Point2D.Double(), 1.0, renderer.getWidth(), renderer.getHeight());
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

        // update player and enemies
        player.update(dt);
        for (Enemy e : enemies) {
            e.update(dt);
            Projectile p = e.shootProjectile(e.angleTowards(player));
            if(p != null) enemyProjectiles.add(p);
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

        // move all player projectiles, destroy if lifetime elapsed
        Iterator<Projectile> it = playerProjectiles.iterator();
        while (it.hasNext()) {
            Projectile p = it.next();

            boolean expired = p.travel(dt);
            boolean hit = false;

            // check every enemy to see if hit by projectile
            Iterator<Enemy> enemyIt = enemies.iterator();
            while (enemyIt.hasNext()) {
                Enemy e = enemyIt.next();
                if (e.getBbox().intersects(p.getBbox())) {
                    // projectile hits enemy
                    hit = true;
                    if (e.takeDamage()) {
                        // enemy defeated
                        points = (int) (points + 100 * Math.pow(e.getMaxHealth(), 1.2));
                        enemyIt.remove();
                    }
                    break;
                }
            }
            if (expired || hit) it.remove();
        }

        // move all enemy projectiles, damage player if hit
        it = enemyProjectiles.iterator();
        while (it.hasNext()) {
            Projectile p = it.next();

            boolean expired = p.travel(dt);
            boolean hit = false;

            if (player.getBbox().intersects(p.getBbox())) {
                hit = true;
                if (player.takeDamage()) {
                    // TODO: defeated -> game over ?
                }
            }

            if (expired || hit) it.remove();
        }

        // shoot
        if (input.isMouseDown(MouseEvent.BUTTON1)) {
            Projectile p = player.shootProjectile(player.rotation);
            if (p != null)
                playerProjectiles.add(p);
        }
    }

    private void render() {
        try (final var r = renderer.getContext()) {
            r.setColor(Color.BLACK);
            r.clearBackground();

            // world drawing
            r.useCamera(camera);

            r.drawImage(bg, 0, 0);

            r.drawImage(player);
            for (final var e : enemies) {
                r.drawImage(e);
            }
            for (final var p : playerProjectiles) {
                r.drawImage(p);
            }
            for (final var p : enemyProjectiles) {
                r.drawImage(p);
            }

            // ui drawing
            r.useScreenSpace();

            r.drawString("SCORE: " + points, 0, 24);

            int x = input.getMouseX();
            int y = input.getMouseY();
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
