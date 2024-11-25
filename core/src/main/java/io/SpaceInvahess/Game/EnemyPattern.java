package io.SpaceInvahess.Game;

import com.badlogic.gdx.math.Vector2;

public interface EnemyPattern {
    Vector2 updatePosition(float deltaTime, Vector2 currentPosition, float speed);
}
