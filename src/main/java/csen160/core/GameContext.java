package csen160.core;

import csen160.entity.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Iterator;

public class GameContext {
    // engine variables
    private boolean running;
    private double fps;
    private double fixedDeltaTime;

    private final InputManager input = new InputManager();
    private final Renderer renderer = new Renderer();

    private final BufferedImage heart = AssetLoader.loadTexture("heart.png");

    // Entities
    private Camera camera;
    private Player player = new Player();
    private HashSet<Enemy> enemies = new HashSet<>();
    private HashSet<Projectile> playerProjectiles = new HashSet<>();
    private HashSet<Projectile> enemyProjectiles = new HashSet<>();
    private Map map = Map.fromFile("/test_map.txt").addLayer("/test_layer.txt");

    // game variables
    private final SpawnManager spawner = new SpawnManager(0, 2500, 0, 1500); // TODO: properly establish size
    boolean spawningEnabled = true;
    boolean triggerPlayerDefeat = false;
    boolean bossFightActive = false;
    private int maxEnemyCount = 100;
    private int points = 0;
    private Boss theBoss;

    public GameContext(double fps) {
        running = true;
        this.fps = fps;
        fixedDeltaTime = 1.0 / fps;
        renderer.addKeyListener(input);
        renderer.addMouseListener(input);
        renderer.addMouseMotionListener(input);

        camera = new Camera(new Point2D.Double(), .75, renderer.getWidth(), renderer.getHeight());

        // initial enemy set
        for (int i = 0; i < 45; i++) {
            Enemy e = spawner.spawnEnemy();
            if (e != null) enemies.add(e);
        }
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

        // update player
        player.update(dt);
        // System.out.println("Player: " + player.getX() + ", " + player.getY());
        // spawn new enemies
        if (enemies.size() < maxEnemyCount && spawningEnabled) {
            var newEnemies = spawner.spawnEnemies(dt);
            if (newEnemies != null)
                enemies.addAll(newEnemies);
        }

        // update enemies
        double snapshotX = player.getX();
        double snapshotY = player.getY();
        for (Enemy e : enemies) {
            e.update(dt, snapshotX, snapshotY);

            if (e.getDistance(player) > e.getRange())
                continue; // don't shoot if player is too far away
            var projectiles = e.shootProjectiles(e.angleTowards(player));
            if (projectiles != null) {
                enemyProjectiles.addAll(projectiles);
            }
        }

        // Move camera
        final var bbox = player.getBbox();

        camera.setViewSize(renderer.getWidth(), renderer.getHeight());
        if (!bossFightActive)
          camera.setPosition(bbox.x(), bbox.y());

        final var mouseWorld = camera.screenToWorld(input.getMouseX(), input.getMouseY());
        final var dx = mouseWorld.x - bbox.x();
        final var dy = mouseWorld.y - bbox.y();
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
                        player.setPoints(points);
                        maxEnemyCount = (points % 120000) * 10 + 100;
                        enemyIt.remove();
                    }
                    break;
                }
            }
            if (bossFightActive) {
                if (theBoss.getBbox().intersects(p.getBbox())) {
                    // projectile hits enemy
                    hit = true;
                    if (theBoss.takeDamage()) {
                        // enemy defeated
                        points += 5000000;
                        theBoss = null;
                    }
                    points += 200;
                }
            }

            if (expired || hit)
                it.remove();
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
                    // start boss fight
                    spawningEnabled = false;
                    triggerPlayerDefeat = true;
                    System.out.println("Player defeated!");
                }
            }
            if (expired || hit)
                it.remove();
        }

        // left click down -> create new player projectile
        if (input.isMouseDown(MouseEvent.BUTTON1)) {
            var ps = player.shootProjectiles(player.rotation);
            if (ps != null) playerProjectiles.addAll(ps);
        }

        // set up boss fight
        if (triggerPlayerDefeat) {
            triggerPlayerDefeat = false;
            enemyProjectiles.clear();
            playerProjectiles.clear();
            enemies.clear();
            camera.setPosition(1250, 1050);
            theBoss = new Boss();
            bossFightActive = true;
        }

        if (bossFightActive) {
            Rectangle2D.Double bounds = camera.getVisibleBounds();
            double cx = player.getX();
            double cy = player.getY();
            if (cx < bounds.x || cx > bounds.x + bounds.width || cy - 250 < bounds.y || cy > bounds.y + bounds.height) {
                double pdx = theBoss.getX() - player.getX();
                double pdy = theBoss.getY() + 500 - player.getY();

                double magnitude = Math.sqrt(pdx * pdx + pdy * pdy);

                pdx /= magnitude;
                pdy /= magnitude;

                player.move(pdx, pdy, dt * 2);
            }
        }
    }

    private void render() {
        try (final var r = renderer.getContext()) {
            r.setColor(Color.BLACK);
            r.clearBackground();

            // world drawing
            r.useCamera(camera);

            // r.drawImage(bg, 0, 0);
            r.drawMap(map);

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
            if (theBoss != null)
                r.drawImage(theBoss);

            // ui drawing
            r.useScreenSpace();

            r.drawString("SCORE: " + points, 0, 24);

            for (int i = 0; i < player.getHealth(); i++) {
                r.drawImage(heart, 20 * i, 30, 30, 30);
            }
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
