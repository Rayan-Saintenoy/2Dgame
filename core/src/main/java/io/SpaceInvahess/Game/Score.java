package io.SpaceInvahess.Game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class Score {
    private int point;
    private BitmapFont font;
    private float x;
    private float y;

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public int getPoint() {
        return point;
    }

    public Score(float x, float y) {
        this.x = x;
        this.y = y;
        this.point = 0;
        font = new BitmapFont();
        font.getData().setScale(0.3f);
        font.setColor(Color.RED );
    }

    public void updateScore(int point) {
        this.point += point;
    }

    public void draw(SpriteBatch batch) {
        font.draw(batch, "Score: " + this.getPoint(), getX(), getY());
    }

}
