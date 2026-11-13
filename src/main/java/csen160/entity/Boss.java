package csen160.entity;

import csen160.core.AssetLoader;
import csen160.core.BBox;

import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Boss extends Entity {
    double fireTimer;
    boolean canFire;
    double projectileSpeed = 150.0;

    int currentAttack = 0;
    double currentAttackTimer = 0;
    double[] attackLengths = {10.0, 10.0, 10.0, 10.0, 10.0, 10.0};
    double[] fireRates = {3.0, 5.0, 20.0, 1.5, 4.5, 12.0};

    double attackRotation = 0;

    double squawkTimer = 0;

    static List<Clip> BOSS_SOUNDS;

    static {
        final var sounds = new ArrayList<Clip>();
        sounds.add(AssetLoader.loadSound("/squawk1.wav"));
        sounds.add(AssetLoader.loadSound("/squawk2.wav"));
        sounds.add(AssetLoader.loadSound("/squawk3.wav"));
        BOSS_SOUNDS = sounds;
    }

    public Boss() {
        super("/boss.png", 150, 150, 50000, 0);

        this.xPos = 3840;
        this.yPos = 2304;
    }

    public void update(double dt) {
        fireTimer += dt;
        if (!canFire && fireTimer >= 1.0 / fireRates[currentAttack]) {
            canFire = true;
        }

        currentAttackTimer += dt;
        if (currentAttackTimer >= attackLengths[currentAttack]) {
            currentAttack = (currentAttack + 1) % attackLengths.length;
            currentAttackTimer = 0;
            attackRotation = 0;
        }

        squawkTimer += dt;
        if (squawkTimer >= Math.random() * 3 + 5) {
            squawkTimer = 0;
            final var idx = ThreadLocalRandom.current().nextInt(BOSS_SOUNDS.size());
            final var clip = BOSS_SOUNDS.get(idx);

            if (clip.isRunning()) {
                clip.stop();
            }

            // increase volume
            if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                final var volume = (FloatControl) clip.getControl(
                        FloatControl.Type.MASTER_GAIN
                );

                volume.setValue(Math.min(6.0f, volume.getMaximum()));
            }
            clip.setFramePosition(0);
            clip.start();
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
        if (!canFire) return new HashSet<>();;
        canFire = false;
        fireTimer = 0;

        HashSet<Projectile> projectiles = new HashSet<>();

        double modifiedDeg = Math.random() * 10.0 - 5.0 + deg;

        switch (currentAttack) {
            case 0: // Flower Burst
                int count = 24;

                for (int i = 0; i < count; i++) {
                    double angle = modifiedDeg + attackRotation
                            + (360.0 * i / count);

                    projectiles.add(shootProjectile(angle));
                }

                attackRotation += 12;
                break;

            case 1: // Aimed Fan
                int fanCount = 7;
                double fanSpread = 60;

                for (int i = 0; i < fanCount; i++) {
                    double offset = -fanSpread / 2
                            + i * fanSpread / (fanCount - 1);

                    projectiles.add(shootProjectile(modifiedDeg + offset));
                }
                break;

            case 2: // Rotating Spiral
                int spiralCount = 4;

                for (int i = 0; i < spiralCount; i++) {
                    double angle = modifiedDeg
                            + attackRotation
                            + (360.0 * i / spiralCount);

                    projectiles.add(shootProjectile(angle));
                }

                attackRotation += 15;
                break;

            case 3: // Bullet Wall
                int wallCount = 9;
                double spacing = 30;

                double radians = Math.toRadians(modifiedDeg);
                double perpX = -Math.sin(radians);
                double perpY = Math.cos(radians);

                for (int i = 0; i < wallCount; i++) {
                    // Leave a gap in the center
                    if (i == 4) continue;

                    double offset = (i - (wallCount - 1) / 2.0)
                            * spacing;

                    projectiles.add(shootProjectile(
                            modifiedDeg,
                            xPos + perpX * offset,
                            yPos + perpY * offset
                    ));
                }
                break;

            case 4: // Butterfly
                int butterflyCount = 6;
                double butterflySpread = 65;

                for (int i = 0; i < butterflyCount; i++) {
                    double offset = i * butterflySpread / (butterflyCount - 1);

                    projectiles.add(shootProjectile(
                            modifiedDeg + offset
                    ));

                    projectiles.add(shootProjectile(
                            modifiedDeg - offset
                    ));
                }
                break;
            case 5: // Rainfall
                int rainCount = (int) (Math.random() * 4) + 3;

                for (int i = 0; i < rainCount; i++) {
                    double angle = Math.random() * 360;

                    projectiles.add(shootProjectile(angle));
                }
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
