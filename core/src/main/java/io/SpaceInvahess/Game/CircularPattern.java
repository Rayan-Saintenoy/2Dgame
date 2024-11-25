package io.SpaceInvahess.Game;

import com.badlogic.gdx.math.Vector2;

public class CircularPattern implements EnemyPattern {
    private float elapsedTime = 0;
    private Vector2 center;
    private float radius;

    public CircularPattern(Vector2 center, float radius) {
        this.center = center;
        this.radius = radius;
    }

    @Override
    public Vector2 updatePosition(float deltaTime, Vector2 currentPosition, float speed) {
        elapsedTime += deltaTime;
        float angle = elapsedTime * speed * 3;

        float newX = center.x + (float) Math.cos(angle) * radius;
        float newY = center.y + (float) Math.sin(angle) * radius;

        return new Vector2(newX, newY);
    }
}
