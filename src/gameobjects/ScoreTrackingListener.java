package gameobjects;

import game.Counter;

/**
 * A hit listener that updates the score when blocks are hit.
 */
public class ScoreTrackingListener implements HitListener {
    private Counter currentScore;

    /**
     * Construct a ScoreTrackingListener.
     *
     * @param scoreCounter the score counter to update
     */
    public ScoreTrackingListener(Counter scoreCounter) {
        this.currentScore = scoreCounter;
    }

    /**
     * Increase the score when a block is hit and colors differ.
     *
     * @param beingHit the block that was hit
     * @param hitter   the ball that hit the block
     */
    public void hitEvent(Block beingHit, Ball hitter) {
        if (beingHit.ballColorMatch(hitter)) {
            return;
        }
        this.currentScore.increase(5);
    }
}
