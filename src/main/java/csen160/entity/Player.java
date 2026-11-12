package csen160.entity;

import csen160.core.*;

import java.awt.*;
import java.awt.geom.Ellipse2D;

public class Player extends Entity {
    private static String path = "/player.png";

    private final double fireRate = 10.0; // per second
    private double fireTimer = 0;
    private boolean canFire = false;

    private final int playerWidth = 15;
    private final int playerHeight = 15;
    private final double imageScale  = 3.0;

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

        int drawWidth = (int) (playerWidth * imageScale);
        int drawHeight = (int) (playerHeight * imageScale);
        tempCtx.drawImage(img, (int) (position.x - drawWidth / 2.0), (int) (position.y - drawHeight / 2.0), drawWidth, drawHeight, null);

        tempCtx.dispose();

        // draw hitbox
        ctx.setColor(Color.green);
        ctx.drawOval((int) position.x - playerWidth / 2, (int) position.y - playerHeight / 2, playerWidth, playerHeight);
//        Ellipse2D.Double circle = new Ellipse2D.Double(position.x - playerWidth / 2.0, position.y - playerHeight / 2.0, playerWidth, playerHeight);
//        ctx.fill(circle);
    }
}
