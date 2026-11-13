package csen160.entity;

import csen160.core.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import javax.sound.sampled.Clip;

/**
 * Represents an abstract game entity with a position, size, health, and
 * movement speed.
 * Provides common functionality for movement, projectile creation,
 * damage handling, and distance/angle calculations. Subclasses are responsible
 * for defining their bounding box and rendering behavior.
 */
public abstract class Entity {
    /**
     * The entity's horizontal position in world coordinates.
     */
    double xPos;

    /**
     * The entity's vertical position in world coordinates.
     */
    double yPos;

    /**
     * The entity's width in pixels.
     */
    int width;

    /**
     * The entity's height in pixels.
     */
    int height;

    /**
     * The image used to render the entity.
     */
    BufferedImage img;

    /**
     * Preloaded pool of wood impact sound clips played randomly when an entity
     * takes damage.
     */
    static List<Clip> HIT_SOUNDS;
    static {
        final var sounds = new ArrayList<Clip>();
        for (int i = 0; i <= 4; i++) {
            final var path0 = String.format("/impactWood_light_%03d.wav", i);
            final var path1 = String.format("/impactWood_medium_%03d.wav", i);
            final var path2 = String.format("/impactWood_heavy_%03d.wav", i);

            sounds.add(AssetLoader.loadSound(path0));
            sounds.add(AssetLoader.loadSound(path1));
            sounds.add(AssetLoader.loadSound(path2));
        }

        HIT_SOUNDS = sounds;
    }

    /**
     * The entity's current rotation angle in degrees.
     */
    public double rotation;

    /**
     * The entity's maximum health.
     */
    int maxHealth;

    /**
     * The entity's current health.
     */
    int health;

    /**
     * The entity's movement speed in pixels per second.
     */
    double speed;

    /**
     * Constructs an {@code Entity} with the specified texture and default
     * attributes
     * (50x50 dimensions, 1 maximum health, and a speed of 200 pixels per second).
     *
     * @param imagePath the classpath resource path to the entity's texture image
     */
    public Entity(String imagePath) {
        this.img = AssetLoader.loadTexture(imagePath);

        this.width = 50;
        this.height = 50;

        this.maxHealth = 1; // default for now
        this.health = this.maxHealth;
        this.speed = 200; // default for now
    }

    /**
     * Constructs an {@code Entity} with the specified texture, dimensions, health,
     * and speed.
     *
     * @param imagePath the classpath resource path to the entity's texture image
     * @param width     the entity's width in pixels
     * @param height    the entity's height in pixels
     * @param maxHealth the entity's initial and maximum health
     * @param speed     the entity's movement speed in pixels per second
     */
    public Entity(String imagePath, int width, int height, int maxHealth, double speed) {
        this.img = AssetLoader.loadTexture(imagePath);
        this.width = width;
        this.height = height;
        this.maxHealth = maxHealth;
        this.health = this.maxHealth;
        this.speed = speed;
    }

    /**
     * Moves the entity in the specified direction.
     * The direction vector is normalized before movement so that diagonal movement
     * does not increase the entity's speed.
     *
     * @param dx the horizontal movement direction
     * @param dy the vertical movement direction
     * @param dt the elapsed time in seconds
     */
    public void move(double dx, double dy, double dt) {
        // normalize
        double magnitude = Math.sqrt(dx * dx + dy * dy);
        if (magnitude == 0)
            return;

        dx /= magnitude;
        dy /= magnitude;

        this.xPos += dx * speed * dt;
        this.yPos += dy * speed * dt;
    }

    /**
     * Instantaneously moves the entity to the specified world position.
     *
     * @param x the target horizontal coordinate in world space
     * @param y the target vertical coordinate in world space
     */
    public void teleport(int x, int y) {
        this.xPos = x;
        this.yPos = y;
    }

    /**
     * Creates a projectile originating from the entity's current position.
     *
     * @param deg the projectile's firing angle in degrees
     * @return the newly created projectile
     */
    public Projectile shootProjectile(double deg) {
        return new Projectile(this.xPos, this.yPos, deg);
    }

    /**
     * Plays a random hit sound effect and reduces the entity's health by one.
     *
     * @return {@code true} if the entity's health has reached zero or below;
     *         {@code false} otherwise
     */
    public boolean takeDamage() {
        final var idx = ThreadLocalRandom.current().nextInt(HIT_SOUNDS.size());
        final var clip = HIT_SOUNDS.get(idx);

        if (clip.isRunning()) {
            clip.stop();
        }
        clip.setFramePosition(0);
        clip.start();

        this.health--;
        return this.health <= 0;
    }

    /**
     * Calculates the angle from this entity toward another entity.
     *
     * @param other the target entity
     * @return the angle toward the target in degrees
     */
    public double angleTowards(Entity other) {
        final var dx = other.getX() - this.xPos;
        final var dy = other.getY() - this.yPos;
        return Math.toDegrees(Math.atan2(dy, dx));
    }

    /**
     * Calculates the Euclidean distance between this entity and another entity.
     *
     * @param other the entity to measure the distance to
     * @return the distance between the two entities in world coordinates
     */
    public double getDistance(Entity other) {
        double dx = xPos - other.xPos;
        double dy = yPos - other.yPos;
        return Math.sqrt(dx * dx + dy * dy);
    }

    /**
     * Returns the entity's horizontal position truncated to an integer.
     *
     * @return the entity's x-coordinate in world space
     */
    public int getX() {
        return (int) this.xPos;
    }

    /**
     * Returns the entity's vertical position truncated to an integer.
     *
     * @return the entity's y-coordinate in world space
     */
    public int getY() {
        return (int) this.yPos;
    }

    /**
     * Returns the entity's width.
     *
     * @return the width in pixels
     */
    public int getWidth() {
        return this.width;
    }

    /**
     * Returns the entity's height.
     *
     * @return the height in pixels
     */
    public int getHeight() {
        return this.height;
    }

    /**
     * Returns the entity's maximum health capacity.
     *
     * @return the maximum health value
     */
    public int getMaxHealth() {
        return this.maxHealth;
    }

    /**
     * Returns the entity's current remaining health.
     *
     * @return the current health value
     */
    public int getHealth() {
        return this.health;
    }

    /**
     * Gets the entity's bounding box, used for collision detection.
     *
     * @return the entity's bounding box
     */
    public abstract BBox getBbox();

    /**
     * Renders the entity using the specified graphics context.
     *
     * @param ctx the graphics context used for rendering
     */
    public abstract void render(Graphics2D ctx);
}
