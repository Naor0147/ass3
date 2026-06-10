package gameobjects;

import game.Game;
import game.Counter;

/**
 * A BallRemover is in charge of removing balls from the game,
 * as well as keeping count of the number of balls that remain.
 */
public class BallRemover implements HitListener {
    private Game game;
    private Counter remainingBalls;

    /**
     * Construct a BallRemover.
     *
     * @param game          the game to remove balls from
     * @param remainingBalls counter tracking available balls
     */
    public BallRemover(Game game, Counter remainingBalls) {
        this.game = game;
        this.remainingBalls = remainingBalls;
    }

    /**
     * Remove the hitting ball from the game.
     *
     * @param beingHit the block that was hit
     * @param hitter   the ball that hit the block
     */
    public void hitEvent(Block beingHit, Ball hitter) {
        hitter.removeFromGame(game);
        remainingBalls.decrease(1);
    }
}
