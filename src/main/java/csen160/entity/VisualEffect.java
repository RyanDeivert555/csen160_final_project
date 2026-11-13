package csen160.entity;

import csen160.core.AssetLoader;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;

public class VisualEffect {
    double xPos;
    double yPos;

    int width;
    int height;

    BufferedImage img;

    double duration;
    double timer;

    public VisualEffect(String imagePath, int width, int height, double x, double y, double duration) {
        this.img = AssetLoader.loadTexture(imagePath);

        this.xPos = x;
        this.yPos = y;

        this.width = width;
        this.height = height;

        this.duration = duration;
    }

    public boolean update(double dt) {
        timer += dt;
        return timer > duration;
    }

    public void render(Graphics2D ctx) {
        final var tempCtx = (Graphics2D) ctx.create();

        tempCtx.drawImage(img, (int) xPos - width / 2, (int) yPos - height / 2, this.width, this.height, null);

        tempCtx.dispose();
    }

}