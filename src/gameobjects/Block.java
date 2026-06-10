package gameobjects;

import biuoop.DrawSurface;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import geometry.Point;
import geometry.Rectangle;
import geometry.Velocity;
import collision.Collidable;
import game.Game;
import game.GameConstants;

/**
 * Basic blok you can hit.
 */
public class Block implements Collidable, Sprite, HitNotifier {
    private Rectangle rectangle;
    private List<HitListener> hitListeners = new ArrayList<HitListener>();
    /**
     * Build a block with a rect.
     *
     * @param rectangle the block shape
     */
    public Block(Rectangle rectangle) {
        this.rectangle = rectangle;
    }

    @Override
    public Rectangle getCollisionRectangle() {
        return this.rectangle;
    }

    @Override
    public Velocity hit(Ball hitter,Point collisionPoint, Velocity currentVelocity) {
        double dx = currentVelocity.getDx();
        double dy = currentVelocity.getDy();

        double x = collisionPoint.getX();
        double y = collisionPoint.getY();

        double leftX = this.rectangle.getUpperLeft().getX();
        double rightX = this.rectangle.getBottomRight().getX();
        double topY = this.rectangle.getUpperLeft().getY();
        double bottomY = this.rectangle.getBottomRight().getY();
        if (Math.abs(x - leftX) < GameConstants.EPSILON || Math.abs(x - rightX) < GameConstants.EPSILON) {
            dx = -dx;
        }

        if (Math.abs(y - topY) < GameConstants.EPSILON || Math.abs(y - bottomY) < GameConstants.EPSILON) {
            dy = -dy;
        }

        notifyHit(hitter);
        return new Velocity(dx, dy);
    }

    @Override
    public void drawOn(DrawSurface d) {

        d.setColor(rectangle.getColor());
        d.fillRectangle((int) this.rectangle.getUpperLeft().getX(),
                (int) this.rectangle.getUpperLeft().getY(),
                (int) this.rectangle.getWidth(), (int) this.rectangle.getHeight());
        d.setColor(Color.BLACK);
        d.drawRectangle((int) this.rectangle.getUpperLeft().getX(),
                (int) this.rectangle.getUpperLeft().getY(),
                (int) this.rectangle.getWidth(), (int) this.rectangle.getHeight());

    }

    @Override
    public void timePassed() {
    }

    /**
     * Add this block to the game.
     *
     * @param g game to add to
     */
    public void addToGame(game.Game g) {
        g.addCollidable(this);
        g.addSprite(this);

    }

    public boolean ballColorMatch(Ball ball) {
        return this.rectangle.getColor().equals(ball.getColor());
    }

    public void removeFromGame(Game game) {
        game.removeCollidable(this);
        game.removeSprite(this);
    }

    private void notifyHit(Ball hitter) {
        // Make a copy of the hitListeners before iterating over them.
        List<HitListener> listeners = new ArrayList<HitListener>(this.hitListeners);
        // Notify all listeners about a hit event:
        for (HitListener hl : listeners) {
            hl.hitEvent(this, hitter);
        }
    }

    @Override
    public void addHitListener(HitListener hl) {
        this.hitListeners.add(hl);
    }

    @Override
    public void removeHitListener(HitListener hl) {
        this.hitListeners.remove(hl);
    }
}
