package csen160.entity;

import csen160.core.*;

import java.awt.*;
import java.util.HashSet;

/**
 * Represents the player-controlled ship entity in the game.
 * <p>
 * Manages map-aware movement and collision resolution, weapon firing cooldowns
 * and
 * multi-projectile volley formations, score-based health and weapon milestones,
 * and rotated sprite rendering.
 */
public class Player extends Entity {

    /** Classpath resource path to the player ship's sprite image. */
    private static String path = "/player.png";

    /** Maximum firing rate in volleys per second. */
    private final double fireRate = 18.0; // per second

    /** Elapsed time in seconds since the player last fired. */
    private double fireTimer = 0;

    /**
     * Indicates whether the weapon cooldown has elapsed and the player may fire.
     */
    private boolean canFire = false;

    /**
     * Number of projectiles spawned per volley (scales from 1 up to 4 based on
     * score).
     */
    private int projectileCount = 1;

    /** Width of the player's collision hitbox in pixels. */
    private final int playerWidth = 15;

    /** Height of the player's collision hitbox in pixels. */
    private final int playerHeight = 15;

    /**
     * Visual scaling multiplier applied to the hitbox dimensions when rendering the
     * sprite.
     */
    private final double imageScale = 3.0;

    /** Current score accumulated by the player. */
    private int points = 0;

    /**
     * Score threshold of the last awarded health regeneration milestone (increments
     * by 250,000).
     */
    private int lastHealthMilestone = 0;

    /**
     * Score threshold of the last awarded projectile count upgrade milestone
     * (increments by 100,000).
     */
    private int lastProjectileMilestone = 0;

    /**
     * Reference to the game {@link Map} used for collision detection during
     * movement.
     */
    private Map map;

    /**
     * Constructs a new {@code Player} using {@code "/player.png"}, initializes
     * maximum health
     * to {@code 10} and current health to {@code 7}, and spawns at world
     * coordinates {@code (256, 256)}.
     */
    public Player() {
        super(path);

        this.maxHealth = 10;
        this.health = 7;

        xPos = 256;
        yPos = 256;
    }

    /**
     * Attaches a {@link Map} instance to the player for terrain and object
     * collision handling.
     *
     * @param map the world {@link Map} to collide against during movement
     */
    public void attachMap(Map map) {
        this.map = map;
    }

    /**
     * Resets the player's maximum health to {@code 10} and current health to
     * {@code 7}.
     */
    public void reset() {
        this.maxHealth = 10;
        this.health = 7;
    }

    /**
     * Moves the player by the specified pixel displacements while resolving
     * collisions
     * independently along the X and Y axes against the given {@link Map}.
     *
     * @param map the {@link Map} used to check solid tile collisions
     * @param dx  the horizontal displacement in pixels
     * @param dy  the vertical displacement in pixels
     */
    public void moveAndCollide(Map map, double dx, double dy) {
        xPos = map.moveX(xPos, yPos, playerWidth, playerHeight, dx);
        yPos = map.moveY(xPos, yPos, playerWidth, playerHeight, dy);
    }

    /**
     * Moves the player in the normalized direction {@code (dx, dy)} scaled by speed
     * and {@code dt}.
     * Resolves axis-aligned map collisions if a {@link Map} is attached; otherwise
     * falls back to
     * {@link Entity#move(double, double, double)}.
     *
     * @param dx the horizontal movement direction
     * @param dy the vertical movement direction
     * @param dt the elapsed time since the last frame, in seconds
     */
    @Override
    public void move(double dx, double dy, double dt) {
        if (map == null) {
            super.move(dx, dy, dt);
            return;
        }

        final double magnitude = Math.sqrt(dx * dx + dy * dy);
        if (magnitude == 0)
            return;

        final double scale = speed * dt / magnitude;
        xPos = map.moveX(xPos, yPos, playerWidth, playerHeight, dx * scale);
        yPos = map.moveY(xPos, yPos, playerWidth, playerHeight, dy * scale);
    }

    /**
     * Updates the player's firing cooldown timer and enables {@link #canFire} once
     * the
     * fire interval ({@code 1.0 / fireRate}) has elapsed.
     *
     * @param dt the elapsed time since the last frame, in seconds
     */
    public void update(double dt) {
        //
        if (!this.canFire && this.fireTimer >= 1.0 / this.fireRate) {
            this.canFire = true;
        } else {
            this.fireTimer += dt;
        }
    }

    /**
     * Fires a single {@link Projectile} in the specified direction if the weapon
     * cooldown is ready.
     *
     * @param deg the firing angle in degrees
     * @return a newly created {@link Projectile}, or {@code null} if the weapon is
     *         still on cooldown
     */
    @Override
    public Projectile shootProjectile(double deg) {
        if (!this.canFire)
            return null;
        this.canFire = false;
        this.fireTimer = 0;

        return new Projectile("/projectile.png", xPos, yPos, deg, 500, 7, 10, 10);
    }

