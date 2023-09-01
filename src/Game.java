import javax.swing.*;

public class Game {
    /**
     * Main method run to start and run the game.
     */
    public static void main(String[] args) {
        Runnable game = new RunMinesweeper(); // Set the game you want to run
                                                                     // here
        SwingUtilities.invokeLater(game);
    }
}
