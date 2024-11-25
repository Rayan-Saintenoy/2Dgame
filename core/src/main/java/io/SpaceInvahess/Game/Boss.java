package io.SpaceInvahess.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

public class Boss extends Invahess {

    public Boss(float x, float y, float speed, String patternName) {
        super(x, y, 24, 16, speed, new Texture("boss.png"), 100, patternName);
        this.setHp(500);
    }

    @Override
    public boolean shoot() {
        Sound bossShootSound = Gdx.audio.newSound(Gdx.files.internal("bossShoot.wav"));

        if (this.isAlive()) {
            Laser ammo = new Laser(this, this.getAngle());
            this.getBullets().add(ammo);
            bossShootSound.play();
            return true;
        }
        return false;
    }
}
