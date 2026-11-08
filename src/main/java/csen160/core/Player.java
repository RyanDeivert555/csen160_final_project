package csen160.core;

import java.awt.Graphics2D;

public class Player extends Entity {
    public Player() {
        super("/player.png");

        this.x = 100;
        this.y = 100;
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
        tempCtx.drawImage(img, x, y, 50, 50, null);

        tempCtx.dispose();
    }
}
