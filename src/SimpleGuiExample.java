import biuoop.GUI;
import biuoop.DrawSurface;

import java.util.Random;
import java.awt.Color;

/**
 * Small demo for drawing with the biuoop GUI.
 */
public class SimpleGuiExample {

  /**
   * Draws random circles on a GUI surface.
   */
  public void drawRandomCircles() {
    Random rand = new Random();
    GUI gui = new GUI("Random Circles Example", 400, 300);
    DrawSurface d = gui.getDrawSurface();
    for (int i = 0; i < 10; ++i) {
      int x = rand.nextInt(400) + 1;
      int y = rand.nextInt(300) + 1;
      int r = 5 * (rand.nextInt(4) + 1);
      d.setColor(Color.RED);
      d.fillCircle(x, y, r);
    }
    gui.show(d);
  }

  /**
   * Program entry point.
   *
   * @param args command-line arguments.
   */
  public static void main(String[] args) {
    SimpleGuiExample example = new SimpleGuiExample();
    example.drawRandomCircles();
  }
}