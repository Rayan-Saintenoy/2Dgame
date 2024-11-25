package io.SpaceInvahess.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

public class Warthog extends Warplane{

    public Warthog(float x, float y, float speed) {
        super(x, y, speed, new Texture("warthogIdle.png"), 100);
    }

    @Override
    public boolean shoot() {
        Sound warthogShootSound = Gdx.audio.newSound(Gdx.files.internal("warthogShoot.wav"));

        if(this.isAlive()) {
            Caliber30Mm ammo = new Caliber30Mm(this);
            this.getBullets().add(ammo);
            warthogShootSound.play();
            return true;
        } else {
            System.out.println("Vous ne pouvez pas tirer si vous êtes mort");
            return false;
        }
    }

}
