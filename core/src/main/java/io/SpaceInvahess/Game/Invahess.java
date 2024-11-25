package io.SpaceInvahess.Game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public abstract class Invahess extends Entity implements Fight {
    private List<Bullet> bullets;
    private EnemyPattern pattern;
    private String patternName;
    private boolean canShoot;
    private boolean doPattern;
    private float distanceTravelled = 0f;
    private final float maxDistance = 93f;
    private boolean angleIncreasing = true;
    private int angle = 45;

    public void setCanShoot(boolean canShoot) {
        this.canShoot = canShoot;
    }

    public List<Bullet> getBullets() {
        return bullets;
    }


    public void setPattern(String patternName) {
        switch (patternName) {
            case "circular":
                this.pattern = new CircularPattern(new Vector2(this.getX(), this.getY()), 3);
                break;
            case "boss":
                this.pattern = new BossPattern(new Vector2(this.getX(), this.getY()));
                break;
            case "linearUp":
                this.pattern = new LinearPattern(new Vector2(0, 1));
                break;
            case "linearDown":
                this.pattern = new LinearPattern(new Vector2(0, -1));
                break;
            default:
                System.err.println("Invalid pattern name: " + patternName);
                break;
        }

    }

    public int getAngle() {
        return angle;
    }

    public Invahess(float x, float y, int width, int height, float speed, Texture texture, long timeBetweenBullets, String patternName) {
        super(x, y, width, height, speed, texture);
        this.setAlive(true);
        this.bullets = new ArrayList<>();
        this.setTimeBetweenBullets(timeBetweenBullets);
        this.patternName = patternName;
        this.canShoot = true;
        this.doPattern = false;
    }

    private void updateAngle() {
        if (angleIncreasing) {
            angle += 15;
            if (angle >= 45) {
                angle = 45;
                angleIncreasing = false;
            }
        } else {
            angle -= 15;
            if (angle <= -45) {
                angle = -45;
                angleIncreasing = true;
            }
        }
    }

    @Override
    public void update(float deltaTime) {

        long currentTime = TimeUtils.millis();

        if (!doPattern) {
            this.setCanReceiveDamage(false);
            canShoot = false;
            float distanceStep = this.getSpeed() * 20 * deltaTime;
            this.distanceTravelled += distanceStep;

            float x = this.getX();
            x -= distanceStep;
            this.setX(x);

            if (this.distanceTravelled >= this.maxDistance) {
                canShoot = true;
                this.setPattern(patternName);
                doPattern = true;
            }
        } else {
            this.setCanReceiveDamage(true);
            Vector2 newPosition = this.pattern.updatePosition(deltaTime, new Vector2(this.getX(), this.getY()), this.getSpeed());
            this.setX(newPosition.x);
            this.setY(newPosition.y);
        }

        if (canShoot) {
            if (currentTime - this.getLastShotTime() >= this.getTimeBetweenBullets()) {
                this.shoot();
                if (patternName == "boss") {
                    this.updateAngle();
                }
                this.setLastShotTime(currentTime);
            }
            ListIterator<Bullet> iterator = this.getBullets().listIterator();
            while (iterator.hasNext()) {
                Bullet bullet = iterator.next();
                bullet.update(deltaTime);
            }
        }
    }
}
