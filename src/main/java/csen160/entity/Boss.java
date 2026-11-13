package csen160.entity;

import csen160.core.BBox;

import java.awt.*;
import java.util.HashSet;

public class Boss extends Entity {
    double fireRate = 1.0;
    double fireTimer;
    boolean canFire;
    double projectileSpeed = 150.0;

    int currentAttack = 0;
    double currentAttackTimer;
    double[] attackLengths = {15.0, 15.0, 15.0, 15.0, 15.0, 15.0};

    public Boss() {
        super("/boss.png", 200, 200, 50000, 0);

        this.xPos = 1250;
        this.yPos = 750;
    }

    public void update(double dt) {
        fireTimer += dt;
        if (!canFire && fireTimer >= 1.0 / fireRate) {
            canFire = true;
        }

        currentAttackTimer += dt;
        if (currentAttackTimer >= attackLengths[currentAttack]) {
            currentAttackTimer = (currentAttackTimer + 1) % attackLengths.length;
        }
    }

    @Override
    public Projectile shootProjectile(double deg) {
        return new Projectile("/evilProjectile.png", xPos, yPos,
                deg, projectileSpeed, 10, 10, 10);
    }

    public Projectile shootProjectile(double deg, double x, double y) {
        return new Projectile("/evilProjectile.png", x, y,
                deg, projectileSpeed, 10, 10, 10);
    }

    public HashSet<Projectile> shootProjectiles(double deg) {
        if (!canFire) return null;
        canFire = false;
        fireTimer = 0;

        HashSet<Projectile> projectiles = new HashSet<>();

        double modifiedDeg = Math.random() * 10.0 - 5.0 + deg;

        switch (currentAttack) {
            case 0:
                final int circleCount = 8;
                for (int i = 0; i < circleCount; i++) {
                    projectiles.add(shootProjectile(modifiedDeg + (360.0 * i / circleCount)));
                }
                break;
            case 1:
                final int arcCount = 5;
                final double arcSpread = 90.0;
                for (int i = 0; i < arcCount; i++) {
                    double offset = (-arcSpread / 2.0) + i * arcSpread / (arcCount - 1);
                    projectiles.add(shootProjectile(modifiedDeg + offset, xPos, yPos));
                }
                break;
            case 2:
                final int wallCount = 5;
                final double spacing = 10.0;

                // get perpendicular angle
                double radians = Math.toRadians(modifiedDeg);
                double perpX = -Math.sin(radians);
                double perpY = Math.cos(radians);

                for (int i = 0; i < wallCount; i++) {
                    double offset = (i - (wallCount - 1) / 2.0) * spacing;
                    double spawnX = xPos + perpX * offset;
                    double spawnY = yPos + perpY * offset;
                    projectiles.add(shootProjectile(modifiedDeg, spawnX, spawnY));
                }
                break;
            case 3:
                for (int i = 0; i < (int) (Math.random() * 3) + 2; i++) {
                    double randomAngle = Math.random() * 360.0;
                    projectiles.add(shootProjectile(randomAngle));
                }
            case 4:
                projectiles.add(shootProjectile(modifiedDeg));
                break;
            case 5:
                break;
            default:
                break;
        }

        return projectiles;
    }

    @Override
    public BBox getBbox() {
        return new BBox(xPos, yPos, this.width, this.height);
    }

    @Override
    public void render(Graphics2D ctx) {
        final var tempCtx = (Graphics2D) ctx.create();

        tempCtx.rotate(Math.toRadians(rotation), xPos, yPos);

        tempCtx.drawImage(img, (int) xPos - width / 2, (int) yPos - height / 2, this.width, this.height, null);

        tempCtx.dispose();
    }

}
