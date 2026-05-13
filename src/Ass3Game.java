/**
 * Entrey point for ass3 game.
 */
public class Ass3Game {
    /**
     * Start the game loop.
     *
     * @param args cmd argumets (not used)
     */
    public static void main(String[] args) {
        Game game = new Game();
        game.initialize();
        game.run();
    }
}
