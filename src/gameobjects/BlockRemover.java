package gameobjects;

import game.Game;
import game.Counter;
// a BlockRemover is in charge of removing blocks from the game, as well as keeping count
// of the number of blocks that remain.
public class BlockRemover implements HitListener {
   private Game game;
   private Counter remainingBlocks;

   public BlockRemover(Game game, Counter remainingBlocks) { 
      this.game = game;
      this.remainingBlocks = remainingBlocks;
   }

   // Blocks that are hit should be removed
   // from the game. Remember to remove this listener from the block
   // that is being removed from the game.
   public void hitEvent(Block beingHit, Ball hitter) {
      beingHit.removeHitListener(this); // stop listening
      beingHit.removeFromGame(game); // remove block
      remainingBlocks.decrease(1); // decrement counter
   }



}