import biuoop.GUI;
import biuoop.DrawSurface;

/**
 * a test class for the Ball class.
 */
public class BallsTest1 {
   /**
    * The main method to run the test.
    *
    * @param args command line arguments
    */
   public static void main(String[] args) {
      GUI gui = new GUI("Balls Test 1", 800, 600);
      DrawSurface d = gui.getDrawSurface();

      Ball b1 = new Ball(new Point(0.0, 0.0), 30, java.awt.Color.RED);
      Ball b2 = new Ball(new Point(100.0, 150.0), 10, java.awt.Color.BLUE);
      Ball b3 = new Ball(new Point(80.0, 249.0), 50, java.awt.Color.GREEN);

      b1.drawOn(d);
      b2.drawOn(d);
      b3.drawOn(d);

      gui.show(d);
   }
}