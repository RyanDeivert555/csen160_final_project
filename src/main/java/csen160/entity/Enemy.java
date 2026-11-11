package csen160.entity;

import csen160.core.BBox;

import java.awt.*;
import java.util.HashSet;

public class Enemy extends Entity {
    double projectileSize;

    double fireRate = 3.0;
    double fireTimer;
    boolean canFire;

    String projectilePattern = "CIRCLE";

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
        final var bounds = getBbox();
        return new Projectile("/evilProjectile.png", position.x + bounds.w() / 2.0, position.y + bounds.h() / 2.0,
                deg, 150, 5, 10, 10);
    }

    public Projectile shootProjectile(double deg, double offsetX, double offsetY) {
        final var bounds = getBbox();
        return new Projectile("/evilProjectile.png", position.x + bounds.w() / 2.0 + offsetX, position.y + bounds.h() / 2.0 + offsetY,
                deg, 150, 5, 10, 10);
    }


    public HashSet<Projectile> shootProjectiles(double deg) {
        if (!this.canFire) return null;
        this.canFire = false;
        this.fireTimer = 0;

        HashSet<Projectile> projectiles = new HashSet<>();
        switch (this.projectilePattern) {
            case "CIRCLE":
                final int count = 12;
                for (int i = 0; i < count; i++) {
                    projectiles.add(this.shootProjectile(deg + (360.0 * i / count)));
                }
                break;
            default:
                projectiles.add(this.shootProjectile(deg));
                break;
        }
        return projectiles;
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
