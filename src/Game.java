import biuoop.DrawSurface;
import biuoop.GUI;
import biuoop.KeyboardSensor;
import biuoop.Sleeper;
import java.awt.Color;

/**
 * Main game runner and setup.
 */
public class Game {
    private static final String WINDOW_TITLE = "Arkanoid";

    private static final Color FRAME_COLOR = new Color(150, 150, 150);

    private static final int BLOCK_WIDTH = 50;
    private static final int BLOCK_HEIGHT = 20;
    private static final int BLOCK_ROWS = 6;
    private static final int MAX_BLOCKS_PER_ROW = 12;
    private static final int BLOCK_START_Y = 60;
    private static final Color[] ROW_COLORS = new Color[] {
            new Color(160, 160, 160),
            new Color(220, 40, 40),
            new Color(245, 200, 0),
            new Color(50, 90, 220),
            new Color(255, 170, 170),
            new Color(40, 200, 40)
    };

    private static final int PADDLE_WIDTH = 100;
    private static final int PADDLE_HEIGHT = 15;
    private static final int PADDLE_SPEED = 6;
    private static final int PADDLE_GAP_FROM_WALL = 0;
    private static final Color PADDLE_COLOR = new Color(251, 193, 3);

    private static final int BALL_RADIUS = 6;
    private static final double BALL_SPEED = 6.0;
    private static final int BALL1_OFFSET_Y = 40;
    private static final int BALL2_OFFSET_X = 30;
    private static final int BALL2_OFFSET_Y = 60;
    private static final Color BALL_COLOR = Color.WHITE;

    private static final int FRAMES_PER_SECOND = 60;
    private static final int MILLISECONDS_PER_SECOND = 1000;

    private static final Color BACKGROUND_COLOR = new Color(5, 4, 124);

    private GUI gui;
    private SpriteCollection sprites;
    private GameEnvironment environment;
    private Sleeper sleeper;

    /**
     * Make a new game.
     */
    public Game() {
        this.sprites = new SpriteCollection();
        this.environment = new GameEnvironment();
        this.sleeper = new Sleeper();
    }

    /**
     * Add a collidable.
     *
     * @param c the collidable to add
     */
    public void addCollidable(Collidable c) {
        this.environment.addCollidable(c);
    }

    /**
     * Add a sprite.
     *
     * @param s the sprite to add
     */
    public void addSprite(Sprite s) {
        this.sprites.addSprite(s);
    }

    // Initialize a new game: create the Blocks and Ball (and Paddle)
    // and add them to the game.
    /**
     * Set up the game objects.
     */
    public void initialize() {

        this.gui = new GUI(WINDOW_TITLE, GameConstants.WINDOW_WIDTH, GameConstants.WINDOW_HEIGHT);
        int paddleY = addPaddle();

        addWalls();
        addBlocks();
        addBalls(paddleY);
    }

    // Run the game -- start the animation loop.
    /**
     * Run the animation loop.
     */
    public void run() {
        int millisecondsPerFrame = MILLISECONDS_PER_SECOND / FRAMES_PER_SECOND;

        sleeper.sleepFor(200);
        while (true) {
            long startTime = System.currentTimeMillis(); // timing

            DrawSurface d = gui.getDrawSurface();
            drawBackground(d);
            this.sprites.drawAllOn(d);
            gui.show(d);
            this.sprites.notifyAllTimePassed();

            // timing
            long usedTime = System.currentTimeMillis() - startTime;
            long milliSecondLeftToSleep = millisecondsPerFrame - usedTime;
            if (milliSecondLeftToSleep > 0) {
                sleeper.sleepFor(milliSecondLeftToSleep);
            }
        }
    }

    private void addWalls() {
        int wt = GameConstants.WALL_THICKNESS;
        Block top = createBlock(0, 0, GameConstants.WINDOW_WIDTH, wt, FRAME_COLOR);
        Block left = createBlock(0, 0, wt, GameConstants.WINDOW_HEIGHT, FRAME_COLOR);
        Block right = createBlock(GameConstants.WINDOW_WIDTH - wt, 0,
                wt, GameConstants.WINDOW_HEIGHT, FRAME_COLOR);
        Block bottom = createBlock(0, GameConstants.WINDOW_HEIGHT - wt,
                GameConstants.WINDOW_WIDTH, wt, FRAME_COLOR);

        top.addToGame(this);
        left.addToGame(this);
        right.addToGame(this);
        bottom.addToGame(this);
    }

    private void addBlocks() {
        int wt = GameConstants.WALL_THICKNESS;
        int availableWidth = GameConstants.WINDOW_WIDTH - (2 * wt);
        int maxBlocks = Math.min(MAX_BLOCKS_PER_ROW, availableWidth / BLOCK_WIDTH);

        for (int row = 0; row < BLOCK_ROWS; row++) {
            int rowBlocks = maxBlocks - row;
            if (rowBlocks <= 0) {
                break;
            }
            Color rowColor = ROW_COLORS[row % ROW_COLORS.length];
            int y = BLOCK_START_Y + row * BLOCK_HEIGHT;
            int startX = GameConstants.WINDOW_WIDTH - wt - (rowBlocks * BLOCK_WIDTH);
            for (int col = 0; col < rowBlocks; col++) {
                int x = startX + col * BLOCK_WIDTH;
                Block block = createBlock(x, y, BLOCK_WIDTH, BLOCK_HEIGHT, rowColor);
                block.addToGame(this);
            }
        }
    }

    private int addPaddle() {
        int wt = GameConstants.WALL_THICKNESS;
        int paddleY = GameConstants.WINDOW_HEIGHT - wt - PADDLE_HEIGHT - PADDLE_GAP_FROM_WALL;
        int paddleX = (GameConstants.WINDOW_WIDTH - PADDLE_WIDTH) / 2;
        KeyboardSensor keyboard = this.gui.getKeyboardSensor();
        Rectangle paddleRect = new Rectangle(new Point(paddleX, paddleY), PADDLE_WIDTH, PADDLE_HEIGHT);
        Paddle paddle = new Paddle(paddleRect, keyboard, PADDLE_COLOR, PADDLE_SPEED);
        paddle.addToGame(this);
        Rectangle paddleRect1 = new Rectangle(new Point(paddleX - GameConstants.WINDOW_WIDTH, paddleY),
                PADDLE_WIDTH, PADDLE_HEIGHT);
        Paddle paddle1 = new Paddle(paddleRect1, keyboard, PADDLE_COLOR, PADDLE_SPEED);
        paddle1.addToGame(this);

        return paddleY;
    }

    private void addBalls(int paddleY) {
        double centerX = GameConstants.WINDOW_WIDTH / 2.0;
        addBall(new Point(centerX, paddleY - BALL1_OFFSET_Y), BALL_SPEED, -BALL_SPEED);
        addBall(new Point(centerX + BALL2_OFFSET_X, paddleY - BALL2_OFFSET_Y), -BALL_SPEED, -BALL_SPEED);
    }

    private void addBall(Point center, double dx, double dy) {
        Ball ball = new Ball(center, BALL_RADIUS, BALL_COLOR);
        ball.setVelocity(dx, dy);
        ball.init(this.environment);
        ball.addToGame(this);
    }

    private Block createBlock(int x, int y, int width, int height, Color color) {
        Rectangle rect = new Rectangle(new Point(x, y), width, height);
        rect.setColor(color);
        return new Block(rect);
    }

    private void drawBackground(DrawSurface d) {
        d.setColor(BACKGROUND_COLOR);
        d.fillRectangle(0, 0, GameConstants.WINDOW_WIDTH, GameConstants.WINDOW_HEIGHT);
    }

}