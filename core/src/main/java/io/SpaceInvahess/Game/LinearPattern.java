package io.SpaceInvahess.Game;

import com.badlogic.gdx.math.Vector2;

public class LinearPattern implements EnemyPattern {
    private Vector2 direction;

    private static final float SPEED_MULTIPLIER = 20f;

    public LinearPattern(Vector2 direction) {
        this.direction = new Vector2(direction).nor();
    }

    @Override
    public Vector2 updatePosition(float deltaTime, Vector2 currentPosition, float speed) {
        float effectiveSpeed = speed * SPEED_MULTIPLIER;

        float newX = currentPosition.x + (direction.x * effectiveSpeed * deltaTime);
        float newY = currentPosition.y + (direction.y * effectiveSpeed * deltaTime);

            if(newX < -6){
                newX = 134;
            }

            if(newX > 134){
                newX = -6;
            }

            if(newY < -6){
                newY = 78;
            }

            if(newY > 78){
                newY = -6;
            }

        return new Vector2(newX, newY);
    }
}
