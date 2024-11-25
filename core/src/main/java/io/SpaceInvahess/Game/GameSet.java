package io.SpaceInvahess.Game;

import com.badlogic.gdx.math.Rectangle;

interface GameSet {
    public float getX();
    public float getY();
    public float getSpeed();
    public int getDmg();
    public void update(float deltaTime);
    public void receiveDamage(int dmg);
    public Rectangle getBoundingBox();
}
