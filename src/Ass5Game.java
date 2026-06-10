import game.Game;

/**
 * Entry point for assignment 5 game.
 */
public class Ass5Game {
    /**
     * Start the game loop.
     *
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        Game game = new Game();
        game.initialize();
        game.run();
    }
}
