package csen160.core;

import java.util.HashSet;

public class Player extends Entity {
    public Player() {
        super("/player.png");
        this.width = 50;
        this.height = 50;

        this.x = 100;
        this.y = 100;
    }

    public void logCollisions(HashSet<Entity> entities) {
        for (Entity e : entities) {
            if (e.fullyContains(this)) {
                System.out.println("Inside entity: " + e);
            } else if (fullyContains(e)) {
                System.out.println("Entity is inside: " + e);
            } else if (intersects(e)) {
                System.out.println("Intersecting entity: " + e);
            }
        }
    }
}
