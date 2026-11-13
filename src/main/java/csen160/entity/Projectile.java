package csen160.entity;

import csen160.core.*;

import java.awt.*;

public class Projectile extends Entity {
    double lifetime;
    double direction; // degrees

    double timeSinceCreation;

    public Projectile() {
        super("/projectile.png");
        this.speed = 500;
        this.lifetime = 5;
    }

    public Projectile(String path) {
        super(path);
        this.speed = 500;
        this.lifetime = 5;
    }

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

    public Projectile(double x, double y, double direction) {
        this();
        this.xPos = x;
        this.yPos = y;
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
        return new BBox(xPos, yPos, this.width, this.height);
    }

    @Override
    public void render(Graphics2D ctx) {
        ctx.drawImage(this.img, (int) (xPos - this.width / 2.0), (int) (yPos - this.height / 2.0), (int) this.width, (int) this.height, null);
    }
}
