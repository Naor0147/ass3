import biuoop.DrawSurface;
import java.awt.Color;

public class Block implements Collidable, Sprite {
    private Rectangle rectangle;

    public Block(Rectangle rectangle) {
        this.rectangle = rectangle;
    }

    @Override
    public Rectangle getCollisionRectangle() {
        return this.rectangle;
    }

    @Override
    public Velocity hit(Point collisionPoint, Velocity currentVelocity) {
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

    public void addToGame(Game g) {
        g.addCollidable(this);
        g.addSprite(this);
    }
}
