package io.SpaceInvahess.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;

public class MainMenuScreen implements Screen {

    private final Main game;
    private Skin mySkin;
    private Stage stage;
    private Texture background;
    private SpriteBatch batch;
    private Screen screen = this;

    public MainMenuScreen(final Main game) {
        this.game = game;
        game.myAssetManager.queueAddSkin();
        game.myAssetManager.manager.finishLoading();
        mySkin = game.myAssetManager.manager.get(GameConstants.skin);
        stage = new Stage(game.screenPort);
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void show() {
        game.screenPort.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
        Table table = new Table();
        table.top().center();
        table.setFillParent(true);
        table.setSkin(mySkin);

        background = new Texture("background_bluesky00.png");
        batch = new SpriteBatch();

        Label gameTitle = new Label("Space Invahess", mySkin, "big");
        gameTitle.setAlignment(Align.center);

        Button startBtn = new TextButton("New Game", mySkin, "small");
        startBtn.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                game.gotoGameScreen(screen);
                return true;
            }
        });

        Button settingsBtn = new TextButton("Settings", mySkin, "small");
        settingsBtn.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                game.gotoSettingsScreen(screen);
                return true;
            }
        });

        Button demoBtn = new TextButton("Demo", mySkin, "small");
        demoBtn.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                game.gotoDemoScreen(screen);
                return true;
            }
        });

        Button exitBtn = new TextButton("Exit", mySkin, "small");
        exitBtn.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.exit();
                return true;
            }
        });

        table.add(gameTitle).padBottom(50).colspan(2).center().row();
        table.add(startBtn).padBottom(20).fillX().uniformX().row();
        table.add(settingsBtn).padBottom(20).fillX().uniformX().row();
        table.add(demoBtn).padBottom(20).fillX().uniformX().row();
        table.add(exitBtn).fillX().uniformX().row();

        stage.addActor(table);
    }

    @Override
    public void render(float delta) {
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));

        // Dessiner le fond
        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();

        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        game.screenPort.update(width, height, true);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        background.dispose();
        batch.dispose();
        stage.dispose();
    }

}
