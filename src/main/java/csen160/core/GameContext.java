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
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

/**
 * Coordinates the core game loop, fixed-timestep simulation updates, rendering
 * pipeline,
 * entity management, collision handling, and state transitions for the game.
 */
public class GameContext {

    /**
     * Represents the high-level execution states of the game.
     */
    enum GameState {
        /** Title screen state awaiting player input to begin a game session. */
        MENU,
        /**
         * Active gameplay state in which world simulation, combat, and boss fights run.
         */
        GAME,
        /** Terminal end-of-game state. */
        END,
    }

    // engine variables
    /** Indicates whether the main game loop should continue running. */
    private boolean running;

    /** Target simulation update rate in ticks/frames per second. */
    private double fps;

    /** Fixed time delta per simulation step, in seconds ({@code 1.0 / fps}). */
    private double fixedDeltaTime;

    /** Input manager polling keyboard and mouse events from the renderer canvas. */
    private final InputManager input = new InputManager();

    /** Double-buffered canvas responsible for rendering world and UI graphics. */
    private final Renderer renderer = new Renderer();

    /** HUD icon texture representing one unit of player health. */
    private final BufferedImage heart = AssetLoader.loadTexture("heart.png");

    /** Current state of the game state machine. */
    private GameState state = GameState.MENU;

    // Entities
    /** Viewport camera handling world-to-screen transforms and visible bounds. */
    private Camera camera;

    /** The player-controlled ship entity. */
    private Player player = new Player();

    /** Active enemy ship entities currently present in the world. */
    private HashSet<Enemy> enemies = new HashSet<>();

    /** Active projectiles fired by the player. */
    private HashSet<Projectile> playerProjectiles = new HashSet<>();

    /** Active projectiles fired by enemies or the boss. */
    private HashSet<Projectile> enemyProjectiles = new HashSet<>();

    /** Active temporary visual effects such as hit explosions. */
    private HashSet<VisualEffect> effects = new HashSet<>();

    /**
     * Multi-layered tile map defining terrain visuals and solid collision tiles.
     */
    private Map map = Map.fromFile("/ocean_map_large_ground.txt").addLayer("/ocean_map_large_objects.txt");

    // game variables
    /** Total width of the loaded map in world pixels. */
    int mapSizeX = map.getMapSize().width;

    /** Total height of the loaded map in world pixels. */
    int mapSizeY = map.getMapSize().height;

    /** Manager responsible for spawning enemies within the map boundaries. */
    private final SpawnManager spawner = new SpawnManager(0, mapSizeX, 0, mapSizeY);

    /** Controls whether periodic enemy wave spawning is currently enabled. */
    boolean spawningEnabled = true;

    /** Maximum number of concurrent enemies permitted in the world. */
    private int maxEnemyCount = 240;

    /** Current score accumulated by the player. */
    private int points = 0;

    /**
     * Flag indicating the player was defeated in standard play and should
     * transition to the boss fight.
     */
    boolean triggerPlayerDefeat = false;

    /** Indicates whether the boss arena encounter is currently active. */
    boolean bossFightActive = false;

    /**
     * The active boss entity during a boss fight, or {@code null} when inactive or
     * defeated.
     */
    private Boss theBoss;

    /**
     * Constructs a new {@code GameContext} configured to run at the specified tick
     * rate.
     * Registers input listeners on the {@link Renderer}, initializes the
     * {@link Camera} and map
     * bindings, starts looping background music, and populates the initial set of
     * enemies.
     *
     * @param fps the target simulation update rate in frames per second
     */
    public GameContext(double fps) {
        running = true;
        this.fps = fps;
        fixedDeltaTime = 1.0 / fps;
        renderer.addKeyListener(input);
        renderer.addMouseListener(input);
        renderer.addMouseMotionListener(input);
        renderer.setFocusable(true);

        camera = new Camera(new Point2D.Double(), .75, renderer.getWidth(), renderer.getHeight());
        player.attachMap(map);
        Enemy.setMap(map);

        final var music = AssetLoader.loadSound("/music.wav");
        final var control = (FloatControl) music.getControl(FloatControl.Type.MASTER_GAIN);
        control.setValue(-12.0f);
        music.setFramePosition(0);
        music.loop(Clip.LOOP_CONTINUOUSLY);

        // initial enemy set
        for (int i = 0; i < 80; i++) {
            Enemy e = spawner.spawnEnemy();
            if (e != null)
                enemies.add(e);
        }
    }

    /**
     * Constructs a new {@code GameContext} with a default simulation rate of
     * {@code 60.0} FPS.
     */
    public GameContext() {
        this(60.0);
    }

    /**
     * Returns the {@link Renderer} component used to display the game.
     *
     * @return the game's {@link Renderer} instance
     */
    public Renderer getRenderer() {
        return renderer;
    }

    /**
     * Returns the fixed simulation time step duration per tick.
     *
     * @return the fixed delta time in seconds
     */
    public double getDeltaTime() {
        return fixedDeltaTime;
    }

