package csen160.entity;

import csen160.core.*;

import java.awt.Graphics2D;

public class Projectile extends Entity {
    double lifetime;
    double direction; // degrees

    double timeSinceCreation;

    public Projectile() {
        super("/projectile.png");
        this.speed = 500;
        this.lifetime = 3;
    }

    public Projectile(double x, double y, double direction, double speed, double lifetime) {
        super("/projectile.png");
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.speed = speed;
        this.lifetime = lifetime;
    }

    public Projectile(double x, double y, double direction) {
        this();
        this.x = x;
        this.y = y;
        this.direction = direction;
    }

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

    @Override
    public BBox getBbox() {
        // TODO: discuss different projectile sizes
        return new BBox(x, y, 100, 100);
    }

    @Override
    public void render(Graphics2D ctx) {
        ctx.drawImage(this.img, (int) x, (int) y, null);
    }
}
