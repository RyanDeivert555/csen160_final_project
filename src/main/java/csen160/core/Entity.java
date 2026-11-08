package csen160.core;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public abstract class Entity {
    int x;
    int y;

    BufferedImage img;
    public double rotation;

    double speed;

    public Entity(String imagePath) {
        this.img = AssetLoader.loadTexture(imagePath);

        this.speed = 200; // default for now
    }

    public void move(int x, int y, double dt) {
        this.x = (int) (this.x + x * speed * dt);
        this.y = (int) (this.y + y * speed * dt);
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public abstract BBox getBbox();

    public abstract void render(Graphics2D ctx);
}
