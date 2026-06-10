package gameobjects;

/**
 * A simple hit listener that prints a message when a block is hit.
 */
public class PrintingHitListener implements HitListener {
   /**
    * Print a message when a block is hit.
    *
    * @param beingHit the block that was hit
    * @param hitter   the ball that hit the block
    */
   public void hitEvent(Block beingHit, Ball hitter) {
      System.out.println("A Block was hit.");
   }
}
