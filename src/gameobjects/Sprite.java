package gameobjects;

import biuoop.DrawSurface;

/**
 * A sprite that can draw and update.
 */
public interface Sprite {
    /**
     * Draw the sprite to the surface.
     *
     * @param d draw surface
     */
    void drawOn(DrawSurface d);

    /**
     * Notify the sprite time passed.
     */
    void timePassed();
}
