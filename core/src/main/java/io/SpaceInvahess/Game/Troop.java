package io.SpaceInvahess.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

public class Troop extends Invahess{

    public Troop(float x, float y, float speed, Texture texture, String patternName) {
        super(x, y,8, 4, speed, texture, 2000, patternName);
        this.setHp(50);
    }

    @Override
    public boolean shoot() {
        Sound troopShootSound = Gdx.audio.newSound(Gdx.files.internal("laserShoot.wav"));

        if (this.isAlive()) {
            Laser ammo = new Laser(this);
            this.getBullets().add(ammo);
            troopShootSound.play();
            return true;
        }
        return false;
    }
}
