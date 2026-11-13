package csen160.entity;

import csen160.core.AssetLoader;
import csen160.core.BBox;

import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Represents the stationary boss entity encountered during the final boss fight
 * phase.
 * <p>
 * Cycles through six distinct bullet-hell projectile patterns on a timed
 * schedule
 * and periodically plays amplified vocal squawk audio cues.
 */
public class Boss extends Entity {

    /** Elapsed time in seconds since the boss last fired a volley. */
    double fireTimer;

    /**
     * Indicates whether the weapon cooldown has elapsed and the boss is ready to
     * fire.
     */
    boolean canFire;

    /** Speed of projectiles fired by the boss, in pixels per second. */
    double projectileSpeed = 150.0;

    /**
     * Index of the currently active attack pattern ({@code 0} through {@code 5}).
     */
    int currentAttack = 0;

    /** Elapsed time in seconds spent in the current attack phase. */
    double currentAttackTimer = 0;

    /**
     * Duration in seconds for each attack phase before advancing to the next
     * pattern.
     */
    double[] attackLengths = { 10.0, 10.0, 10.0, 10.0, 10.0, 10.0 };

    /** Firing rate in volleys per second corresponding to each attack phase. */
    double[] fireRates = { 3.0, 5.0, 20.0, 1.5, 4.5, 12.0 };

    /**
     * Cumulative angular offset in degrees used to rotate radial and spiral
     * patterns between volleys.
     */
    double attackRotation = 0;

    /**
     * Elapsed time in seconds since the last boss squawk sound effect was
     * triggered.
     */
    double squawkTimer = 0;

    /**
     * Preloaded pool of squawk sound clips played periodically during the
     * encounter.
     */
    static List<Clip> BOSS_SOUNDS;

    static {
        final var sounds = new ArrayList<Clip>();
        sounds.add(AssetLoader.loadSound("/squawk1.wav"));
        sounds.add(AssetLoader.loadSound("/squawk2.wav"));
        sounds.add(AssetLoader.loadSound("/squawk3.wav"));
        BOSS_SOUNDS = sounds;
    }

    /**
     * Constructs the {@code Boss} using {@code "/boss.png"} with dimensions
     * {@code 150x150},
     * {@code 50,000} health, zero movement speed, and a fixed arena position at
     * {@code (3840, 2304)}.
     */
    public Boss() {
        super("/boss.png", 150, 150, 50000, 0);

        this.xPos = 3840;
        this.yPos = 2304;
    }

