package csen160.entity;

import csen160.core.*;

import java.awt.*;

public class Player extends Entity {
    private static String path = "/player.png";

    private final double fireRate = 10.0; // per second
    private double fireTimer = 0;
    private boolean canFire = false;

    private final int playerWidth = 25;
    private final int playerHeight = 25;

    public Player() {
        super(path);

        this.maxHealth = 10;
        this.health = 3;
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
        if (!this.canFire) return null;
        this.canFire = false;
        this.fireTimer = 0;

        return new Projectile("/projectile.png", position.x, position.y,
                deg, 500, 1, 10, 10);
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

        tempCtx.drawImage(img, (int) position.x - playerWidth, (int) position.y - playerHeight, playerWidth * 2, playerHeight * 2, null);

        tempCtx.dispose();

        // draw hitbox
        ctx.setColor(Color.yellow);
        ctx.drawOval((int) position.x - playerWidth / 2, (int) position.y - playerHeight / 2, playerWidth, playerHeight);
    }
}
