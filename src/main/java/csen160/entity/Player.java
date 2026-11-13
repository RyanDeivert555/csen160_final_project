package csen160.entity;

import csen160.core.*;

import java.awt.*;
import java.util.HashSet;

public class Player extends Entity {
    private static String path = "/player.png";

    private final double fireRate = 12.0; // per second
    private double fireTimer = 0;
    private boolean canFire = false;
    private int projectileCount = 1;

    private final int playerWidth = 15;
    private final int playerHeight = 15;
    private final double imageScale = 3.0;

    private int points = 0;
    private int lastHealthMilestone = 0;
    private int lastProjectileMilestone = 0;

    private Map map;

    public Player() {
        super(path);

        this.maxHealth = 10;
        this.health = 5;

        this.position.x = 64;
        this.position.y = 64;
    }

    public void attachMap(Map map) {
        this.map = map;
    }

    public void moveAndCollide(Map map, double dx, double dy) {
        position.x = map.moveX(position.x, position.y, playerWidth, playerHeight, dx);
        position.y = map.moveY(position.x, position.y, playerWidth, playerHeight, dy);
    }

    @Override
    public void move(double dx, double dy, double dt) {
        if (map == null) {
            super.move(dx, dy, dt);
            return;
        }

        final double magnitude = Math.sqrt(dx * dx + dy * dy);
        if (magnitude == 0)
            return;

        final double scale = speed * dt / magnitude;
        position.x = map.moveX(position.x, position.y, playerWidth, playerHeight, dx * scale);
        position.y = map.moveY(position.x, position.y, playerWidth, playerHeight, dy * scale);
    }

    public void update(double dt) {
        //
        if (!this.canFire && this.fireTimer >= 1.0 / this.fireRate) {
            this.canFire = true;
        } else {
            this.fireTimer += dt;
        }
    }

    @Override
    public Projectile shootProjectile(double deg) {
        if (!this.canFire)
            return null;
        this.canFire = false;
        this.fireTimer = 0;

        return new Projectile("/projectile.png", position.x, position.y, deg, 500, 1, 10, 10);
    }

    public HashSet<Projectile> shootProjectiles(double deg) {
        if (!this.canFire)
            return null;
        this.canFire = false;
        this.fireTimer = 0;

        var projectiles = new HashSet<Projectile>();

        double[][] offsets = switch (projectileCount) {
            case 2 -> new double[][] { { -5, 0 }, { 5, 0 } };
            case 3 -> new double[][] { { 0, 8 }, // Front
                    { -6, -6 }, // Back left
                    { 6, -6 } // Back right
            };
            case 4 -> new double[][] { { -5, 5 }, // Front left
                    { 5, 5 }, // Front right
                    { -10, -5 }, // Back left
                    { 10, -5 } // Back right
            };
            default -> new double[][] { { 0, 0 } };
        };

        double rad = Math.toRadians(deg);

        double perpendicularX = -Math.sin(rad);
        double perpendicularY = Math.cos(rad);

        double forwardX = Math.cos(rad);
        double forwardY = Math.sin(rad);

        for (double[] offset : offsets) {
            double x = position.x
                    + perpendicularX * offset[0]
                    + forwardX * offset[1];

            double y = position.y
                    + perpendicularY * offset[0]
                    + forwardY * offset[1];

            projectiles.add(new Projectile(
                    "/projectile.png",
                    x, y,
                    deg,
                    500, 1, 10, 10));
        }
        return projectiles;
    }

    public void setPoints(int points) {
        this.points = points;
        if (points - lastHealthMilestone > 250000) {
            if (health < maxHealth)
                health++;
            lastHealthMilestone += 250000;
        }
        if (points - lastProjectileMilestone > 100000) {
            if (projectileCount < 4) {
                projectileCount++;
            }
            lastProjectileMilestone += 100000;
        }
    }

    @Override
    public BBox getBbox() {
        return new BBox(position.x, position.y, this.playerWidth, this.playerHeight);
    }

    @Override
    public void render(Graphics2D ctx) {
        // player is rendered twice as large as the hitbox
        final var tempCtx = (Graphics2D) ctx.create();

        tempCtx.rotate(Math.toRadians(rotation), this.position.x, this.position.y);

        int drawWidth = (int) (playerWidth * imageScale);
        int drawHeight = (int) (playerHeight * imageScale);
        tempCtx.drawImage(img, (int) (position.x - drawWidth / 2.0), (int) (position.y - drawHeight / 2.0), drawWidth,
                drawHeight, null);

        tempCtx.dispose();

        // draw hitbox
        ctx.setColor(Color.green);
        ctx.drawOval((int) position.x - playerWidth / 2, (int) position.y - playerHeight / 2, playerWidth,
                playerHeight);
        // Ellipse2D.Double circle = new Ellipse2D.Double(position.x - playerWidth /
        // 2.0, position.y - playerHeight / 2.0, playerWidth, playerHeight);
        // ctx.fill(circle);
    }
}
