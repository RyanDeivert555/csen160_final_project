package csen160.entity;

import csen160.core.BBox;

import java.awt.*;

public class Enemy extends Entity {
    double projectileSize;

    double fireRate = 3.0;
    double fireTimer;
    boolean canFire;

    public Enemy() {
        super("/temp.png");
    }

    public void update(double dt) {
        if (!this.canFire && this.fireTimer >= 1.0 / this.fireRate) {
            this.canFire = true;
        } else {
            this.fireTimer += dt;
        }
    }

    @Override
    public Projectile shootProjectile(double deg) {
        if(!this.canFire) return null;
        this.canFire = false;
        this.fireTimer = 0;

        final var bounds = getBbox();
        return new Projectile("/evilProjectile.png", position.x + bounds.w() / 2.0, position.y + bounds.h() / 2.0,
                deg, 250, 5, 10, 10);
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
