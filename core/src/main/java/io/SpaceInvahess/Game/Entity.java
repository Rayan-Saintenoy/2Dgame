package io.SpaceInvahess.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;

public abstract class Entity implements GameSet{
    private int hp;
    private float x;
    private float y;
    private int width;
    private int height;
    private boolean alive;
    private float speed;
    private int dmg;
    private Texture texture;
    private long timeBetweenBullets;
    private long lastShotTime;
    private boolean canReceiveDamage;

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    @Override
    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    @Override
    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public void setDmg(int dmg) {
        this.dmg = dmg;
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    @Override
    public float getSpeed() {
        return speed;
    }

    @Override
    public int getDmg() {
        return dmg;
    }

    public float getTimeBetweenBullets() {
        return timeBetweenBullets;
    }

    public void setTimeBetweenBullets(long timeBetweenBullets) {
        this.timeBetweenBullets = timeBetweenBullets;
    }

    public long getLastShotTime() {
        return lastShotTime;
    }

    public void setLastShotTime(long lastShotTime) {
        this.lastShotTime = lastShotTime;
    }

    public void setCanReceiveDamage(boolean canReceiveDamage) {
        this.canReceiveDamage = canReceiveDamage;
    }

    public Entity(float x, float y, int width, int height, float speed, Texture texture) {
        this.x = x - width / 2;
        this.y = y - height / 2;
        this.width = width;
        this.height = height;
        this.speed = speed;
        this.texture = texture;
        this.lastShotTime = 0;
        this.setHp(150);
        this.canReceiveDamage = true;
    }

    public boolean intersects(Rectangle otherRectangle){
        Rectangle thisRectangle = new Rectangle(this.getX(), this.getY(), this.getWidth(), this.getHeight());
        return thisRectangle.overlaps(otherRectangle);
    }

    public void draw(Batch batch){
        if (blinking) {
            blinkTime += Gdx.graphics.getDeltaTime();

            if ((int) (blinkTime * 20) % 2 == 0) {
                batch.setColor(1, 0, 0, 1);
            } else {
                batch.setColor(1, 1, 1, 1);
            }

            if (blinkTime > 0.4f) {
                blinking = false;
            }
        }

        batch.draw(texture, x, y, width, height);
        batch.setColor(1, 1, 1, 1);

    }

    private float blinkTime = 0;
    private boolean blinking = false;

    @Override
    public void receiveDamage(int dmg) {
        Sound hitSound = Gdx.audio.newSound(Gdx.files.internal("hit1.wav"));
        Sound deathSound = Gdx.audio.newSound(Gdx.files.internal("boom5.wav"));
        Sound cantTouchThis = Gdx.audio.newSound(Gdx.files.internal("cantTouchThis.wav"));
        if (canReceiveDamage) {
            this.setHp(this.getHp() - dmg);
            hitSound.play();
            if (this.getHp() <= 0) {
                this.setAlive(false);
                hitSound.play();
                deathSound.play();

            }
            blinking = true;
            blinkTime = 0;

            System.out.println("Damage Received: " + dmg);
            System.out.println("Remaining HP: " + this.getHp());
        }
        else {
            cantTouchThis.play(0.4f, 0.5f, 1);
        }
    }

    public void dispose() {
        if (texture != null) {
            texture.dispose();
        }
    }

    @Override
    public Rectangle getBoundingBox(){
        return new Rectangle(this.getX(), this.getY(), this.getWidth(), this.getHeight());
    }
}

