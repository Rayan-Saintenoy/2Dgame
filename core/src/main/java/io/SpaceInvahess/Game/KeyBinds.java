package io.SpaceInvahess.Game;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class KeyBinds {

    private int moveUp;
    private int moveDown;
    private int moveBackward;
    private int moveForward;
    private int fire;
    private boolean keyPressed;
    private boolean isWarthog;

    private Resolution resolution;
    private boolean isFullScreen = false;

    private Preferences preferences;

    public int getMoveUp() {
        return moveUp;
    }

    public void setMoveUp(int moveUp) {
        this.moveUp = moveUp;
        saveKeyBindings();
    }

    public int getMoveDown() {
        return moveDown;
    }

    public void setMoveDown(int moveDown) {
        this.moveDown = moveDown;
        saveKeyBindings();
    }

    public int getMoveBackward() {
        return moveBackward;
    }

    public void setMoveBackward(int moveBackward) {
        this.moveBackward = moveBackward;
        saveKeyBindings();
    }

    public int getMoveForward() {
        return moveForward;
    }

    public void setMoveForward(int moveForward) {
        this.moveForward = moveForward;
        saveKeyBindings();
    }

    public int getFire() {
        return fire;
    }

    public void setFire(int fire) {
        this.fire = fire;
        saveKeyBindings();
    }

    public boolean isKeyPressed() {
        return keyPressed;
    }

    public void setKeyPressed(boolean keyPressed) {
        this.keyPressed = keyPressed;
    }

    public boolean isWarthog() {
        return isWarthog;
    }

    public void setWarthog(boolean warthog) {
        this.isWarthog = warthog;
        saveKeyBindings();
    }

    public Resolution getResolution() {
        return resolution;
    }

    public void setResolution(Resolution resolution) {
        this.resolution = resolution;
        saveKeyBindings();
    }

    public boolean isFullScreen() {
        return isFullScreen;
    }

    public void setFullScreen(boolean fullScreen) {
        this.isFullScreen = fullScreen;
    }

    public int[] getAllKeys() {
        return new int[]{moveUp, moveDown, moveBackward, moveForward, fire};
    }

    public int getWidth() {
        return resolution.getWidth();
    }

    public int getHeight() {
        return resolution.getHeight();
    }

    public KeyBinds() {
        preferences = Gdx.app.getPreferences("SpaceInvahessSettings");

        loadKeyBindings();
        this.keyPressed = false;
    }

    private void loadKeyBindings() {
        moveUp = preferences.getInteger("moveUp", Input.Keys.W);
        moveDown = preferences.getInteger("moveDown", Input.Keys.S);
        moveBackward = preferences.getInteger("moveBackward", Input.Keys.A);
        moveForward = preferences.getInteger("moveForward", Input.Keys.D);
        fire = preferences.getInteger("fire", Input.Keys.SPACE);
        isWarthog = preferences.getBoolean("isWarthog", true);

        String resolutionString = preferences.getString("resolution", Resolution.R1920x1080.name());
        this.resolution = Resolution.valueOf(resolutionString);
    }

    private void saveKeyBindings() {
        preferences.putInteger("moveUp", moveUp);
        preferences.putInteger("moveDown", moveDown);
        preferences.putInteger("moveBackward", moveBackward);
        preferences.putInteger("moveForward", moveForward);
        preferences.putInteger("fire", fire);
        preferences.putBoolean("isWarthog", isWarthog);
        preferences.putString("resolution", resolution.name());

        preferences.flush();
    }

    public enum Resolution {
        R1920x1080(1920, 1080),
        R1280x720(1280, 720),
        R1366x768(1366, 768),
        R2560x1440(2560, 1440);

        private final int width;
        private final int height;

        Resolution(int width, int height) {
            this.width = width;
            this.height = height;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }
    }
}
