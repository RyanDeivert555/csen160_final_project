package csen160.entity;

import csen160.core.BBox;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.HashSet;

/**
 * Represents an AI-controlled enemy ship entity in the game world.
 * <p>
 * Handles state-based waypoint steering (wandering, approaching, retreating,
 * and strafing)
 * with map obstacle avoidance, firing cooldown management, and configurable
 * multi-projectile
 * attack patterns.
 */
public class Enemy extends Entity {

    /** Firing rate in volleys per second. */
    double fireRate = 1.0;

    /** Elapsed time in seconds since the enemy last fired. */
    double fireTimer;

    /**
     * Indicates whether the weapon cooldown has elapsed and the enemy is ready to
     * fire.
     */
    boolean canFire;

    /**
     * Attack pattern identifier ({@code "DEFAULT"}, {@code "CIRCLE"},
     * {@code "ARC"}, {@code "WALL"}, or {@code "RANDOM"}).
     */
    String projectilePattern = "DEFAULT";

    /**
     * Whether projectiles are fired at completely random angles instead of aiming
     * toward the target.
     */
    boolean randomAim;

    /** Speed of projectiles fired by this enemy, in pixels per second. */
    double projectileSpeed = 150.0;

    /** Engagement and detection radius in world pixels. */
    double range = 400.0;

    /** Current navigation waypoint x-coordinate in world space. */
    private double targetX;

    /** Current navigation waypoint y-coordinate in world space. */
    private double targetY;

    /** Countdown timer in seconds until a new navigation waypoint is chosen. */
    private double movementTimer;

    /**
     * Shared {@link Map} instance used by all enemies for obstacle and terrain
     * collision checks.
     */
    private static Map map;

    /**
     * Assigns the shared world {@link Map} used by all {@code Enemy} instances for
     * movement collision and spawn validation.
     *
     * @param m the active world {@link Map}
     */
    public static void setMap(Map m) {
        map = m;
    }

    /**
     * Checks whether this enemy's current position and dimensions overlap a solid
     * tile
     * on the shared {@link Map}.
     *
     * @return {@code true} if the enemy is blocked by map geometry; {@code false}
     *         otherwise
     */
    public boolean isBlocked() {
        return map.isBlocked(getX(), getY(), getWidth(), getHeight());
    }

    /**
     * Steering modes used to compute navigation waypoints relative to the player.
     */
    private enum MovementBehavior {
        /** Random local movement independent of the player's position. */
        WANDER,
        /** Movement closing distance toward the player. */
        APPROACH,
        /** Movement increasing distance away from the player. */
        RETREAT,
        /** Lateral movement perpendicular to the vector toward the player. */
        STRAFE
    }

    /**
     * Sign multiplier ({@code 1.0} or {@code -1.0}) determining lateral strafe
     * direction.
     */
    private double strafeDirection = 1.0;

    /**
     * Constructs a default placeholder {@code Enemy} using {@code "/temp.png"}.
     */
    public Enemy() {
        super("/temp.png");
    }

    /**
     * Constructs an {@code Enemy} archetype with explicit dimensions, health,
     * movement speed,
     * and weapon parameters.
     *
     * @param imagePath         classpath resource path to the enemy's sprite
     *                          texture
     * @param width             entity width in pixels
     * @param height            entity height in pixels
     * @param maxHealth         maximum and initial health points
     * @param speed             movement speed in pixels per second
     * @param projectilePattern firing pattern name ({@code "DEFAULT"},
     *                          {@code "CIRCLE"},
     *                          {@code "ARC"}, {@code "WALL"}, or {@code "RANDOM"})
     * @param fireRate          attack rate in volleys per second
     * @param projectileSpeed   speed of spawned projectiles in pixels per second
     * @param randomAim         {@code true} to randomize firing angles across 360
     *                          degrees
     */
    public Enemy(String imagePath, int width, int height, int maxHealth, double speed, String projectilePattern,
            double fireRate, double projectileSpeed, boolean randomAim) {
        super(imagePath, width, height, maxHealth, speed);
        this.projectilePattern = projectilePattern;
        this.fireRate = fireRate;
        this.randomAim = randomAim;
        this.projectileSpeed = projectileSpeed;
    }

    /**
     * Copy constructor that clones the sprite, dimensions, health, speed, and
     * firing
     * configuration from a prototype {@code Enemy}.
     *
     * @param other the prototype {@code Enemy} to copy attributes from
     */
    public Enemy(Enemy other) {
        super("/temp.png", other.width, other.height, other.maxHealth, other.speed);
        this.img = other.img;
        this.projectilePattern = other.projectilePattern;
        this.fireRate = other.fireRate;
        this.randomAim = other.randomAim;
        this.projectileSpeed = other.projectileSpeed;
    }

