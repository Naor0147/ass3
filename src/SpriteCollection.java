import java.util.List;
import biuoop.DrawSurface;
public class SpriteCollection {
    List<Sprite> sprites;
    public SpriteCollection() {
        this.sprites = new java.util.ArrayList<>();
    }

    public void addSprite(Sprite s) {
        this.sprites.add(s);
    }
    public void notifyAllTimePassed() {
        for (Sprite sprite : this.sprites) {
            sprite.timePassed();
        }
    }
    public void drawAllOn(DrawSurface d) {
        for (Sprite sprite : this.sprites) {
            sprite.drawOn(d);
        }
    }

        
}
