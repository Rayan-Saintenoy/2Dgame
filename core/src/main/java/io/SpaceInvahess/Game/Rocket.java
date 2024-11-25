package io.SpaceInvahess.Game;

import com.badlogic.gdx.graphics.Texture;

public class Rocket extends Bullet{
    public Rocket(Rafale rafale) {
        super(8, 3, 25,100f, rafale, new Texture("missile00.png"));
    }
}
