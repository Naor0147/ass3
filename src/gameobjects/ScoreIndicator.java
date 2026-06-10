package gameobjects;

import biuoop.DrawSurface;
import game.Counter;

/**
 * A sprite that displays the current score on screen.
 */
public class ScoreIndicator implements Sprite {
    private Counter score;

    /**
     * Construct a ScoreIndicator.
     *
     * @param score the score counter to display
     */
    public ScoreIndicator(Counter score) {
        this.score = score;
    }

    /**
     * Draw the score on the given surface.
     *
     * @param d the draw surface
     */
    public void drawOn(DrawSurface d) {
        d.setColor(java.awt.Color.WHITE);
        String text = "Score: " + this.score.getValue();
        d.drawText(400 - text.length() * 4, 15, text, 14);
    }

    /**
     * Notify the sprite that time has passed.
     */
    public void timePassed() {
    }
}
