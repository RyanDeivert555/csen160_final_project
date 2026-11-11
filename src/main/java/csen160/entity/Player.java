package csen160.entity;

import csen160.core.*;

import java.awt.Graphics2D;

public class Player extends Entity {
    private static String path = "/player.png";

    private final double fireRate = (double) 1 / 10; // 1/<shots per second>
    private double fireTimer = 0;
    private boolean canFire = false;

    public Player() {
        super(path);
    }

    // public Projectile(double x, double y, double direction, double speed, double
    // lifetime, double width, double height) {

    public void update(double dt){
        //
        if(!this.canFire && this.fireTimer >= this.fireRate){
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
        return new Projectile(position.x + bounds.w() / 2.0, position.y + bounds.h() / 2.0,
                deg, 500, 1, 10, 10);
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