    /**
     * Fires a volley of {@code 1} to {@code 4} {@link Projectile} instances
     * oriented along {@code deg}
     * based on the player's current {@link #projectileCount}, if the weapon
     * cooldown is ready.
     *
     * @param deg the firing angle in degrees
     * @return a {@link HashSet} of newly spawned {@link Projectile} instances, or
     *         {@code null}
     *         if the weapon is still on cooldown
     */
    public HashSet<Projectile> shootProjectiles(double deg) {
        if (!this.canFire)
            return null;
        this.canFire = false;
        this.fireTimer = 0;

        var projectiles = new HashSet<Projectile>();

        double[][] offsets = switch (projectileCount) {
            case 2 -> new double[][] { { -5, 0 }, { 5, 0 } };
            case 3 -> new double[][] { { 0, 8 }, // Front
                    { -6, -6 }, // Back left
                    { 6, -6 } // Back right
            };
            case 4 -> new double[][] { { -5, 5 }, // Front left
                    { 5, 5 }, // Front right
                    { -10, -5 }, // Back left
                    { 10, -5 } // Back right
            };
            default -> new double[][] { { 0, 0 } };
        };

        double rad = Math.toRadians(deg);

        double perpendicularX = -Math.sin(rad);
        double perpendicularY = Math.cos(rad);

        double forwardX = Math.cos(rad);
        double forwardY = Math.sin(rad);

        for (double[] offset : offsets) {
            double x = xPos
                    + perpendicularX * offset[0]
                    + forwardX * offset[1];

            double y = yPos
                    + perpendicularY * offset[0]
                    + forwardY * offset[1];

            projectiles.add(new Projectile(
                    "/projectile.png",
                    x, y,
                    deg,
                    500, 7, 10, 10));
        }
        return projectiles;
    }

    /**
     * Updates the player's score and evaluates progression milestones:
     * restores {@code 1} health (up to {@link #maxHealth}) every {@code 250,000}
     * points and
     * increases {@link #projectileCount} (up to {@code 4}) every {@code 100,000}
     * points.
     *
     * @param points the player's updated total score
     */
    public void setPoints(int points) {
        this.points = points;
        if (points - lastHealthMilestone > 250000) {
            if (health < maxHealth)
                health++;
            lastHealthMilestone += 250000;
        }
        if (points - lastProjectileMilestone > 100000) {
            if (projectileCount < 4) {
                projectileCount++;
            }
            lastProjectileMilestone += 100000;
        }
    }

    /**
     * Configures the player's maximum health to {@code 3} and sets current health
     * to the specified value.
     * <p>
     * To be called at the start of the boss fight ONLY.
     *
     * @param health the starting health value for the boss fight
     */
    public void setHealth(int health) {
        this.maxHealth = 3;
        this.health = health;
    }

    /**
     * Returns the axis-aligned bounding box representing the player's collision
     * hitbox.
     *
     * @return a {@link BBox} constructed from {@code (xPos, yPos)} and
     *         {@code (playerWidth, playerHeight)}
     */
    @Override
    public BBox getBbox() {
        return new BBox(xPos, yPos, this.playerWidth, this.playerHeight);
    }

    /**
     * Renders the player's ship sprite rotated around {@code (xPos, yPos)} at
     * {@link #imageScale}
     * times the hitbox size, along with a green circular hitbox outline.
     *
     * @param ctx the {@link Graphics2D} context on which to render the player
     */
    @Override
    public void render(Graphics2D ctx) {
        // player is rendered twice as large as the hitbox
        final var tempCtx = (Graphics2D) ctx.create();

        tempCtx.rotate(Math.toRadians(rotation), this.xPos, this.yPos);

        int drawWidth = (int) (playerWidth * imageScale);
        int drawHeight = (int) (playerHeight * imageScale);
        tempCtx.drawImage(img, (int) (xPos - drawWidth / 2.0), (int) (yPos - drawHeight / 2.0), drawWidth,
                drawHeight, null);

        tempCtx.dispose();

        // draw hitbox
        ctx.setColor(Color.green);
        ctx.drawOval((int) xPos - playerWidth / 2, (int) yPos - playerHeight / 2, playerWidth,
                playerHeight);
        // Ellipse2D.Double circle = new Ellipse2D.Double(position.x - playerWidth /
        // 2.0, position.y - playerHeight / 2.0, playerWidth, playerHeight);
        // ctx.fill(circle);
    }
}
