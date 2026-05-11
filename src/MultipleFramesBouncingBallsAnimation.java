import biuoop.DrawSurface;
import biuoop.GUI;
import biuoop.Sleeper;

/**
 * Task 4 Animation with multiple bouncing balls in multiple frames.
 */
public class MultipleFramesBouncingBallsAnimation {

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

        Rectangle windowBounds = new Rectangle(0, 0, GameConstants.WINDOW_WIDTH, GameConstants.WINDOW_HEIGHT);
        Rectangle greyRect = new Rectangle(50, 50, 500, 500);
        greyRect.setColor(java.awt.Color.GRAY);
        Rectangle yellowRect = new Rectangle(450, 450, 600, 600);
        yellowRect.setColor(java.awt.Color.YELLOW);
        Ball[] balls = GameEngine.createValidBallsArr(intArray);

        Rectangle[] arrayObs = {greyRect, yellowRect };

        int ballsLength = balls.length;
        int n = 0;
        for (Ball ball : balls) {
            if (n < ballsLength / 2) {
                Point p = GameEngine.randomPointInRecWithObstacls(greyRect, new Rectangle[] {yellowRect },
                        ball.getSize());
                ball.setPoint(p);
            } else {
                Point p = GameEngine.randomPointInRecWithObstacls(windowBounds, arrayObs, ball.getSize());
                ball.setPoint(p);
            }
            ball.addObstacles(windowBounds.getLines());
            ball.addObstacles(greyRect.getLines());
            ball.addObstacles(yellowRect.getLines());
            ball.setVelocity(GameEngine.getVelocityFromRadius(ball.getSize()));
            n++;
        }

        GUI gui = new GUI("Bouncing Ball", GameConstants.WINDOW_WIDTH, GameConstants.WINDOW_HEIGHT);
        Sleeper sleeper = new Sleeper();

        while (true) {
            for (Ball ball : balls) {
                ball.moveOneStep();
            }
            DrawSurface surface = gui.getDrawSurface();
            greyRect.drawOn(surface);

            for (Ball ball : balls) {
                ball.drawOn(surface);
            }
            yellowRect.drawOn(surface);
            gui.show(surface);
            sleeper.sleepFor(GameConstants.FRAME_DELAY_MS);
        }
    }

}
