package csen160.entity;

import csen160.core.AssetLoader;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Represents a temporary, non-interactive visual effect (such as an explosion or hit spark)
 * rendered at a fixed position in the game world for a specified duration.
 */
public class VisualEffect {

    /** The x-coordinate of the visual effect's center in world space. */
    double xPos;

    /** The y-coordinate of the visual effect's center in world space. */
    double yPos;

    /** The rendered width of the visual effect in pixels. */
    int width;

    /** The rendered height of the visual effect in pixels. */
    int height;

    /** The sprite texture displayed for this visual effect. */
    BufferedImage img;

    /** The total lifespan of the visual effect, in seconds. */
    double duration;

    /** The elapsed time since the visual effect was created, in seconds. */
    double timer;

    /**
     * Constructs a new {@code VisualEffect} with the specified texture, dimensions, position, and lifespan.
     *
     * @param imagePath the resource path to the effect's texture image
     * @param width     the rendered width of the effect in pixels
     * @param height    the rendered height of the effect in pixels
     * @param x         the x-coordinate of the effect's center
     * @param y         the y-coordinate of the effect's center
     * @param duration  the total time in seconds the effect should remain active
     */
    public VisualEffect(String imagePath, int width, int height, double x, double y, double duration) {
        this.img = AssetLoader.loadTexture(imagePath);

        this.xPos = x;
        this.yPos = y;

        this.width = width;
        this.height = height;

        this.duration = duration;
    }

    /**
     * Advances the effect's elapsed timer and checks whether its lifespan has expired.
     *
     * @param dt the elapsed time since the last frame, in seconds
     * @return {@code true} if the elapsed timer exceeds {@link #duration} and the effect should be removed;
     *         {@code false} otherwise
     */
    public boolean update(double dt) {
        timer += dt;
        return timer > duration;
    }

    /**
     * Renders the visual effect centered at {@code (xPos, yPos)} onto the provided graphics context.
     *
     * @param ctx the {@link Graphics2D} context on which to draw the effect
     */
    public void render(Graphics2D ctx) {
        final var tempCtx = (Graphics2D) ctx.create();

        tempCtx.drawImage(img, (int) xPos - width / 2, (int) yPos - height / 2, this.width, this.height, null);

        tempCtx.dispose();
    }

}
