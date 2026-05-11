import biuoop.DrawSurface;
import biuoop.GUI;
import biuoop.Sleeper;

/**
 * Runs a simple animation with one moving ball.
 */
public class BouncingBallAnimation {

    /**
     * Main get four values from the command line and parse them.
     *
     * @param args command-line arguments.
     */
    public static void main(String[] args) {
        if (args == null || args.length != 4) {
            System.out.println("you need to write 4 integers: <x> <y> <dx> <dy>");
            return;
        }
        int x = (int) Double.parseDouble(args[0]);
        int y = (int) Double.parseDouble(args[1]);
        double dx = Double.parseDouble(args[2]);
        double dy = Double.parseDouble(args[3]);

        if (x < 0 || y < 0) {
            System.out.println("x and y must be bigger or eqaul to 0");
            return;
        }

        drawAnimation(new Point(x, y), dx, dy);
    }

    /**
     * Draws and updates the ball forever in a loop.
     *
     * @param start starting center point of the ball.
     * @param dx    velocity value on x-axis.
     * @param dy    velocity value on y-axis.
     */
    private static void drawAnimation(Point start, double dx, double dy) {
        GUI gui = new GUI("Bouncing Ball", GameConstants.WINDOW_WIDTH, GameConstants.WINDOW_HEIGHT);
        Sleeper sleeper = new Sleeper();
        Ball ball = new Ball(start, GameConstants.DEFAULT_BALL_RADIUS, java.awt.Color.BLACK);
        ball.setVelocity(dx, dy);
        // my obstacles
        Rectangle bounds = new Rectangle(0, 0, GameConstants.WINDOW_WIDTH, GameConstants.WINDOW_HEIGHT);
        ball.addObstacles(bounds.getLines());

        while (true) {
            ball.moveOneStep();
            DrawSurface surface = gui.getDrawSurface();
            ball.drawOn(surface);
            gui.show(surface);
            sleeper.sleepFor(GameConstants.FRAME_DELAY_MS);
        }
    }

}
