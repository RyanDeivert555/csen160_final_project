package csen160.entity;

import csen160.core.*;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

public abstract class Entity {
    Point2D.Double position = new Point2D.Double();
    BufferedImage img;
    public double rotation;

    double speed;

    public Entity(String imagePath) {
        this.img = AssetLoader.loadTexture(imagePath);
        this.speed = 200; // default for now
    }

    // TODO: maybe add check that makes sure dx and dy is normalized?
    public void move(double dx, double dy, double dt) {
        this.position.x += dx * speed * dt;
        this.position.y += dy * speed * dt;
    }

    public void teleport(int x, int y) {
        this.position.x = x;
        this.position.y = y;
    }

    public Projectile shootProjectile(double deg) {
        final var bounds = getBbox();
        return new Projectile(this.position.x + bounds.w() / 2.0, this.position.y + bounds.h() / 2.0,
                deg);
    }

    public int getX() {
        return (int) this.position.x;
    }

    public int getY() {
        return (int) this.position.y;
    }

    public abstract BBox getBbox();

    public abstract void render(Graphics2D ctx);
}
