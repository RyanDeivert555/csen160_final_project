package csen160.entity;

import csen160.core.BBox;

import java.awt.*;

public class Enemy extends Entity {
    double projectileSize;

    public Enemy() {
        super("/temp.png");
    }

    @Override
    public Projectile shootProjectile(double deg) {
        final var bounds = getBbox();
        return new Projectile(position.x + bounds.w() / 2.0, position.y + bounds.h() / 2.0,
                deg, 500, 5, 10, 10);
    }

    @Override
    public BBox getBbox() {
        return new BBox(position.x, position.y, 50, 50);
    }

    @Override
    public void render(Graphics2D ctx) {
        final var tempCtx = (Graphics2D) ctx.create();

        tempCtx.rotate(
                Math.toRadians(rotation),
                position.x + (50 / 2), position.y + (50 / 2));
        // TODO: player width?
        tempCtx.drawImage(img, (int) position.x, (int) position.y, 50, 50, null);

        tempCtx.dispose();
    }
}
