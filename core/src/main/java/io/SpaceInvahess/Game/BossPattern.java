package io.SpaceInvahess.Game;

import com.badlogic.gdx.math.Vector2;

public class BossPattern implements EnemyPattern {
    private Vector2 direction;
    private boolean movingDown;

    private static final float SPEED_MULTIPLIER = 20f;

    public BossPattern(Vector2 direction) {
        this.direction = new Vector2(direction).nor();
        this.movingDown = false;
    }

    @Override
    public Vector2 updatePosition(float deltaTime, Vector2 currentPosition, float speed) {
        float effectiveSpeed = speed * SPEED_MULTIPLIER;

        float newX = currentPosition.x + (direction.x * 0 * deltaTime);

        float newY;

        if (movingDown) {
            newY = currentPosition.y - (direction.y * effectiveSpeed * deltaTime);
            if (newY <= 0) {
                newY = 0;
                movingDown = false;
            }
        } else {
            newY = currentPosition.y + (direction.y * effectiveSpeed * deltaTime);
            if (newY >= 57) {
                newY = 57;
                movingDown = true;
            }
        }

        return new Vector2(newX, newY);
    }
}
