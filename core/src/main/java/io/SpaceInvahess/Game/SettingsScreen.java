package io.SpaceInvahess.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.Preferences;

public class SettingsScreen implements Screen {

    private Stage stage;
    private SpriteBatch batch;
    private Skin skin;
    private OrthographicCamera camera;
    private final Main game;
    private KeyBinds keyBinds;

    private TextField moveLeftField;
    private TextField moveRightField;
    private TextField moveUpField;
    private TextField moveDownField;
    private TextField shootField;

    private TextField activeTextField;

    private Preferences preferences;

    private SelectBox<String> planeSelectBox;
    private Image planePreviewImage;
    private Texture warthogTexture;
    private Texture rafaleTexture;

    private SelectBox<String> resolutionSelectBox;

    private Screen screen = this;

    public SettingsScreen(Main game) {
        this.game = game;
        keyBinds = new KeyBinds();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1280, 720);
        batch = new SpriteBatch();

        stage = new Stage(new FitViewport(1280, 720, camera));
        skin = new Skin(Gdx.files.internal("skin/glassy-ui.json"));
        preferences = Gdx.app.getPreferences("SpaceInvahessSettings");

        loadKeyBindings();
        loadPlaneTextures();

        Gdx.input.setInputProcessor(stage);
    }

    private void loadPlaneTextures() {
        warthogTexture = new Texture("warthogIdle.png");
        rafaleTexture = new Texture("rafaleIdle.png");
    }

    @Override
    public void show() {
        Table table = new Table();
        table.center();
        table.setFillParent(true);
        table.setSkin(skin);

        Label settingsTitle = new Label("Settings",skin,"big");
        settingsTitle.setSize(GameConstants.col_width*2,GameConstants.row_height*2);
        settingsTitle.setPosition(GameConstants.centerX - settingsTitle.getWidth()/2,GameConstants.centerY + GameConstants.row_height);
        settingsTitle.setAlignment(Align.center);

        moveLeftField = createKeyBindingField(keyBinds.getMoveBackward());
        moveRightField = createKeyBindingField(keyBinds.getMoveForward());
        moveUpField = createKeyBindingField(keyBinds.getMoveUp());
        moveDownField = createKeyBindingField(keyBinds.getMoveDown());
        shootField = createKeyBindingField(keyBinds.getFire());

        // Replace the CheckBox with a SelectBox
        planeSelectBox = new SelectBox<>(skin);
        planeSelectBox.setItems("Warthog", "Rafale");
        planeSelectBox.setSelected(keyBinds.isWarthog() ? "Warthog" : "Rafale");

        // Add a listener to update the selected plane
        planeSelectBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                String selected = planeSelectBox.getSelected();
                keyBinds.setWarthog(selected.equals("Warthog"));
                updatePlanePreview();
            }
        });
        planePreviewImage = new Image(warthogTexture);
        updatePlanePreview();

        resolutionSelectBox = new SelectBox<>(skin);
        resolutionSelectBox.setItems( "R1280x720", "R1366x768", "R1920x1080", "R2560x1440");
        resolutionSelectBox.setSelected(keyBinds.getResolution().name());
        resolutionSelectBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                String selected = resolutionSelectBox.getSelected();
                Resolution applySelectedResolution = Resolution.valueOf(selected);
                KeyBinds.Resolution selectedResolution = KeyBinds.Resolution.valueOf(selected);
                keyBinds.setResolution(selectedResolution);
                applyResolution(applySelectedResolution);
            }
        });

        TextButton backButton = new TextButton("Back to Menu", skin);
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                saveKeyBindings();
                game.gotoMenuScreen(screen);
            }
        });

        table.add(settingsTitle).padBottom(20).row();
        table.add("Move Left: ").padRight(10);
        table.add(moveLeftField).padBottom(20).row();
        table.add("Move Right: ").padRight(10);
        table.add(moveRightField).padBottom(20).row();
        table.add("Move Up: ").padRight(10);
        table.add(moveUpField).padBottom(20).row();
        table.add("Move Down: ").padRight(10);
        table.add(moveDownField).padBottom(20).row();
        table.add("Shoot: ").padRight(10);
        table.add(shootField).padBottom(20).row();

        table.add("Select Plane: ").padRight(10);
        table.add(planeSelectBox).padBottom(20).row();

        table.add("Preview: ").padRight(10);
        table.add(planePreviewImage).size(120, 30).padBottom(20).row();

        table.add("Resolution: ").padRight(10);
        table.add(resolutionSelectBox).padBottom(20).row();

        table.add(backButton).fillX().uniformX().padBottom(20).row();

        stage.addActor(table);
    }

    private void updatePlanePreview() {
        if (keyBinds.isWarthog()) {
            planePreviewImage.setDrawable(new Image(warthogTexture).getDrawable());
        } else {
            planePreviewImage.setDrawable(new Image(rafaleTexture).getDrawable());
        }
    }

    private TextField createKeyBindingField(final int defaultKey) {
        TextField textField = new TextField(Input.Keys.toString(defaultKey), skin);
        textField.setMessageText("Press a key...");

        textField.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                activeTextField = textField;
                textField.setText("");
                textField.setMessageText("Press a key...");
            }
        });

        return textField;
    }

    @Override
    public void render(float delta) {
        if (activeTextField != null) {
            for (int key = 0; key < 256; key++) {
                if (Gdx.input.isKeyJustPressed(key)) {
                    String keyName = Input.Keys.toString(key);
                    activeTextField.setText(keyName);
                    updateKeyBinding(key);
                    break;
                }
            }
        }

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
        batch.end();
    }

    private void updateKeyBinding(int key) {
        if (activeTextField == moveLeftField) {
            keyBinds.setMoveBackward(key);
        } else if (activeTextField == moveRightField) {
            keyBinds.setMoveForward(key);
        } else if (activeTextField == moveUpField) {
            keyBinds.setMoveUp(key);
        } else if (activeTextField == moveDownField) {
            keyBinds.setMoveDown(key);
        } else if (activeTextField == shootField) {
            keyBinds.setFire(key);
        }

        saveKeyBindings();
        activeTextField = null;
    }

    private void saveKeyBindings() {
        preferences.putInteger("moveLeft", keyBinds.getMoveBackward());
        preferences.putInteger("moveRight", keyBinds.getMoveForward());
        preferences.putInteger("moveUp", keyBinds.getMoveUp());
        preferences.putInteger("moveDown", keyBinds.getMoveDown());
        preferences.putInteger("shoot", keyBinds.getFire());
        preferences.putBoolean("isWarthog", keyBinds.isWarthog());
        preferences.flush();
    }

    private void loadKeyBindings() {
        keyBinds.setMoveBackward(preferences.getInteger("moveLeft", Input.Keys.A));
        keyBinds.setMoveForward(preferences.getInteger("moveRight", Input.Keys.D));
        keyBinds.setMoveUp(preferences.getInteger("moveUp", Input.Keys.W));
        keyBinds.setMoveDown(preferences.getInteger("moveDown", Input.Keys.S));
        keyBinds.setFire(preferences.getInteger("shoot", Input.Keys.SPACE));
        keyBinds.setWarthog(preferences.getBoolean("isWarthog", true));
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    private void applyResolution(Resolution resolution) {
        switch (resolution) {
            case R1920x1080:
                Gdx.graphics.setWindowedMode(1920, 1080);
                break;
            case R1280x720:
                Gdx.graphics.setWindowedMode(1280, 720);
                break;
            case R1366x768:
                Gdx.graphics.setWindowedMode(1366, 768);
                break;
            case R2560x1440:
                Gdx.graphics.setWindowedMode(2560, 1440);
                break;
        }
    }

    @Override
    public void hide() { }

    @Override
    public void pause() { }

    @Override
    public void resume() { }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
        warthogTexture.dispose();
        rafaleTexture.dispose();
    }

}
