package csen160.entity;

import csen160.core.BBox;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.HashSet;

public class Enemy extends Entity {
    double projectileSize;

    double fireRate = 1.0;
    double fireTimer;
    boolean canFire;

    String projectilePattern = "RANDOM";

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
        return new Projectile("/evilProjectile.png", position.x, position.y,
                deg, 150, 5, 10, 10);
    }

    public Projectile shootProjectile(double deg, double x, double y) {
        return new Projectile("/evilProjectile.png", x, y,
                deg, 150, 5, 10, 10);
    }


    public HashSet<Projectile> shootProjectiles(double deg) {
        if (!canFire) return null;
        canFire = false;
        fireTimer = 0;

        HashSet<Projectile> projectiles = new HashSet<>();

        switch (projectilePattern) {
            case "CIRCLE":
                final int circleCount = 12;
                for (int i = 0; i < circleCount; i++) {
                    projectiles.add(shootProjectile(deg + (360.0 * i / circleCount)));
                }
                break;
            case "ARC":
                final int arcCount = 8;
                final double arcSpread = 90.0;
                for (int i = 0; i < arcCount; i++) {
                    double offset = (-arcSpread / 2.0) + i * arcSpread / (arcCount - 1);
                    projectiles.add(shootProjectile(deg + offset, position.x, position.y));
                }
                break;
            case "WALL":
                final int wallCount = 5;
                final double spacing = 25.0;

                // get perpendicular angle
                double radians = Math.toRadians(deg);
                double perpX = -Math.sin(radians);
                double perpY = Math.cos(radians);

                for (int i = 0; i < wallCount; i++) {
                    double offset = (i - (wallCount - 1) / 2.0) * spacing;
                    double spawnX = position.x + perpX * offset;
                    double spawnY = position.y + perpY * offset;
                    if (i == 0) System.out.println(spawnX + ", " + spawnY);
                    projectiles.add(shootProjectile(deg, spawnX, spawnY));
                }
                break;
            case "RANDOM":
                for(int i = 0; i < (int) (Math.random() * 3) + 2; i++){
                    double randomAngle = Math.random() * 360.0;
                    projectiles.add(shootProjectile(randomAngle));
                }
            default:
                projectiles.add(shootProjectile(deg));
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

        tempCtx.drawImage(img, (int) position.x - width / 2, (int) position.y - height / 2, this.width, this.height, null);

        tempCtx.dispose();
    }
}
