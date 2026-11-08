package csen160.core;

public class Projectile extends Entity {

    double lifetime;
    double direction; // degrees

    double timeSinceCreation;

    public Projectile(){
        super("/projectile.png");
        this.width = 10;
        this.height = 10;
        this.speed = 500;
        this.lifetime = 3;
    }

    public Projectile(int x, int y, double direction){
        this();
        this.x = x;
        this.y = y;
        this.direction = direction;
    }

    public boolean travel(double dt) {
        this.timeSinceCreation += dt;
        if(this.timeSinceCreation > lifetime){
            return true; // please destroy me!
        }

        double dx = Math.cos(Math.toRadians(this.direction));
        double dy = Math.sin(Math.toRadians(this.direction));
        this.move(dx, dy, dt);
        return false;
    }

}
