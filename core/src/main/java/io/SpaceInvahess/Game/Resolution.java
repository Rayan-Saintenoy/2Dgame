package io.SpaceInvahess.Game;

public enum Resolution {
    R1920x1080(1920, 1080),
    R1280x720(1280, 720),
    R1366x768(1366, 768),
    R2560x1440(2560, 1440);

    private final int width;
    private final int height;

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    Resolution(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public static Resolution getResolutionByIndex(int index) {
        return values()[index];
    }
}
