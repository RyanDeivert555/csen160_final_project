package csen160.entity;

import csen160.core.*;

import java.awt.Graphics2D;

public class Player extends Entity {
    private static String path = "/player.png";
    double projectileSize;

    public Player() {
        super(path);
        this.x = 100;
        this.y = 100;
    }

//    public Projectile(double x, double y, double direction, double speed, double lifetime, double width, double height) {

    @Override
    public Projectile shootProjectile(double deg) {
        final var bounds = getBbox();
        return new Projectile(this.x + bounds.w() / 2.0, this.y + bounds.h() / 2.0,
                deg, 500, 5, 10, 10);
    }

    @Override
    public BBox getBbox() {
        return new BBox(x, y, 50, 50);
    }

    @Override
    public void render(Graphics2D ctx) {
        final var tempCtx = (Graphics2D) ctx.create();

        tempCtx.rotate(
                Math.toRadians(rotation),
                x + (50 / 2), y + (50 / 2));
        // TODO: player width?
        tempCtx.drawImage(img, (int) x, (int) y, 50, 50, null);

        tempCtx.dispose();
    }
}
