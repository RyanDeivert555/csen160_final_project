package csen160.entity;

import csen160.core.*;

import java.awt.*;

public class Projectile extends Entity {
    double lifetime;
    double direction; // degrees

    double width;
    double height;

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

    public Projectile(String path, double x, double y, double direction, double speed, double lifetime, double width,
                      double height) {
        this(path);
        this.position.x = x;
        this.position.y = y;
        this.direction = direction;
        this.speed = speed;
        this.lifetime = lifetime;
        this.width = width;
        this.height = height;
    }

    public Projectile(double x, double y, double direction) {
        this();
        this.position.x = x;
        this.position.y = y;
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
        return new BBox(position.x, position.y, this.width, this.height);
    }

    @Override
    public void render(Graphics2D ctx) {
        ctx.drawImage(this.img, (int) (position.x - this.width / 2), (int) (position.y - this.height / 2), (int) this.width, (int) this.height, null);
    }
}