    /**
     * Advances the enemy's firing timer and AI movement for one simulation step.
     * Periodically selects waypoints based on proximity to the player, moves toward
     * the active waypoint while resolving map collisions, and updates sprite
     * rotation.
     *
     * @param dt      elapsed time since the last frame, in seconds
     * @param playerX the player's current x-coordinate in world space
     * @param playerY the player's current y-coordinate in world space
     */
    public void update(double dt, double playerX, double playerY) {
        // Firing timer
        if (!canFire && fireTimer >= 1.0 / fireRate) {
            canFire = true;
        } else {
            fireTimer += dt;
        }

        double distanceToPlayer = Point2D.distance(
                xPos, yPos,
                playerX, playerY);

        boolean playerNearby = distanceToPlayer <= range;

        // Choose new waypoint periodically
        movementTimer -= dt;

        double distanceToTarget = Point2D.distance(
                xPos, yPos,
                targetX, targetY);

        if (movementTimer <= 0 || distanceToTarget < 10) {
            chooseWaypoint(playerX, playerY, playerNearby);
            double movementInterval = 2.0;
            movementTimer = movementInterval + Math.random() * 2.0;
        }

        // Move toward waypoint
        double dx = targetX - xPos;
        double dy = targetY - yPos;

        double length = Math.hypot(dx, dy);

        if (length > 0) {
            double moveX = dx / length;
            double moveY = dy / length;

            final double stepX = moveX * speed * dt;
            final double stepY = moveY * speed * dt;

            if (map == null) {
                xPos += stepX;
                yPos += stepY;
            } else {
                final double oldX = xPos;
                final double oldY = yPos;
                xPos = map.moveX(oldX, oldY, width, height, stepX);
                yPos = map.moveY(xPos, oldY, width, height, stepY);

                // Hit land or a rock: pick a new waypoint next frame instead of pushing into it
                if (Math.abs(xPos - oldX - stepX) > 1e-9 || Math.abs(yPos - oldY - stepY) > 1e-9) {
                    movementTimer = 0;
                }
            }

            rotation = Math.toDegrees(Math.atan2(moveY, moveX)) + 270;
        }
    }

    /**
     * Selects a new navigation waypoint {@code (targetX, targetY)} using weighted
     * {@link MovementBehavior} states when the player is within {@link #range}, or
     * random wandering otherwise. Retries up to 8 random offsets if the chosen
     * point is blocked.
     *
     * @param playerX      the player's x-coordinate in world space
     * @param playerY      the player's y-coordinate in world space
     * @param playerNearby {@code true} if the player is within {@link #range}
     */
    private void chooseWaypoint(double playerX, double playerY, boolean playerNearby) {
        double angle;
        double distance;

        MovementBehavior behavior = MovementBehavior.WANDER;
        if (playerNearby) {
            double roll = Math.random();

            if (roll < 0.4) {
                behavior = MovementBehavior.STRAFE;
            } else if (roll < 0.65) {
                behavior = MovementBehavior.APPROACH;
            } else if (roll < 0.85) {
                behavior = MovementBehavior.RETREAT;
            }

            if (Math.random() < 0.5) {
                strafeDirection *= -1;
            }
        }

        angle = Math.random() * Math.PI * 2;
        distance = 75 + Math.random() * 150;

        switch (behavior) {
            case APPROACH:
                targetX = xPos + (playerX - xPos) * 0.4;
                targetY = yPos + (playerY - yPos) * 0.4;
                break;

            case RETREAT:
                double dx = xPos - playerX;
                double dy = yPos - playerY;
                double length = Math.max(1, Math.hypot(dx, dy));

                targetX = xPos + dx / length * distance;
                targetY = yPos + dy / length * distance;
                break;

            case STRAFE:
                double px = playerX - xPos;
                double py = playerY - yPos;
                double len = Math.max(1, Math.hypot(px, py));

                targetX = xPos - py / len * distance * strafeDirection;
                targetY = yPos + px / len * distance * strafeDirection;
                break;

            case WANDER:
            default:
                targetX = xPos + Math.cos(angle) * distance;
                targetY = yPos + Math.sin(angle) * distance;
                break;
        }

        // Do not aim at land: retry a few random wander targets, otherwise stay put
        if (map != null && map.isBlocked(targetX, targetY, width, height)) {
            for (int i = 0; i < 8; i++) {
                final double a = Math.random() * Math.PI * 2;
                final double d = 75 + Math.random() * 150;
                targetX = xPos + Math.cos(a) * d;
                targetY = yPos + Math.sin(a) * d;
                if (!map.isBlocked(targetX, targetY, width, height)) {
                    return;
                }
            }
            targetX = xPos;
            targetY = yPos;
        }
    }

