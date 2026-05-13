import biuoop.DrawSurface;
import biuoop.KeyboardSensor;
import java.awt.Color;

/**
 * The Paddle is the player-controlled block in the game.
 * It moves left and right and has special collision logic.
 */
public class Paddle implements Sprite, Collidable {
    private static final int ANGLE_LEFTMOST = 300;
    private static final int ANGLE_MID_LEFT = 330;
    private static final int ANGLE_MID_RIGHT = 30;
    private static final int ANGLE_RIGHTMOST = 60;

    private KeyboardSensor keyboard;
    private Rectangle rect;
    private Color color;
    private int speed;

    /**
     * Constructor for the Paddle.
     *
     * @param rect     the shape of the paddle.
     * @param keyboard the keyboard sensor.
     * @param color    the color of the paddle.
     * @param speed    the movement speed of the paddle.
     */
    public Paddle(Rectangle rect, KeyboardSensor keyboard, Color color, int speed) {
        this.rect = rect;
        this.keyboard = keyboard;
        this.color = color;
        this.speed = speed;
        this.rect.setColor(color);
    }

    /**
     * Moves the paddle to the left, wrapping around the screen edges.
     * The paddle stays within the wall boundaries.
     */
    public void moveLeft() {
        double newX = this.rect.getUpperLeft().getX() - this.speed;
        double width = this.rect.getWidth();
        // wrap to the right side (but inside the right wall)
        if (newX < GameConstants.WALL_THICKNESS) {
            newX = GameConstants.WINDOW_WIDTH - GameConstants.WALL_THICKNESS - width;
        }
        this.rect = new Rectangle(new Point(newX, this.rect.getUpperLeft().getY()),
                (int) width, (int) this.rect.getHeight());
        this.rect.setColor(this.color);
    }

    /**
     * Moves the paddle to the right, wrapping around the screen edges.
     * The paddle stays within the wall boundaries.
     */
    public void moveRight() {
        double newX = this.rect.getUpperLeft().getX() + this.speed;
        double width = this.rect.getWidth();
        // wrap to the left side (but inside the left wall)
        if (newX + width > GameConstants.WINDOW_WIDTH - GameConstants.WALL_THICKNESS) {
            newX = GameConstants.WALL_THICKNESS;
        }
        this.rect = new Rectangle(new Point(newX, this.rect.getUpperLeft().getY()),
                (int) width, (int) this.rect.getHeight());
        this.rect.setColor(this.color);
    }

    // --- Sprite Implementation ---

    @Override
    public void timePassed() {
        if (keyboard.isPressed(KeyboardSensor.LEFT_KEY)) {
            moveLeft();
        }
        if (keyboard.isPressed(KeyboardSensor.RIGHT_KEY)) {
            moveRight();
        }
    }

    @Override
    public void drawOn(DrawSurface d) {
        d.setColor(this.color);
        this.rect.drawOn(d);
        d.setColor(Color.BLACK);
        d.drawRectangle((int) rect.getUpperLeft().getX(), (int) rect.getUpperLeft().getY(),
                (int) rect.getWidth(), (int) rect.getHeight());
    }

    // --- Collidable Implementation ---

    @Override
    public Rectangle getCollisionRectangle() {
        return this.rect;
    }

    @Override
    public Velocity hit(Point collisionPoint, Velocity currentVelocity) {
        double paddleWidth = this.rect.getWidth();
        double startX = this.rect.getUpperLeft().getX();
        double hitX = collisionPoint.getX();

        double dx = currentVelocity.getDx();
        double dy = currentVelocity.getDy();
        double speedMagnitude = Math.sqrt(dx * dx + dy * dy);

        // the paddle is split into 5 equal zones
        double regionWidth = paddleWidth / 5;

        if (hitX < startX + regionWidth) {
            return Velocity.fromAngleAndSpeed(ANGLE_LEFTMOST, speedMagnitude);
        } else if (hitX < startX + 2 * regionWidth) {
            return Velocity.fromAngleAndSpeed(ANGLE_MID_LEFT, speedMagnitude);
        } else if (hitX < startX + 3 * regionWidth) {
            // middle region - just flip vertical
            return new Velocity(dx, -dy);
        } else if (hitX < startX + 4 * regionWidth) {
            return Velocity.fromAngleAndSpeed(ANGLE_MID_RIGHT, speedMagnitude);
        } else {
            return Velocity.fromAngleAndSpeed(ANGLE_RIGHTMOST, speedMagnitude);
        }
    }

    /**
     * Adds this paddle to the game by registering it as a Sprite and a Collidable.
     *
     * @param g the game object.
     */
    public void addToGame(Game g) {
        g.addSprite(this);
        g.addCollidable(this);
    }
}