package io.SpaceInvahess.Game;

import com.badlogic.gdx.graphics.Texture;

public class Caliber30Mm extends Bullet{
    public Caliber30Mm(Warthog warthog) {
        super(4, 1, 10,150f, warthog, new Texture("bullet01.png"));
    }
}