    /**
     * Advances the boss's firing cooldown, attack-phase rotation timer, and
     * periodic
     * vocal squawk timer for one simulation tick.
     *
     * @param dt the elapsed time since the last frame, in seconds
     */
    public void update(double dt) {
        fireTimer += dt;
        if (!canFire && fireTimer >= 1.0 / fireRates[currentAttack]) {
            canFire = true;
        }

        currentAttackTimer += dt;
        if (currentAttackTimer >= attackLengths[currentAttack]) {
            currentAttack = (currentAttack + 1) % attackLengths.length;
            currentAttackTimer = 0;
            attackRotation = 0;
        }

        squawkTimer += dt;
        if (squawkTimer >= Math.random() * 3 + 5) {
            squawkTimer = 0;
            final var idx = ThreadLocalRandom.current().nextInt(BOSS_SOUNDS.size());
            final var clip = BOSS_SOUNDS.get(idx);

            if (clip.isRunning()) {
                clip.stop();
            }

            // increase volume
            if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                final var volume = (FloatControl) clip.getControl(
                        FloatControl.Type.MASTER_GAIN);

                volume.setValue(Math.min(6.0f, volume.getMaximum()));
            }
            clip.setFramePosition(0);
            clip.start();
        }
    }

    /**
     * Spawns a single boss {@link Projectile} at the boss's center position.
     *
     * @param deg the firing angle in degrees
     * @return a newly created {@link Projectile} configured with
     *         {@code "/evilProjectile.png"},
     *         {@link #projectileSpeed}, a 10-second lifetime, and {@code 10x10}
     *         dimensions
     */
    @Override
    public Projectile shootProjectile(double deg) {
        return new Projectile("/evilProjectile.png", xPos, yPos,
                deg, projectileSpeed, 10, 10, 10);
    }

    /**
     * Spawns a single boss {@link Projectile} at the specified world coordinates.
     *
     * @param deg the firing angle in degrees
     * @param x   the spawn x-coordinate in world space
     * @param y   the spawn y-coordinate in world space
     * @return a newly created {@link Projectile} configured with
     *         {@code "/evilProjectile.png"},
     *         {@link #projectileSpeed}, a 10-second lifetime, and {@code 10x10}
     *         dimensions
     */
    public Projectile shootProjectile(double deg, double x, double y) {
        return new Projectile("/evilProjectile.png", x, y,
                deg, projectileSpeed, 10, 10, 10);
    }

    /**
     * Fires a volley of {@link Projectile} instances corresponding to
     * {@link #currentAttack}
     * if the weapon cooldown has elapsed:
     * <ul>
     * <li><b>0 (Flower Burst):</b> 24 projectiles in a full 360-degree ring,
     * rotating by {@code +12°} per volley.</li>
     * <li><b>1 (Aimed Fan):</b> 7 projectiles spread evenly across a {@code 60°}
     * arc toward the target.</li>
     * <li><b>2 (Rotating Spiral):</b> 4-arm radial spiral rotating by {@code +15°}
     * per volley.</li>
     * <li><b>3 (Bullet Wall):</b> 9-shot perpendicular line spaced {@code 30}
     * pixels apart with a navigable center gap.</li>
     * <li><b>4 (Butterfly):</b> Symmetric pairs of 6 projectiles fanning outward up
     * to {@code ±65°}.</li>
     * <li><b>5 (Rainfall):</b> 3 to 6 projectiles fired in random directions across
     * {@code 360°}.</li>
     * </ul>
     *
     * @param deg the base aim angle toward the player in degrees
     * @return a {@link HashSet} containing the newly spawned projectiles, or an
     *         empty set
     *         if the boss is still on firing cooldown
     */
    public HashSet<Projectile> shootProjectiles(double deg) {
        if (!canFire)
            return new HashSet<>();
        ;
        canFire = false;
        fireTimer = 0;

        HashSet<Projectile> projectiles = new HashSet<>();

        double modifiedDeg = Math.random() * 10.0 - 5.0 + deg;

        switch (currentAttack) {
            case 0: // Flower Burst
                int count = 24;

                for (int i = 0; i < count; i++) {
                    double angle = modifiedDeg + attackRotation
                            + (360.0 * i / count);

                    projectiles.add(shootProjectile(angle));
                }

                attackRotation += 12;
                break;

            case 1: // Aimed Fan
                int fanCount = 7;
                double fanSpread = 60;

                for (int i = 0; i < fanCount; i++) {
                    double offset = -fanSpread / 2
                            + i * fanSpread / (fanCount - 1);

                    projectiles.add(shootProjectile(modifiedDeg + offset));
                }
                break;

            case 2: // Rotating Spiral
                int spiralCount = 4;

                for (int i = 0; i < spiralCount; i++) {
                    double angle = modifiedDeg
                            + attackRotation
                            + (360.0 * i / spiralCount);

                    projectiles.add(shootProjectile(angle));
                }

                attackRotation += 15;
                break;

            case 3: // Bullet Wall
                int wallCount = 9;
                double spacing = 30;

                double radians = Math.toRadians(modifiedDeg);
                double perpX = -Math.sin(radians);
                double perpY = Math.cos(radians);

                for (int i = 0; i < wallCount; i++) {
                    // Leave a gap in the center
                    if (i == 4)
                        continue;

                    double offset = (i - (wallCount - 1) / 2.0)
                            * spacing;

                    projectiles.add(shootProjectile(
                            modifiedDeg,
                            xPos + perpX * offset,
                            yPos + perpY * offset));
                }
                break;

            case 4: // Butterfly
                int butterflyCount = 6;
                double butterflySpread = 65;

                for (int i = 0; i < butterflyCount; i++) {
                    double offset = i * butterflySpread / (butterflyCount - 1);

                    projectiles.add(shootProjectile(
                            modifiedDeg + offset));

                    projectiles.add(shootProjectile(
                            modifiedDeg - offset));
                }
                break;
            case 5: // Rainfall
                int rainCount = (int) (Math.random() * 4) + 3;

                for (int i = 0; i < rainCount; i++) {
                    double angle = Math.random() * 360;

                    projectiles.add(shootProjectile(angle));
                }
                break;
        }
        return projectiles;
    }

    /**
     * Returns the axis-aligned bounding box representing the boss's collision area.
     *
     * @return a {@link BBox} constructed from the boss's position and dimensions
     */
    @Override
    public BBox getBbox() {
        return new BBox(xPos, yPos, this.width, this.height);
    }

    /**
     * Renders the boss sprite rotated around {@code (xPos, yPos)} onto the provided
     * graphics context.
     *
     * @param ctx the {@link Graphics2D} context on which to render the boss
     */
    @Override
    public void render(Graphics2D ctx) {
        final var tempCtx = (Graphics2D) ctx.create();

        tempCtx.rotate(Math.toRadians(rotation), xPos, yPos);

        tempCtx.drawImage(img, (int) xPos - width / 2, (int) yPos - height / 2, this.width, this.height, null);

        tempCtx.dispose();
    }

}
