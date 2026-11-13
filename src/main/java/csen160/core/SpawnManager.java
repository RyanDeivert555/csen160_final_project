package csen160.core;

import csen160.entity.*;

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
                32, 52, 3, 100, "DEFAULT", .5, 120, false));

        enemyTypes.put("SCOUT", new Enemy(
                "/PiratePack/PNG/Ships/ship (1).png",
                32, 52, 2, 180, "DEFAULT", .6, 120, false));

        enemyTypes.put("RAIDER", new Enemy(
                "/PiratePack/PNG/Ships/ship (2).png",
                36, 56, 5, 120, "ARC", .25, 100, false));

        // FAST ENEMIES
        enemyTypes.put("SPEEDER", new Enemy(
                "/PiratePack/PNG/Ships/ship (3).png",
                30, 48, 3, 240, "RANDOM", 2, 120, true));

        enemyTypes.put("INTERCEPTOR", new Enemy(
                "/PiratePack/PNG/Ships/ship (4).png",
                36, 56, 6, 200, "DEFAULT", 1, 120, true));

        // STANDARD COMBAT ENEMIES
        enemyTypes.put("FIGHTER", new Enemy(
                "/PiratePack/PNG/Ships/ship (5).png",
                48, 64, 6, 110, "ARC", .1, 150, false));

        enemyTypes.put("GUNNER", new Enemy(
                "/PiratePack/PNG/Ships/ship (6).png",
                48, 64, 5, 90, "WALL", .4, 90, false));

        enemyTypes.put("BOMBER", new Enemy(
                "/PiratePack/PNG/Ships/ship (7).png",
                56, 72, 7, 65, "CIRCLE", .7, 110, false));

        // ADVANCED ENEMIES
        enemyTypes.put("SNIPER", new Enemy(
                "/PiratePack/PNG/Ships/ship (8).png",
                44, 60, 8, 75, "DEFAULT", .25, 300, true));

        enemyTypes.put("GATLING", new Enemy(
                "/PiratePack/PNG/Ships/ship (8).png",
                52, 68, 12, 80, "RANDOM", 2.0, 130, true));

        enemyTypes.put("DESTROYER", new Enemy(
                "/PiratePack/PNG/Ships/ship (9).png",
                64, 80, 15, 55, "WALL", .3, 100, false));

        // HEAVY ENEMIES
        enemyTypes.put("TANK", new Enemy(
                "/PiratePack/PNG/Ships/ship (10).png",
                72, 88, 20, 40, "DEFAULT", 1, 120, false));

        enemyTypes.put("ARTILLERY", new Enemy(
                "/PiratePack/PNG/Ships/ship (11).png",
                64, 80, 30, 50, "ARC", .5, 120, false));

        // ELITE ENEMIES
        enemyTypes.put("ELITE", new Enemy(
                "/PiratePack/PNG/Ships/ship (12).png",
                72, 88, 40, 100, "CIRCLE", 1, 140, true));

        enemyTypes.put("ELITE_GUNNER", new Enemy(
                "/PiratePack/PNG/Ships/ship (13).png",
                80, 96, 50, 65, "WALL", .3, 150, false));

        keys = new ArrayList<>(enemyTypes.keySet());
    }

    public SpawnManager(int minX, int maxX, int minY, int maxY) {
        this.minX = minX;
        this.maxX = maxX;

        this.minY = minY;
        this.maxY = maxY;
    }

    public Enemy spawnEnemy() {
        String randomKey = keys.get(random.nextInt(keys.size()));
        Enemy e = new Enemy(enemyTypes.get(randomKey));
        int attempts = 1;
        do {
            double x = random.nextDouble(minX, maxX);
            double y = random.nextDouble(minY, maxY);
            e.teleport((int) x, (int) y);
            attempts++;
        } while (e.isBlocked() && attempts < 100);
        if (e.isBlocked()) return null;
        return e;
    }

    public HashSet<Enemy> spawnEnemies(double dt) {
        timeSinceLastSpawn += dt;
        if (timeSinceLastSpawn < random.nextDouble(1.0, 2.5))
            return null;
        timeSinceLastSpawn = 0;

        String randomKey = keys.get(random.nextInt(keys.size()));

        var newEnemies = new HashSet<Enemy>();
        // TODO: 5 spawn attempts, more attempts as score increases?
        for (int i = 0; i < 5; i++) {
            double x = random.nextDouble(minX, maxX);
            double y = random.nextDouble(minY, maxY);
            final var e = new Enemy(enemyTypes.get(randomKey));
            e.teleport((int) x, (int) y);
            if (!e.isBlocked()) {
                newEnemies.add(e);
            }

        }

        return newEnemies;
    }
}
