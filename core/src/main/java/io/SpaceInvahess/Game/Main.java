package io.SpaceInvahess.Game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;

public class Main extends Game {

    private SpriteBatch batch;
    public Viewport screenPort;
    public MyAssetManager myAssetManager = new MyAssetManager();
    private KeyBinds keyBinds;

    @Override
    public void create() {
        batch = new SpriteBatch();
        OrthographicCamera camera = new OrthographicCamera();
        camera.setToOrtho(false);
        screenPort = new ScreenViewport();

        keyBinds = new KeyBinds();
        applyResolution(keyBinds.getResolution(), keyBinds.isFullScreen());

        this.setScreen(new MainMenuScreen(this));

        Gdx.graphics.setResizable(false);

        Music music = Gdx.audio.newMusic(Gdx.files.internal("Space Rider.ogg"));
        music.setVolume(0.7f);
        music.setLooping(true);
        music.play();
    }

    private void applyResolution(KeyBinds.Resolution resolution, boolean fullscreen) {
        Graphics.DisplayMode nativeMode = Gdx.graphics.getDisplayMode();

        if (resolution.getWidth() > nativeMode.width || resolution.getHeight() > nativeMode.height) {
            resolution = getMaxResolution(nativeMode);
        }

        if (fullscreen) {
            Gdx.graphics.setFullscreenMode(nativeMode);
        } else {

            Gdx.graphics.setWindowedMode(resolution.getWidth(), resolution.getHeight());
        }
    }

    private KeyBinds.Resolution getMaxResolution(Graphics.DisplayMode nativeMode) {
        for (KeyBinds.Resolution res : KeyBinds.Resolution.values()) {
            if (res.getWidth() <= nativeMode.width && res.getHeight() <= nativeMode.height) {
                return res;
            }
        }
        return KeyBinds.Resolution.R1280x720;
    }

    public void gotoMenuScreen(Screen screen) {
        screen.dispose();
        MainMenuScreen menuScreen = new MainMenuScreen(this);
        setScreen(menuScreen);
    }

    public void gotoSettingsScreen(Screen screen) {
        screen.dispose();
        SettingsScreen settingsScreen = new SettingsScreen(this);
        setScreen(settingsScreen);
    }

    public void gotoGameScreen(Screen screen) {
        screen.dispose();
        GameScreen gameScreen = new GameScreen(this);
        setScreen(gameScreen);
    }

    public void gotoDemoScreen(Screen screen) {
        screen.dispose();
        DemoScreen demoScreen = new DemoScreen(this);
        setScreen(demoScreen);
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
