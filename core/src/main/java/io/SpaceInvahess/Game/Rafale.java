package io.SpaceInvahess.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

public class Rafale extends Warplane{

    public Rafale(float x, float y, float speed) {
        super(x, y, speed, new Texture("rafaleIdle.png"), 250);
    }

    @Override
    public boolean shoot() {
        Sound rafaleShootSound = Gdx.audio.newSound(Gdx.files.internal("rafaleShoot.wav"));

        if(this.isAlive()) {
            Rocket ammo = new Rocket(this);
            this.getBullets().add(ammo);
            rafaleShootSound.play();
            return true;
        } else {
            System.out.println("Vous ne pouvez pas tirer si vous êtes mort");
            return false;
        }
    }
}
