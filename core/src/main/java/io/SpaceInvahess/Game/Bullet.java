package io.SpaceInvahess.Game;

import com.badlogic.gdx.graphics.Texture;

public abstract class Bullet extends Entity{

    public Bullet(int width, int height, int dmg, float speed, Warplane warplane, Texture texture) {
        super(warplane.getX() + 15, warplane.getY(), width, height, speed, texture);
        this.setDmg(dmg);

         if (warplane.isMoving()) {
             this.setY(this.getY() + 3);
         } else {
             this.setY(warplane.getY());
         }
    }

    public Bullet(int width, int height, float speed, Invahess invahess) {
        super(invahess.getX(), invahess.getY(), width, height, speed, new Texture("bulletInvahess01.png"));

        if(invahess instanceof Boss) {
            this.setX(this.getX() - 3);
            this.setY(this.getY() + 10);
        }
    }

    @Override
    public void update(float deltaTime) {
        float x = this.getX();
        x += this.getSpeed() * deltaTime;
        this.setX(x);
    }
}
