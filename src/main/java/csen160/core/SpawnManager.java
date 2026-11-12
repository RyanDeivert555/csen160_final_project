package csen160.core;

import csen160.entity.Enemy;
import csen160.entity.Projectile;

import java.util.*;

public class SpawnManager {
    int minX;
    int maxX;
    int minY;
    int maxY;

    double timeSinceLastSpawn;

    private static final Random random = new Random();
    public static HashMap<String, Enemy> enemyTypes;
    public static List<String> keys;

    static {
        enemyTypes = new HashMap<>();

        // BASIC ENEMIES
        enemyTypes.put("BASIC", new Enemy(
                "/PiratePack/PNG/Ships/dinghySmall1.png",
                32, 52, 3, 100, "DEFAULT", 1, 150, false
        ));

        enemyTypes.put("SCOUT", new Enemy(
                "/PiratePack/PNG/Ships/ship (1).png",
                32, 52, 2, 180, "DEFAULT", 0.8, 130, false
        ));

        enemyTypes.put("RAIDER", new Enemy(
                "/PiratePack/PNG/Ships/ship (2).png",
                36, 56, 5, 120, "ARC", 1.2, 160, false
        ));

        // FAST ENEMIES
        enemyTypes.put("SPEEDER", new Enemy(
                "/PiratePack/PNG/Ships/ship (3).png",
                30, 48, 3, 240, "RANDOM", 1.5, 120, true
        ));

        enemyTypes.put("INTERCEPTOR", new Enemy(
                "/PiratePack/PNG/Ships/ship (4).png",
                36, 56, 6, 200, "DEFAULT", 0.6, 180, true
        ));

        // STANDARD COMBAT ENEMIES
        enemyTypes.put("FIGHTER", new Enemy(
                "/PiratePack/PNG/Ships/ship (5).png",
                48, 64, 6, 110, "ARC", 1.0, 180, false
        ));

        enemyTypes.put("GUNNER", new Enemy(
                "/PiratePack/PNG/Ships/ship (6).png",
                48, 64, 5, 90, "WALL", 1.8, 140, false
        ));

        enemyTypes.put("BOMBER", new Enemy(
                "/PiratePack/PNG/Ships/ship (7).png",
                56, 72, 7, 65, "CIRCLE", 2.5, 110, false
        ));

        // ADVANCED ENEMIES
        enemyTypes.put("SNIPER", new Enemy(
                "/PiratePack/PNG/Ships/ship (8).png",
                44, 60, 8, 75, "DEFAULT", 2.0, 300, true
        ));

        enemyTypes.put("GATLING", new Enemy(
                "/PiratePack/PNG/Ships/ship (8).png",
                52, 68, 12, 80, "RANDOM", 0.35, 170, true
        ));

        enemyTypes.put("DESTROYER", new Enemy(
                "/PiratePack/PNG/Ships/ship (9).png",
                64, 80, 15, 55, "WALL", 2.0, 170, false
        ));

        // HEAVY ENEMIES
        enemyTypes.put("TANK", new Enemy(
                "/PiratePack/PNG/Ships/ship (10).png",
                72, 88, 20, 40, "DEFAULT", 1.5, 200, false
        ));

        enemyTypes.put("ARTILLERY", new Enemy(
                "/PiratePack/PNG/Ships/ship (11).png",
                64, 80, 30, 50, "ARC", 2.0, 250, false
        ));

        // ELITE ENEMIES
        enemyTypes.put("ELITE", new Enemy(
                "/PiratePack/PNG/Ships/ship (12).png",
                72, 88, 40, 100, "CIRCLE", 1.5, 200, true
        ));

        enemyTypes.put("ELITE_GUNNER", new Enemy(
                "/PiratePack/PNG/Ships/ship (13).png",
                80, 96, 50, 65, "WALL", 1.0, 220, false
        ));

        keys = new ArrayList<>(enemyTypes.keySet());
    }

    public SpawnManager(int minX, int maxX, int minY, int maxY) {
        this.minX = minX;
        this.maxX = maxX;

        this.minY = minY;
        this.maxY = maxY;
    }

    public HashSet<Enemy> spawnEnemies(double dt) {
        timeSinceLastSpawn += dt;
        if (timeSinceLastSpawn < random.nextDouble(1.0, 2.5)) return null;
        timeSinceLastSpawn = 0;

        String randomKey = keys.get(random.nextInt(keys.size()));

        var newEnemies = new HashSet<Enemy>();
        for (int i = 0; i < Math.random() * 1 + 1; i++) {
            var e = new Enemy(enemyTypes.get(randomKey));

            double x = random.nextDouble(minX, maxX);
            double y = random.nextDouble(minY, maxY);

            e.teleport((int) x, (int) y);

            newEnemies.add(e);
        }

        return newEnemies;
    }
}