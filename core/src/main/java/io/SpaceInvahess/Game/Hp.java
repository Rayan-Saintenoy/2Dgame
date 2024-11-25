package io.SpaceInvahess.Game;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Color;

public class Hp{
    private BitmapFont font;
    private float x;
    private float y;

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public Hp(float x, float y) {
        this.x = x;
        this.y = y;
        font = new BitmapFont();
        font.getData().setScale(0.3f);
        font.setColor(Color.RED);
    }

    public void draw(SpriteBatch batch, int hp) {
        font.draw(batch, "HP: " + hp,  getX(), getY());
    }
}
