package gameobjects;

import game.Game;
import game.Counter;

/**
 * A BlockRemover is in charge of removing blocks from the game,
 * as well as keeping count of the number of blocks that remain.
 */
public class BlockRemover implements HitListener {
   private Game game;
   private Counter remainingBlocks;

   /**
    * Construct a BlockRemover.
    *
    * @param game           the game to remove blocks from
    * @param remainingBlocks counter tracking remaining blocks
    */
   public BlockRemover(Game game, Counter remainingBlocks) {
      this.game = game;
      this.remainingBlocks = remainingBlocks;
   }

   /**
    * Remove the hit block from the game if ball color does not match.
    *
    * @param beingHit the block that was hit
    * @param hitter   the ball that hit the block
    */
   public void hitEvent(Block beingHit, Ball hitter) {
      if (beingHit.ballColorMatch(hitter)) {
         return;
      }
      hitter.setColor(beingHit.getCollisionRectangle().getColor());
      beingHit.removeHitListener(this);
      beingHit.removeFromGame(game);
      remainingBlocks.decrease(1);
   }
}
