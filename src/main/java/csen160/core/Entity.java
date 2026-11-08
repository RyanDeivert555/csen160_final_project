package csen160.core;

import java.awt.image.BufferedImage;

public class Entity {
    double x;
    double y;

    int width;
    int height;

    BufferedImage img;
    public double rotation;

    double speed;

    public Entity(String imagePath) {
        this.img = AssetLoader.loadTexture(imagePath);

        this.width = img.getWidth();
        this.height = img.getHeight();

        this.speed = 200; // default for now
    }

    public Entity(String imagePath, double scale) {
        this(imagePath);
        this.width = (int) (img.getWidth() * scale);
        this.height = (int) (img.getHeight() * scale);
    }

    public void move(double x, double y, double dt) {
        this.x = this.x + x * speed * dt;
        this.y = this.y + y * speed * dt;
    }

    public Projectile shootProjectile(double deg) {
        return new Projectile((int) (this.x + ((double) this.width / 2)), (int) (this.y + ((double) this.height / 2)), deg);
    }

    // TODO: collision detection between rotated entities ?
    public boolean intersects(Entity other) {
        return (x <= (other.x + other.width) && (x + width) >= other.x)
                && (y <= (other.y + other.height) && (y + height) >= other.y);
    }

    public boolean fullyContains(Entity inner) {
        return inner.x >= x
                && inner.y >= y
                && inner.x + inner.width <= x + width
                && inner.y + inner.height <= y + height;
    }

    public int getX() {
        return (int)this.x;
    }

    public int getY() {
        return (int)this.y;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }
}
