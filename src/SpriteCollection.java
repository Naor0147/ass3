import java.util.List;
import biuoop.DrawSurface;

/**
 * Collection of sprites for the game.
 */
public class SpriteCollection {
    private final List<Sprite> sprites;

    /**
     * Make a new sprite list.
     */
    public SpriteCollection() {
        this.sprites = new java.util.ArrayList<>();
    }

    /**
     * Access the sprites list.
     *
     * @return list of sprites
     */
    public List<Sprite> getSprites() {
        return this.sprites;
    }

    /**
     * Add a sprite.
     *
     * @param s the sprite to add
     */
    public void addSprite(Sprite s) {
        this.sprites.add(s);
    }

    /**
     * Call timePassed on all sprites.
     */
    public void notifyAllTimePassed() {
        for (Sprite sprite : this.sprites) {
            sprite.timePassed();
        }
    }

    /**
     * Draw all sprites to the surface.
     *
     * @param d surface to draw on
     */
    public void drawAllOn(DrawSurface d) {
        for (Sprite sprite : this.sprites) {
            sprite.drawOn(d);
        }
    }
}
