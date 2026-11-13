package csen160.entity;

import csen160.core.*;

import java.awt.*;

/**
 * Represents a projectile entity that travels in a straight line at a fixed
 * angle
 * and expires once its maximum lifetime elapses.
 */
public class Projectile extends Entity {

    /**
     * The maximum lifespan of the projectile before it should be destroyed, in
     * seconds.
     */
    double lifetime;

    /** The direction of travel in degrees. */
    double direction; // degrees

    /** The elapsed time since the projectile was spawned, in seconds. */
    double timeSinceCreation = 0;

    /**
     * Constructs a default {@code Projectile} using {@code "/projectile.png"},
     * a speed of {@code 500}, and a lifetime of {@code 5} seconds.
     */
    public Projectile() {
        super("/projectile.png");
        this.speed = 500;
        this.lifetime = 5;
    }

    /**
     * Constructs a {@code Projectile} with the specified texture resource path,
     * a default speed of {@code 500}, and a lifetime of {@code 5} seconds.
     *
     * @param path the resource path to the projectile's sprite image
     */
    public Projectile(String path) {
        super(path);
        this.speed = 500;
        this.lifetime = 5;
    }

    /**
     * Constructs a fully customized {@code Projectile} with the specified texture,
     * position, trajectory, speed, lifespan, and dimensions.
     *
     * @param path      the resource path to the projectile's sprite image
     * @param x         the initial x-coordinate of the projectile's center
     * @param y         the initial y-coordinate of the projectile's center
     * @param direction the travel angle in degrees
     * @param speed     the movement speed in world units per second
     * @param lifetime  the maximum lifespan of the projectile in seconds
     * @param width     the rendered and bounding width of the projectile in pixels
     * @param height    the rendered and bounding height of the projectile in pixels
     */
    public Projectile(String path, double x, double y, double direction, double speed, double lifetime, int width,
            int height) {
        this(path);
        this.xPos = x;
        this.yPos = y;
        this.direction = direction;
        this.speed = speed;
        this.lifetime = lifetime;
        this.width = width;
        this.height = height;
    }

    /**
     * Constructs a default-styled {@code Projectile} at the specified position
     * moving in the given direction.
     *
     * @param x         the initial x-coordinate of the projectile's center
     * @param y         the initial y-coordinate of the projectile's center
     * @param direction the travel angle in degrees
     */
    public Projectile(double x, double y, double direction) {
        this();
        this.xPos = x;
        this.yPos = y;
        this.direction = direction;
    }

    /**
     * Advances the projectile's age and moves it along its {@link #direction}
     * vector
     * for the given time delta.
     *
     * @param dt the elapsed time since the last frame, in seconds
     * @return {@code true} if the projectile's {@link #lifetime} has expired and it
     *         should be destroyed;
     *         {@code false} otherwise
     */
    public boolean travel(double dt) {
        this.timeSinceCreation += dt;
        if (this.timeSinceCreation >= lifetime) {
            return true; // please destroy me!
        }

        double dx = Math.cos(Math.toRadians(this.direction));
        double dy = Math.sin(Math.toRadians(this.direction));
        this.move(dx, dy, dt);

        return false;
    }

    /**
     * Returns the axis-aligned bounding box representing this projectile's
     * collision area.
     *
     * @return a {@link BBox} constructed from the projectile's position and
     *         dimensions
     */
    @Override
    public BBox getBbox() {
        return new BBox(xPos, yPos, this.width, this.height);
    }

    /**
     * Renders the projectile's sprite centered at {@code (xPos, yPos)} onto the
     * provided graphics context.
     *
     * @param ctx the {@link Graphics2D} context on which to draw the projectile
     */
    @Override
    public void render(Graphics2D ctx) {
        ctx.drawImage(this.img, (int) (xPos - this.width / 2.0), (int) (yPos - this.height / 2.0), (int) this.width,
                (int) this.height, null);
    }
}
