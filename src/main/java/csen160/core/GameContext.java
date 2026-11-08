package csen160.core;

import csen160.App;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.HashSet;

public class GameContext {
    private boolean running;
    private double fps;
    private double fixedDeltaTime;
    private final InputManager input = new InputManager();
    private final Renderer renderer = new Renderer();

    private Player player = new Player();
    private HashSet<Entity> entities = new HashSet<>();
    private HashSet<Projectile> playerProjectiles = new HashSet<>();

    public GameContext(double fps) {
        running = true;
        this.fps = fps;
        fixedDeltaTime = 1.0 / fps;
        renderer.addKeyListener(input);
        renderer.addMouseListener(input);
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

        // TODO: test entity --> remove later
        entities.add(new Entity("/temp.png", .25));

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

        // rotate player
        double playerToMouseDeg = Math.toDegrees(Math.atan2(
                input.getMouseY() - (player.getY() + player.getHeight() / 2.0),
                input.getMouseX() - (player.getX() + player.getWidth() / 2.0)));
        player.rotation = playerToMouseDeg;

        // move all projectiles, destroy if lifetime elapsed
        playerProjectiles.removeIf(p -> p.travel(dt));

        // shoot
        if (input.isMousePressed(1)) {
            Projectile p = player.shootProjectile(playerToMouseDeg);
            playerProjectiles.add(p);
        }

        // face direction of mouse
        // player.logCollisions(entities);
    }

    private void render() {
        try (final var r = renderer.getContext()) {
            r.setColor(Color.BLACK);
            r.clearBackground();

            // MOVE ENTITIES
            r.drawImage(player);
            for (Entity e : entities) {
                r.drawImage(e);
            }
            for (Projectile p : playerProjectiles) {
                r.drawImage(p);
            }

            int x = input.getMouseX();
            int y = input.getMouseY();

            r.setColor(Color.BLUE);
            r.drawRect(x-10, y-10, 20, 20);

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
