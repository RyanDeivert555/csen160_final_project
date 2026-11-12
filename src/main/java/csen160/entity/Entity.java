package csen160.entity;

import csen160.core.*;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

public abstract class Entity {
    Point2D.Double position = new Point2D.Double();

    int width;
    int height;

    BufferedImage img;

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
        if (magnitude == 0) return;

        dx /= magnitude;
        dy /= magnitude;

        this.position.x += dx * speed * dt;
        this.position.y += dy * speed * dt;
    }

    public void teleport(int x, int y) {
        this.position.x = x;
        this.position.y = y;
    }

    public Projectile shootProjectile(double deg) {
        final var bounds = getBbox();
        return new Projectile(this.position.x, this.position.y, deg);
    }

    public boolean takeDamage() {
        this.health--;
        return this.health <= 0;
    }

    public double angleTowards(Entity other) {
        final var dx = other.getX() - this.position.x;
        final var dy = other.getY() - this.position.y;
        return Math.toDegrees(Math.atan2(dy, dx));
    }

    public double getDistance(Entity other) {
        double dx = position.x - other.position.x;
        double dy = position.y - other.position.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    public int getX() {
        return (int) this.position.x;
    }

    public int getY() {
        return (int) this.position.y;
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