    /**
     * Spawns a single enemy {@link Projectile} at this enemy's current center
     * position.
     *
     * @param deg the firing angle in degrees
     * @return a newly created {@link Projectile} configured with
     *         {@code "/evilProjectile.png"}
     *         and {@link #projectileSpeed}
     */
    @Override
    public Projectile shootProjectile(double deg) {
        return new Projectile("/evilProjectile.png", xPos, yPos,
                deg, projectileSpeed, 7, 10, 10);
    }

    /**
     * Spawns a single enemy {@link Projectile} at the specified world coordinates.
     *
     * @param deg the firing angle in degrees
     * @param x   the spawn x-coordinate in world space
     * @param y   the spawn y-coordinate in world space
     * @return a newly created {@link Projectile} configured with
     *         {@code "/evilProjectile.png"}
     *         and {@link #projectileSpeed}
     */
    public Projectile shootProjectile(double deg, double x, double y) {
        return new Projectile("/evilProjectile.png", x, y,
                deg, projectileSpeed, 7, 10, 10);
    }

    /**
     * Fires a volley of {@link Projectile} instances according to
     * {@link #projectilePattern}
     * if the enemy's firing cooldown has elapsed.
     *
     * @param deg the base aim angle toward the target in degrees
     * @return a {@link HashSet} of spawned {@link Projectile} instances, or
     *         {@code null}
     *         if the weapon is still on cooldown
     */
    public HashSet<Projectile> shootProjectiles(double deg) {

        if (!canFire)
            return null;
        canFire = false;
        fireTimer = 0;

        HashSet<Projectile> projectiles = new HashSet<>();

        double modifiedDeg = Math.random() * 10.0 - 5.0 + deg;
        if (randomAim)
            modifiedDeg = Math.random() * 360.0;

        switch (projectilePattern) {
            case "CIRCLE":
                final int circleCount = 8;
                for (int i = 0; i < circleCount; i++) {
                    projectiles.add(shootProjectile(modifiedDeg + (360.0 * i / circleCount)));
                }
                break;
            case "ARC":
                final int arcCount = 5;
                final double arcSpread = 90.0;
                for (int i = 0; i < arcCount; i++) {
                    double offset = (-arcSpread / 2.0) + i * arcSpread / (arcCount - 1);
                    projectiles.add(shootProjectile(modifiedDeg + offset, xPos, yPos));
                }
                break;
            case "WALL":
                final int wallCount = 5;
                final double spacing = 10.0;

                // get perpendicular angle
                double radians = Math.toRadians(modifiedDeg);
                double perpX = -Math.sin(radians);
                double perpY = Math.cos(radians);

                for (int i = 0; i < wallCount; i++) {
                    double offset = (i - (wallCount - 1) / 2.0) * spacing;
                    double spawnX = xPos + perpX * offset;
                    double spawnY = yPos + perpY * offset;
                    projectiles.add(shootProjectile(modifiedDeg, spawnX, spawnY));
                }
                break;
            case "RANDOM":
                for (int i = 0; i < (int) (Math.random() * 3) + 2; i++) {
                    double randomAngle = Math.random() * 360.0;
                    projectiles.add(shootProjectile(randomAngle));
                }
            default:
                projectiles.add(shootProjectile(modifiedDeg));
                break;
        }
        return projectiles;
    }

    /**
     * Returns the enemy's engagement and detection radius.
     *
     * @return the range in world pixels
     */
    public double getRange() {
        return this.range;
    }

    /**
     * Returns the axis-aligned bounding box representing this enemy's collision
     * area.
     *
     * @return a {@link BBox} constructed from the enemy's position and dimensions
     */
    @Override
    public BBox getBbox() {
        return new BBox(xPos, yPos, this.width, this.height);
    }

    /**
     * Renders the enemy sprite rotated around {@code (xPos, yPos)} onto the
     * provided
     * graphics context.
     *
     * @param ctx the {@link Graphics2D} context on which to render the enemy
     */
    @Override
    public void render(Graphics2D ctx) {
        final var tempCtx = (Graphics2D) ctx.create();

        tempCtx.rotate(Math.toRadians(rotation), xPos, yPos);

        tempCtx.drawImage(img, (int) xPos - width / 2, (int) yPos - height / 2, this.width, this.height, null);

        tempCtx.dispose();
    }
}
