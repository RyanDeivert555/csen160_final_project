package csen160.core;

public class Projectile extends Entity {

    double direction; // degrees

    public Projectile(){
        super("/projectile.png");
        this.width = 10;
        this.height = 10;
        this.speed = 500;
    }

    public Projectile(int x, int y, double direction){
        this();
        this.x = x;
        this.y = y;
        this.direction = direction;
    }

    public void moveInDirection(double dt) {
        double newX = this.x * Math.cos(Math.toRadians(direction));
        double newY = this.y * Math.sin(Math.toRadians(direction));
        this.move((int)newX, (int)newY, dt);
    }

}
