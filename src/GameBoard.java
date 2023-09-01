import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


/**
 * This class instantiates a Minesweeper object, which is the model for the game.
 * As the user clicks the game board, the model is updated. Whenever the model
 * is updated, the game board repaints itself and updates its status JLabel to
 * reflect the current state of the model.
 *
 * 
 * In this game's Model-View-Controller framework, GameBoard stores the model as a field
 * and acts as both the controller (with a MouseListener) and the view (with
 * its paintComponent method and the status JLabel).
 */
@SuppressWarnings("serial")
public class GameBoard extends JPanel {

    private Minesweeper m; // model for the game
    private JLabel status; // current status text
    // displays how many tiles left to be uncovered before win condition is met
    private JLabel remainingTiles;
    // variable to freeze the screen when a game over condition is reached
    private boolean gameOverStatus;

    // Game constants
    public static final int BOARD_WIDTH = 400;
    public static final int BOARD_HEIGHT = 400;

    /**
     * Initializes the game board.
     */
    public GameBoard(JLabel statusInit, JLabel remainingTilesInit) {
        // creates border around the court area, JComponent method
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // Enable keyboard focus on the court area. When this component has the
        // keyboard focus, key events are handled by its key listener.
        setFocusable(true);

        m = new Minesweeper(10,10, 13); // initializes model for the game
        status = statusInit; // initializes the status JLabel
        remainingTiles = remainingTilesInit;
        gameOverStatus = false;

        /*
         * Listens for mouseclicks. Updates the model, then updates the game
         * board based off of the updated model.
         */
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                Point p = e.getPoint();

                // updates the model given the coordinates of the mouseclick
                if (!m.getBoard()[p.x / 40][p.y / 40].getCoverageStatus()) {
                    m.playTurn(p.x / 40, p.y / 40);
                }
                if (!gameOverStatus) {
                    updateStatus(); // updates the status JLabel
                    updateTilesRemaining(); // updates the tilesRemaining JLabel
                    repaint(); // repaints the game board
                }
                if (m.gameOverStatus()) {
                    gameOverStatus = true;
                }
            }
        });
    }

    /**
     * GameBoard constructor for resuming a paused game
     */
    public GameBoard(JLabel statusInit, JLabel remainingTilesInit, Minesweeper mGame) {
        // creates border around the court area, JComponent method
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // Enable keyboard focus on the court area. When this component has the
        // keyboard focus, key events are handled by its key listener.
        setFocusable(true);

        m = mGame; // loads in previously saved game
        status = statusInit; // initializes the status JLabel
        remainingTiles = remainingTilesInit;
        gameOverStatus = false;

        /*
         * Listens for mouseclicks. Updates the model, then updates the game
         * board based off of the updated model.
         */
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                Point p = e.getPoint();

                // updates the model given the coordinates of the mouseclick
                if (!m.getBoard()[p.x / 40][p.y / 40].getCoverageStatus()) {
                    m.playTurn(p.x / 40, p.y / 40);
                }
                if (!gameOverStatus) {
                    updateStatus(); // updates the status JLabel
                    updateTilesRemaining(); // updates the tilesRemaining JLabel
                    repaint(); // repaints the game board
                }
                if (m.gameOverStatus()) {
                    gameOverStatus = true;
                }
            }
        });
    }

    /**
     * (Re-)sets the game to its initial state.
     */
    public void reset() {
        m.reset(10, 10, 13);
        status.setText("Game Underway");
        remainingTiles.setText("[Tiles Remaining]: " + m.getTilesRemaining());
        gameOverStatus = false;
        repaint();

        // Makes sure this component has keyboard/mouse focus
        requestFocusInWindow();
    }

    /**
     * Updates the JLabel to reflect the current state of the game.
     */
    private void updateStatus() {
        if (m.gameOverStatus() && m.gameWonStatus()) {
            status.setText("Game Over: You Win");
        }

        if (m.gameOverStatus() && m.gameLossStatus()) {
            status.setText("Game Over: You Lose");
        }
    }

    /**
     * Updates the tilesRemaining JLabel
     */
    private void updateTilesRemaining() {
        remainingTiles.setText("[Tiles Remaining]: " + m.getTilesRemaining());
    }

    /**
     * Draws the game board.
     * 
     * There are many ways to draw a game board. This approach
     * will not be sufficient for most games, because it is not
     * modular. All of the logic for drawing the game board is
     * in this method, and it does not take advantage of helper
     * methods. Consider breaking up your paintComponent logic
     * into multiple methods or classes, like Mushroom of Doom.
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draws board grid
        for (int i = 1; i < 10; i++) {
            //g.setColor(Color.BLACK);
            g.drawLine(i * 40, 0, i * 40, 400);
            g.drawLine(0, i * 40, 400, i * 40);
        }

        // Draws each tile
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                // if not uncovered, draw a blank gray square
                if (!m.getBoard()[i][j].getCoverageStatus()) {
                    g.setColor(Color.GRAY);
                    g.fillRect((40 * i) + 1,(40 * j) + 1,38, 38);
                } else {
                    // if uncovered, draw a bomb, surrBombCount, or nothing at all; also square
                    // background is white (default color)
                    if (m.getBoard()[i][j].getBombStatus()) {
                        // black circles represent bombs
                        g.setColor(Color.BLACK);
                        g.fillOval((40 * i) + 15, (40 * j) + 15,15, 15);
                    } else {
                        if (m.getBoard()[i][j].getSurrBombCount() != 0) {
                            g.setColor(Color.BLACK);
                            g.drawString(
                                    "" + m.getBoard()[i][j].getSurrBombCount(),
                                    (40 * i) + 15, (40 * j) + 25);
                        }
                        // do nothing if there are no surrounding bombs
                    }
                }
            }
        }
    }

    /**
     * Returns the size of the game board.
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(BOARD_WIDTH, BOARD_HEIGHT);
    }

    /**
     * getter method for the minesweeper game within this board
     */
    public Minesweeper getMinesweeperGame() {
        return this.m;
    }
}
