package io.SpaceInvahess.Game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class Explosion {

    private Animation<TextureRegion> explosionAnimation;
    private float explosionTimer;

    private Rectangle boundingBox;

    public Explosion(Texture texture, Rectangle boundingBox, float totalAnimationTime) {
        this.boundingBox = boundingBox;

        int frameCount = texture.getWidth() / 32;
        TextureRegion[] textureRegion1D = new TextureRegion[frameCount];
        for (int i = 0; i < frameCount; i++) {
            textureRegion1D[i] = new TextureRegion(texture, i * 32, 0, 32, 32);
        }

        explosionAnimation = new Animation<TextureRegion>(totalAnimationTime / frameCount, textureRegion1D);
        explosionTimer = 0;
    }

    public void update(float deltaTime) {
        explosionTimer += deltaTime;
    }

    public void draw(SpriteBatch batch) {
        batch.draw(
            explosionAnimation.getKeyFrame(explosionTimer),
            boundingBox.x,
            boundingBox.y,
            boundingBox.width,
            boundingBox.width
        );
    }

    public boolean isFinished() {
        return explosionAnimation.isAnimationFinished(explosionTimer);
    }
}
