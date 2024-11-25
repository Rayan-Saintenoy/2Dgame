package io.SpaceInvahess.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.ArrayList;
import java.util.List;

public abstract class Warplane extends Entity implements Fight{
    private int numOfLife = 3;
    private KeyBinds keyBinds = new KeyBinds();
    private List<Bullet> bullets;
    private int baseHeight = 3;
    private boolean moving;
    private Texture idle;
    private Texture up;
    private Texture down;

    public int getBaseHeight() {
        return baseHeight;
    }

    public int getNumOfLife() {
        return numOfLife;
    }

    public void setNumOfLife(int numOfLife) {
        this.numOfLife = numOfLife;
    }

    public KeyBinds getKeyBinds() {
        return keyBinds;
    }

    public List<Bullet> getBullets() {
        return bullets;
    }

    public boolean isMoving() {
        return moving;
    }

    public void setMoving(boolean moving) {
        this.moving = moving;
    }

    public Warplane(float x, float y, float speed, Texture texture, long timeBetweenBullets) {
        super(x, y, 12, 3, speed, texture);
         this.setAlive(true);
         this.bullets = new ArrayList<>();
         this.setTimeBetweenBullets(timeBetweenBullets);
    }

    @Override
    public void update(float deltaTime) {
        if(this instanceof Warthog) {
            idle = new Texture("warthogIdle.png");
            up = new Texture("warthogUp.png");
            down = new Texture("warthogDown.png");
        } else if (this instanceof Rafale) {
            idle = new Texture("rafaleIdle.png");
            up = new Texture("rafaleUp.png");
            down = new Texture("rafaleDown.png");
        } else {
            idle = new Texture("");
            up = new Texture("");
            down = new Texture("");
        }

        boolean moveUp = Gdx.input.isKeyPressed(this.getKeyBinds().getMoveUp());
        boolean moveDown = Gdx.input.isKeyPressed(this.getKeyBinds().getMoveDown());
        boolean moveForward = Gdx.input.isKeyPressed(this.getKeyBinds().getMoveForward());
        boolean moveBackward = Gdx.input.isKeyPressed(this.getKeyBinds().getMoveBackward());

        if(!moveUp || !moveDown) {
            this.setMoving(false);
        }

        if (moveUp) {
            float y = this.getY();
            y += this.getSpeed() * deltaTime;
            this.setY(Math.min(y, 72 - this.getHeight()));
            this.setMoving(true);
            this.setTexture(up);

            if (!this.getKeyBinds().isKeyPressed()) {
                this.setHeight(getBaseHeight() + 4);
                this.getKeyBinds().setKeyPressed(true);
            }
        } else if (moveDown) {
            float y = this.getY();
            y -= this.getSpeed() * deltaTime;
            this.setY(Math.max(y, 0));
            this.setMoving(true);
            this.setTexture(down);

            if (!this.getKeyBinds().isKeyPressed()) {
                this.setHeight(getBaseHeight() + 4);
                this.getKeyBinds().setKeyPressed(true);
            }
        } else {
            this.setTexture(idle);
            this.setHeight(getBaseHeight());
            this.getKeyBinds().setKeyPressed(false);
        }

        if (moveBackward) {
            float x = this.getX();
            x -= this.getSpeed() * deltaTime;
            this.setX(Math.max(x, 0));
        }

        if (moveForward) {
            float x = this.getX();
            x += this.getSpeed() * deltaTime;
            this.setX(Math.min(x, 128 - this.getWidth()));
        }

        if (Gdx.input.isKeyPressed(this.getKeyBinds().getFire())) {
            long currentTime = TimeUtils.millis();

            if (currentTime - this.getLastShotTime() >= this.getTimeBetweenBullets()) {
                this.shoot();
                this.setLastShotTime(currentTime);
            }
        }
    }


}
