package csen160.entity;

import csen160.core.BBox;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.HashSet;

public class Enemy extends Entity {
    double projectileSize;

    double fireRate = 3.0;
    double fireTimer;
    boolean canFire;

    String projectilePattern = "default";

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
        return new Projectile("/evilProjectile.png", position.x, position.y,
                deg, 150, 5, 10, 10);
    }

    public Projectile shootProjectile(double deg, double offsetX, double offsetY) {
        final var bounds = getBbox();
        return new Projectile("/evilProjectile.png", position.x, position.y,
                deg, 150, 5, 10, 10);
    }


    public HashSet<Projectile> shootProjectiles(double deg) {
        if (!this.canFire) return null;
        this.canFire = false;
        this.fireTimer = 0;

        HashSet<Projectile> projectiles = new HashSet<>();

        switch (this.projectilePattern) {
            case "CIRCLE":
                final int circleCount = 12;
                for (int i = 0; i < circleCount; i++) {
                    projectiles.add(this.shootProjectile(deg + (360.0 * i / circleCount)));
                }
                break;
            case "ARC":
                final int arcCount = 8;
                final double arcSpread = 90.0;
                for (int i = 0; i < arcCount; i++) {
                    double offset = (-arcSpread / 2.0) + i * arcSpread / (arcCount - 1);
                    projectiles.add(this.shootProjectile(deg + offset, this.position.x, this.position.y));
                }
                break;
            case "WALL":
                projectiles.add(new Projectile("/evilProjectile.png", this.position.x, this.position.y,
                        deg, 150, 5, 10, 30));
                break;
            default:
                projectiles.add(this.shootProjectile(deg));
                break;
        }
        return projectiles;
    }

    @Override
    public BBox getBbox() {
        return new BBox(position.x, position.y, this.width, this.height);
    }

    @Override
    public void render(Graphics2D ctx) {
        final var tempCtx = (Graphics2D) ctx.create();

        tempCtx.rotate(Math.toRadians(rotation), position.x, position.y);

        tempCtx.drawImage(img, (int) position.x - 25, (int) position.y - 25, this.width, this.height, null);

        tempCtx.dispose();
    }
}
