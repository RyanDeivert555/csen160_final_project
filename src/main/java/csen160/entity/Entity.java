package csen160.entity;

import csen160.core.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import javax.sound.sampled.Clip;

public abstract class Entity {
    double xPos;
    double yPos;

    int width;
    int height;

    BufferedImage img;

    static final List<Clip> HIT_SOUNDS;
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

    public double rotation;

    int maxHealth;
    int health;
    double speed;

    public Entity(String imagePath) {
        this.img = AssetLoader.loadTexture(imagePath);

        this.width = 50;
        this.height = 50;

        this.maxHealth = 1; // default for now
        this.health = this.maxHealth;
        this.speed = 200; // default for now
    }

    public Entity(String imagePath, int width, int height, int maxHealth, double speed) {
        this.img = AssetLoader.loadTexture(imagePath);
        this.width = width;
        this.height = height;
        this.maxHealth = maxHealth;
        this.health = this.maxHealth;
        this.speed = speed;
    }

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

    public void teleport(int x, int y) {
        this.xPos = x;
        this.yPos = y;
    }

    public Projectile shootProjectile(double deg) {
        return new Projectile(this.xPos, this.yPos, deg);
    }

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

    public double angleTowards(Entity other) {
        final var dx = other.getX() - this.xPos;
        final var dy = other.getY() - this.yPos;
        return Math.toDegrees(Math.atan2(dy, dx));
    }

    public double getDistance(Entity other) {
        double dx = xPos - other.xPos;
        double dy = yPos - other.yPos;
        return Math.sqrt(dx * dx + dy * dy);
    }

    public int getX() {
        return (int) this.xPos;
    }

    public int getY() {
        return (int) this.yPos;
    }

    public int getWidth() {
        return (int) this.width;
    }

    public int getHeight() {
        return (int) this.height;
    }

    public int getMaxHealth() {
        return this.maxHealth;
    }

    public int getHealth() {
        return this.health;
    }

    public abstract BBox getBbox();

    public abstract void render(Graphics2D ctx);
}
