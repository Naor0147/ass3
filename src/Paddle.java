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
     */
    public void moveLeft() {
        double newX = this.rect.getUpperLeft().getX() - this.speed;
        double width = this.rect.getWidth();
        if (newX < 0) {
            newX = GameConstants.WINDOW_WIDTH - width;
        }
        this.rect = new Rectangle(new Point(newX, this.rect.getUpperLeft().getY()),
                (int) width, (int) this.rect.getHeight());
        this.rect.setColor(this.color);
    }

    /**
     * Moves the paddle to the right, wrapping around the screen edges.
     */
    public void moveRight() {
        double newX = this.rect.getUpperLeft().getX() + this.speed;
        double width = this.rect.getWidth();
        if (newX + width > GameConstants.WINDOW_WIDTH) {
            newX = 0;
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
        // Add a black outline for better visibility
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

        // Calculate the speed (scalar) of the current velocity
        double dx = currentVelocity.getDx();
        double dy = currentVelocity.getDy();
        double speedMagnitude = Math.sqrt(dx * dx + dy * dy);

        // Divide the paddle into 5 equal regions
        double regionWidth = paddleWidth / 5;

        // Region 1: Leftmost (300 degrees)
        if (hitX < startX + regionWidth) {
            return Velocity.fromAngleAndSpeed(ANGLE_LEFTMOST, speedMagnitude);
        }
        // Region 2: Mid-left (330 degrees)
        else if (hitX < startX + 2 * regionWidth) {
            return Velocity.fromAngleAndSpeed(ANGLE_MID_LEFT, speedMagnitude);
        }
        // Region 3: Center (Standard bounce - reverse $dy$)
        else if (hitX < startX + 3 * regionWidth) {
            return new Velocity(dx, -dy);
        }
        // Region 4: Mid-right (30 degrees)
        else if (hitX < startX + 4 * regionWidth) {
            return Velocity.fromAngleAndSpeed(ANGLE_MID_RIGHT, speedMagnitude);
        }
        // Region 5: Rightmost (60 degrees)
        else {
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