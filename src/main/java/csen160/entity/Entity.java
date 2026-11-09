package csen160.entity;

import csen160.core.*;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public abstract class Entity {
    double x;
    double y;
    BufferedImage img;
    public double rotation;

    double speed;

    public Entity(String imagePath) {
        this.img = AssetLoader.loadTexture(imagePath);
        this.speed = 200; // default for now
    }

    // TODO: maybe add check that makes sure dx and dy is normalized?
    public void move(double dx, double dy, double dt) {
        this.x = this.x + dx * speed * dt;
        this.y = this.y + dy * speed * dt;
    }

    public void teleport(int x, int y) {
        this.x = x;
        this.y = y
        ;
    }

    public Projectile shootProjectile(double deg) {
        final var bounds = getBbox();
        return new Projectile(this.x + bounds.w() / 2.0, this.y + bounds.h() / 2.0,
                deg);
    }

    public int getX() {
        return (int) this.x;
    }

    public int getY() {
        return (int) this.y;
    }

    public abstract BBox getBbox();

    public abstract void render(Graphics2D ctx);
}
