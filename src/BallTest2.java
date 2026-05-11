import biuoop.GUI;
import biuoop.DrawSurface;
import biuoop.Sleeper;
import java.awt.Color;
import java.util.Random;

/**
 * A test class for the game collision checking corner (vertex) hits on a
 * rectangle, now with 100 balls.
 */
public class BallTest2 {
    /**
     * Runs the collision test simulation.
     *
     * @param args command-line arguments.
     */
    public static void main(String[] args) {
        GUI gui = new GUI("Collision Engine Test - 100 Balls", 800, 600);
        Sleeper sleeper = new Sleeper();

        // 4 boundary walls + 4 rectangle lines = 8 lines
        Line[] lines = new Line[8];

        // Boundaries
        lines[0] = new Line(50, 50, 750, 50); // Top
        lines[1] = new Line(750, 50, 750, 550); // Right
        lines[2] = new Line(750, 550, 50, 550); // Bottom
        lines[3] = new Line(50, 550, 50, 50); // Left

        // Inner Rectangle (spanning from x=300 to 500, y=200 to 400)
        lines[4] = new Line(300, 200, 500, 200); // Top edge
        lines[5] = new Line(500, 200, 500, 400); // Right edge
        lines[6] = new Line(500, 400, 300, 400); // Bottom edge
        lines[7] = new Line(300, 400, 300, 200); // Left edge

        // Create 100 balls with random properties
        Ball[] balls = new Ball[100];
        Random rand = new Random();

        for (int i = 0; i < balls.length; i++) {
            // Pick a safe starting spot (top-left region to avoid starting inside the inner
            // box)
            int startX = rand.nextInt(200) + 60; // 60 to 259
            int startY = rand.nextInt(100) + 60; // 60 to 159
            int radius = rand.nextInt(6) + 5; // radius 5 to 10

            // Random color
            Color randColor = new Color(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));

            balls[i] = new Ball(new Point(startX, startY), radius, randColor);

            // Random velocity between -5 and 5, avoiding 0,0
            int dx = rand.nextInt(11) - 5;
            int dy = rand.nextInt(11) - 5;
            if (dx == 0 && dy == 0) {
                dx = 3;
            }
            balls[i].setVelocity(dx, dy);
        }

        // Animation loop
        while (true) {
            DrawSurface d = gui.getDrawSurface();

            d.setColor(Color.WHITE);
            d.fillRectangle(0, 0, 800, 600);

            // Draw boundaries and rectangle
            d.setColor(Color.BLUE);
            for (Line line : lines) {
                int x1 = (int) line.start().getX();
                int y1 = (int) line.start().getY();
                int x2 = (int) line.end().getX();
                int y2 = (int) line.end().getY();
                d.drawLine(x1, y1, x2, y2);
            }

            // Draw and resolve collisions for all 100 balls
            for (Ball ball : balls) {
                ball.drawOn(d);
                CollisionEngine.resolveBallLineCollision(ball, lines);
            }

            gui.show(d);
            sleeper.sleepFor(20);
        }
    }
}