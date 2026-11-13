package csen160.entity;

import csen160.core.BBox;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.HashSet;

public class Enemy extends Entity {
    double fireRate = 1.0;
    double fireTimer;
    boolean canFire;

    String projectilePattern = "DEFAULT";
    boolean randomAim;
    double projectileSpeed = 150.0;

    double range = 400.0;
    private double targetX;
    private double targetY;
    private double movementTimer;

    private enum MovementBehavior {
        WANDER,
        APPROACH,
        RETREAT,
        STRAFE
    }

    private double strafeDirection = 1.0;

    public Enemy() {
        super("/temp.png");
    }

    public Enemy(String imagePath, int width, int height, int maxHealth, double speed, String projectilePattern, double fireRate, double projectileSpeed, boolean randomAim) {
        super(imagePath, width, height, maxHealth, speed);
        this.projectilePattern = projectilePattern;
        this.fireRate = fireRate;
        this.randomAim = randomAim;
        this.projectileSpeed = projectileSpeed;
    }

    public Enemy(Enemy other) {
        super("/temp.png", other.width, other.height, other.maxHealth, other.speed);
        this.img = other.img;
        this.projectilePattern = other.projectilePattern;
        this.fireRate = other.fireRate;
        this.randomAim = other.randomAim;
        this.projectileSpeed = other.projectileSpeed;
    }

    public void update(double dt, double playerX, double playerY) {
        // Firing timer
        if (!canFire && fireTimer >= 1.0 / fireRate) {
            canFire = true;
        } else {
            fireTimer += dt;
        }

        double distanceToPlayer = Point2D.distance(
                xPos, yPos,
                playerX, playerY
        );

        boolean playerNearby = distanceToPlayer <= range;

        // Choose new waypoint periodically
        movementTimer -= dt;

        double distanceToTarget = Point2D.distance(
                xPos, yPos,
                targetX, targetY
        );

        if (movementTimer <= 0 || distanceToTarget < 10) {
            chooseWaypoint(playerX, playerY, playerNearby);
            double movementInterval = 2.0;
            movementTimer = movementInterval + Math.random() * 2.0;
        }

        // Move toward waypoint
        double dx = targetX - xPos;
        double dy = targetY - yPos;

        double length = Math.hypot(dx, dy);

        if (length > 0) {
            double moveX = dx / length;
            double moveY = dy / length;

            xPos += moveX * speed * dt;
            yPos += moveY * speed * dt;

            // Face movement direction
            rotation = Math.toDegrees(Math.atan2(moveY, moveX)) + 270;
        }
    }

    private void chooseWaypoint(double playerX, double playerY, boolean playerNearby) {
        double angle;
        double distance;

        MovementBehavior behavior = MovementBehavior.WANDER;
        if (playerNearby) {
            double roll = Math.random();

            if (roll < 0.4) {
                behavior = MovementBehavior.STRAFE;
            } else if (roll < 0.65) {
                behavior = MovementBehavior.APPROACH;
            } else if (roll < 0.85) {
                behavior = MovementBehavior.RETREAT;
            }

            if (Math.random() < 0.5) {
                strafeDirection *= -1;
            }
        }

        angle = Math.random() * Math.PI * 2;
        distance = 75 + Math.random() * 150;

        switch (behavior) {
            case APPROACH:
                targetX = xPos + (playerX - xPos) * 0.4;
                targetY = yPos + (playerY - yPos) * 0.4;
                break;

            case RETREAT:
                double dx = xPos - playerX;
                double dy = yPos - playerY;
                double length = Math.max(1, Math.hypot(dx, dy));

                targetX = xPos + dx / length * distance;
                targetY = yPos + dy / length * distance;
                break;

            case STRAFE:
                double px = playerX - xPos;
                double py = playerY - yPos;
                double len = Math.max(1, Math.hypot(px, py));

                targetX = xPos - py / len * distance * strafeDirection;
                targetY = yPos + px / len * distance * strafeDirection;
                break;

            case WANDER:
            default:
                targetX = xPos + Math.cos(angle) * distance;
                targetY = yPos + Math.sin(angle) * distance;
                break;
        }
    }

    @Override
    public Projectile shootProjectile(double deg) {
        return new Projectile("/evilProjectile.png", xPos, yPos,
                deg, projectileSpeed, 5, 10, 10);
    }

    public Projectile shootProjectile(double deg, double x, double y) {
        return new Projectile("/evilProjectile.png", x, y,
                deg, projectileSpeed, 5, 10, 10);
    }

    public HashSet<Projectile> shootProjectiles(double deg) {

        if (!canFire) return null;
        canFire = false;
        fireTimer = 0;

        HashSet<Projectile> projectiles = new HashSet<>();

        double modifiedDeg = Math.random() * 10.0 - 5.0 + deg;
        if (randomAim) modifiedDeg = Math.random() * 360.0;

        switch (projectilePattern) {
            case "CIRCLE":
                final int circleCount = 8;
                for (int i = 0; i < circleCount; i++) {
                    projectiles.add(shootProjectile(modifiedDeg + (360.0 * i / circleCount)));
                }
                break;
            case "ARC":
                final int arcCount = 5;
                final double arcSpread = 90.0;
                for (int i = 0; i < arcCount; i++) {
                    double offset = (-arcSpread / 2.0) + i * arcSpread / (arcCount - 1);
                    projectiles.add(shootProjectile(modifiedDeg + offset, xPos, yPos));
                }
                break;
            case "WALL":
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
            case "RANDOM":
                for (int i = 0; i < (int) (Math.random() * 3) + 2; i++) {
                    double randomAngle = Math.random() * 360.0;
                    projectiles.add(shootProjectile(randomAngle));
                }
            default:
                projectiles.add(shootProjectile(modifiedDeg));
                break;
        }
        return projectiles;
    }

    public double getRange() {
        return this.range;
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
