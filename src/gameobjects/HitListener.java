package gameobjects;

/**
 * Interface for objects that listen to hit events.
 */
public interface HitListener {
   /**
    * This method is called whenever the beingHit object is hit.
    *
    * @param beingHit the block that was hit
    * @param hitter   the ball that's doing the hitting
    */
   void hitEvent(Block beingHit, Ball hitter);
}
