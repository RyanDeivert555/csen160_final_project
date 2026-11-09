package csen160.entity;

import csen160.core.BBox;

import java.awt.*;

public class Enemy extends Entity {
    double projectileSize;

    public Enemy() {
        super("/temp.png");
        this.x = 100;
        this.y = 100;
    }

    @Override
    public Projectile shootProjectile(double deg) {
        final var bounds = getBbox();
        return new Projectile(this.x + bounds.w() / 2.0, this.y + bounds.h() / 2.0,
                deg, 500, 5, 10, 10);
    }

    @Override
    public BBox getBbox() {
        return new BBox(x, y, 50, 50);
    }

    @Override
    public void render(Graphics2D ctx) {
        final var tempCtx = (Graphics2D) ctx.create();

        tempCtx.rotate(
                Math.toRadians(rotation),
                x + (50 / 2), y + (50 / 2));
        // TODO: player width?
        tempCtx.drawImage(img, (int) x, (int) y, 50, 50, null);

        tempCtx.dispose();
    }
}
