import biuoop.DrawSurface;
import biuoop.GUI;
import biuoop.Sleeper;

/**
 * Runs an animation with multiple bouncing balls.
 */
public class MultipleBouncingBallsAnimation {
    /**
     * Parses command-line arguments and starts the animation.
     *
     * @param args command-line arguments.
     */
    public static void main(String[] args) {
        int[] intArray = GameEngine.parseArgsForPositiveIntegers(args);
        if (intArray == null) {
            return;
        }
        drawbals(intArray);
    }

    /**
     * Draws the animation for a list of ball radii.
     *
     * @param intArray array of ball radii
     */
    private static void drawbals(int[] intArray) {
        Rectangle bounds = new Rectangle(0, 0, GameConstants.WINDOW_WIDTH, GameConstants.WINDOW_HEIGHT);
        Ball[] balls = GameEngine.createValidBallsArr(intArray);
        for (Ball ball : balls) {
            ball.addObstacles(bounds.getLines());
            ball.setVelocity(GameEngine.getVelocityFromRadius(ball.getSize()));
        }

        GUI gui = new GUI("Bouncing Ball", GameConstants.WINDOW_WIDTH, GameConstants.WINDOW_HEIGHT);
        Sleeper sleeper = new Sleeper();

        while (true) {

            for (Ball ball : balls) {
                ball.moveOneStep();
            }
            DrawSurface surface = gui.getDrawSurface();
            for (Ball ball : balls) {
                ball.drawOn(surface);
            }
            gui.show(surface);
            sleeper.sleepFor(GameConstants.FRAME_DELAY_MS);
        }
    }
}