    /**
     * Starts and executes the main fixed-timestep game loop until {@link #running}
     * is set to
     * {@code false}. Accumulates elapsed wall-clock time to step {@link #update()}
     * at a steady
     * rate while rendering each frame and yielding briefly via {@link #sleep()}.
     */
    public void run() {
        renderer.requestFocusInWindow();
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

    /**
     * Advances the game simulation by one fixed time step.
     * Handles state transitions, WASD player movement, mouse aiming, enemy spawning
     * and AI,
     * camera clamping, projectile movement and collision checks, visual effect
     * lifespans,
     * and boss fight setup and arena bounds.
     */
    private void update() {
        if (input.isKeyDown(KeyEvent.VK_ESCAPE)) {
            running = false;
            return;
        }

        if (state == GameState.MENU) {
            if (input.isKeyDown(KeyEvent.VK_SPACE)) {
                state = GameState.GAME;
                points = 0;
                return;
            }
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
        final var centerX = player.getX() + bbox.w() / 2.0;
        final var centerY = player.getY() + bbox.h() / 2.0;

        camera.setViewSize(renderer.getWidth(), renderer.getHeight());

        if (!bossFightActive) {
            Rectangle2D.Double bounds = camera.getVisibleBounds();

            double halfWidth = bounds.width / 2.0;
            double halfHeight = bounds.height / 2.0;

            double x = Math.clamp(centerX, halfWidth, mapSizeX - halfWidth);

            double y = Math.clamp(centerY, halfHeight, mapSizeY - halfHeight);

            if (bounds.width >= 2500)
                x = mapSizeX / 2.0;
            if (bounds.height >= 1250)
                y = mapSizeY / 2.0;

            camera.setPosition(x, y);
        }

        // rotate player towards mouse
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
                        player.setPoints(points);
                        maxEnemyCount = (points % 120000) * 15 + 240;
                        enemyIt.remove();
                    }
                    effects.add(
                            new VisualEffect("/PiratePack/PNG/Effects/explosion1.png", 25, 25, p.getX(), p.getY(), .4));
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
                        // TODO: call game over here also --> but its a victory
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
                    if (!bossFightActive) {
                        spawningEnabled = false;
                        triggerPlayerDefeat = true;
                    } else {
                        state = GameState.MENU;
                        player.reset();
                        player.teleport(256, 256);
                        bossFightActive = false;
                        spawningEnabled = true;
                        triggerPlayerDefeat = false;
                    }
                }
                effects.add(new VisualEffect("/PiratePack/PNG/Effects/explosion3.png", 20, 20, p.getX(), p.getY(), .4));
            }
            if (expired || hit)
                it.remove();
        }

        // update effects -> remove if duration expired
        effects.removeIf(e -> e.update(dt));

        // left click down -> create new player projectile
        if (input.isMouseDown(MouseEvent.BUTTON1)) {
            var ps = player.shootProjectiles(player.rotation);
            if (ps != null)
                playerProjectiles.addAll(ps);
        }

        // set up boss fight
        if (triggerPlayerDefeat) {
            triggerPlayerDefeat = false;
            enemyProjectiles.clear();
            playerProjectiles.clear();
            enemies.clear();
            theBoss = new Boss();
            camera.setPosition(theBoss.getX(), theBoss.getY());
            player.teleport(theBoss.getX(), theBoss.getY() + 200);
            player.setHealth(3);
            bossFightActive = true;
        }

        if (bossFightActive) {
            // keep player in boss arena
            Rectangle2D.Double bounds = camera.getVisibleBounds();

            double cx = player.getX();
            double cy = player.getY();

            double marginX = player.getWidth() / 2.0;
            double marginY = player.getHeight() / 2.0;

            double minX = bounds.x + marginX;
            double maxX = bounds.x + bounds.width - marginX;

            double minY = bounds.y + marginY;
            double maxY = bounds.y + bounds.height - marginY;

            cx = Math.clamp(cx, minX, maxX);
            cy = Math.clamp(cy, minY, maxY);

            player.teleport((int) cx, (int) cy);

            theBoss.update(dt);
            var ps = theBoss.shootProjectiles(theBoss.angleTowards(player));
            if (ps != null)
                enemyProjectiles.addAll(ps);
        }
    }

    /**
     * Renders the current frame to the {@link Renderer} buffer.
     * In {@link GameState#MENU}, draws the background map and start prompt;
     * otherwise
     * renders all world-space entities under the active {@link Camera} transform
     * followed
     * by screen-space HUD elements (score and player health hearts).
     */
    private void render() {
        try (final var r = renderer.getContext()) {
            if (state == GameState.MENU) {
                r.drawMap(map);
                r.setColor(Color.BLACK);
                r.drawString(String.format("Previous game score: %d\n", points), 200, 100);
                r.drawString("PRESS SPACE TO START!", 200, 200);
                return;
            }

            r.setColor(Color.BLACK);
            r.clearBackground();

            // world drawing
            r.useCamera(camera);

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
            for (final var ve : effects) {
                r.drawImage(ve);
            }

            // ui drawing
            r.useScreenSpace();

            r.drawString("SCORE: " + points, 4, 28);

            for (int i = 0; i < player.getHealth(); i++) {
                r.drawImage(heart, 20 * i, 36, 30, 30);
            }
        }
    }

    /**
     * Pauses the game loop thread for 1 millisecond to avoid busy-waiting CPU
     * usage.
     * Restores the interrupt flag and halts the loop if interrupted.
     */
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
