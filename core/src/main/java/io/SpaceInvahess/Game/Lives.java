package io.SpaceInvahess.Game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Lives {
    private int numOfLife;
    private float x;
    private float y;
    private Texture[] textures;

    public Lives(int numOfLife, float x, float y) {
        this.x = x;
        this.y = y;
        this.numOfLife = Math.max(0, Math.min(numOfLife, 3));

        this.textures = new Texture[] {
            new Texture("heart03.png"),
            new Texture("heart02.png"),
            new Texture("heart01.png"),
            new Texture("heart00.png")
        };
    }

    public void setLives(int numOfLife) {
        this.numOfLife = Math.max(0, Math.min(numOfLife, 3));
    }

    public void draw(SpriteBatch batch) {
        batch.draw(textures[numOfLife], x, y, 12, 3);
    }

    public void dispose() {
        for (Texture texture : textures) {
            texture.dispose();
        }
    }
}
