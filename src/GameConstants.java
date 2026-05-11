/**
 * save all the game constants for easier acces.
 */
public final class GameConstants {

    /**
     * just consstant class - should not be instantiated.
     */
    private GameConstants() {
    }

    /**
     * width in pixels.
     */
    public static final int WINDOW_WIDTH = 800;

    /**
     * window height in pixels.
     */
    public static final int WINDOW_HEIGHT = 600;

    /**
     * value for deveiton.
     */
    public static final double EPSILON = 1e-6;

    /**
     * value for distance that is not valid.
     */
    public static final double THE_NULL_DISTANCE = -1;

    /**
     * default delay.
     */
    public static final int FRAME_DELAY_MS = 20;

    /**
     * default ball radius.
     */
    public static final int DEFAULT_BALL_RADIUS = 10;

    /**
     * safety margin multiplier.
     */
    public static final double SAFETY_MARGIN_MULTIPLIER = 1.5;
}
