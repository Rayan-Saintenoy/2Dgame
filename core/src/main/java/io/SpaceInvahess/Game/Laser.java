package io.SpaceInvahess.Game;

public class Laser extends Bullet{
    private float angle;

    public Laser(Troop troop) {
        super(3, 3, 30f, troop);
        this.setDmg(10);
    }

    public Laser(Boss boss, int angle) {
        super(5, 5, 30f, boss);
        this.setDmg(30);
        this.angle = angle;
    }

    @Override
    public void update(float deltaTime) {
        float radians = (float) Math.toRadians(angle);
        float dx = (float) Math.cos(radians) * this.getSpeed() * deltaTime;
        float dy = (float) Math.sin(radians) * this.getSpeed() * deltaTime;

        this.setX(this.getX() - dx);
        this.setY(this.getY() + dy);
    }

}
