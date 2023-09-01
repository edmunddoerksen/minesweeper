import java.util.*;

public class Minesweeper {
    private Tile[][] board;
    private boolean isFirstMove; // tells us if the player has uncovered a tile
    // the number of unmined tiles the player still has to uncover to win the game
    private int tilesRemaining;
    private int bombCount; // the number of bombs in the game
    private boolean isGameWon; // boolean that tells us whether the user has won the game
    private boolean isGameLost; // boolean that tells us whether the user has lost the game

    /**
     * Constructor sets up game state.
     */
    public Minesweeper(int bH, int bW, int bombC) {
        reset(bH, bW, bombC);
    }

    /**
     * Constructor for testing purposes. Mines are placed at pre-determined locations
     */
    public Minesweeper(int bH, int bW, TreeSet<Integer> bombs) {
        // set instance variables
        this.board = new Tile[bH][bW];
        this.bombCount = bombs.size();
        this.tilesRemaining = (bH * bW) - bombCount;
        isGameLost = false;
        isGameWon = false;
        this.isFirstMove = true;

        // place the bombs in the game
        int generationIndex = 0;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (bombs.contains(generationIndex)) {
                    board[i][j] = new Tile(true);
                } else {
                    board[i][j] = new Tile(false);
                }
                generationIndex++;
            }
        }
    }

    /**
     * Construct a game of Minesweeper at any stage
     */
    public Minesweeper(
            Tile[][] boardInput, boolean isFirstMove, int tilesRemaining, int bombCount,
            boolean isGameWon, boolean isGameLost) {
        this.board = boardInput;
        this.isFirstMove = isFirstMove;
        this.tilesRemaining = tilesRemaining;
        this.bombCount = bombCount;
        this.isGameWon = isGameWon;
        this.isGameLost = isGameLost;
    }

    /**
     * playTurn allows players to play a turn. Returns true if the move is
     * successful and false if a player tries to play in a location that is
     * taken or after the game has ended. If the turn is successful and the game
     * has not ended, the player is changed. If the turn is unsuccessful or the
     * game has ended, the player is not changed.
     */
    public void playTurn(int r, int c) {
        // ensures that the first click will not result in a Game Over
        if (this.isFirstMove) {
            if (this.board[r][c].getBombStatus()) {

                int spacesAvailableCount = 0;
                for (int i = 0; i < this.board.length; i++) {
                    for (int j = 0; j < this.board[i].length; j++) {
                        if (!this.board[i][j].getBombStatus()) {
                            spacesAvailableCount++;
                        }
                    }
                }
                int bombLocationIndex = (int) (Math.random() * spacesAvailableCount);
                int indexLocator = 0;
                for (int i = 0; i < this.board.length; i++) {
                    for (int j = 0; j < this.board[i].length; j++) {
                        if (!this.board[i][j].getBombStatus() && indexLocator ==
                                bombLocationIndex) {
                            this.board[i][j].setBombStatus(true);
                            indexLocator++;
                        } else if (!this.board[i][j].getBombStatus()) {
                            indexLocator++;
                        }
                    }
                }
                this.board[r][c].setBombStatus(false);
            }
            this.isFirstMove = false;
        }

        // call the recursive uncover function
        uncoverTiles(r, c);
    }


    /**
     * recursive function for uncovering tiles (in the event of the uncovered tile not containing a
     * bomb). if the win condition (i.e. tilesRemaining == 0) is reached, ends the game immediately
     */
    public void uncoverTiles(int r, int c) {
        // uncover the clicked tile
        this.board[r][c].setCoverageStatus(true);

        // if uncovered tile contains a bomb, end the game with the loss condition
        if (this.board[r][c].getBombStatus()) {
            this.isGameLost = true;
        } else {
            // if no bomb, decrement tilesRemaining
            this.tilesRemaining--;
            // if all non-bomb tiles have been uncovered, end the game with the win condition
            if (this.tilesRemaining == 0) {
                this.isGameWon = true;
            } else {
                // otherwise, count the number of bombs in the surrounding squares:
                // if surrBombCount == 0, employ the recursive case
                if (countSurrBombs(r, c) == 0) {
                    for (int i = r - 1; i < r + 2; i++) {
                        for (int j = c - 1; j < c + 2; j++) {
                            if ((i >= 0 && i < this.board.length) &&
                                    (j >= 0 && j < board[0].length)) {
                                // prevents infinite loop by ensuring that tiles are not uncovered
                                // repeatedly
                                if (!this.board[i][j].getCoverageStatus()) {
                                    uncoverTiles(i, j);
                                }
                            }
                        }
                    }
                } else {
                    // update surrBombCount of the current tile for display purposes
                    this.board[r][c].setSurrBombCount(countSurrBombs(r,c));
                }
            }
        }
    }


    /**
        function that counts the number of bombs that surround a given uncovered square
     */
    public int countSurrBombs(int r, int c) {
        int surrBombCount = 0;
        for (int i = r - 1; i < r + 2; i++) {
            for (int j = c - 1; j < c + 2; j++) {
                if ((i >= 0 && i < this.board.length) && (j >= 0 && j < board[0].length)) {
                    if (board[i][j].getBombStatus()) {
                        surrBombCount++;
                    }
                }
            }
        }
        return surrBombCount;
    }

    /**
     * printGameState prints the current game state
     * for debugging.
     */
    public void printGameState() {
        System.out.println("\n\nTiles Remaining " + ":" + this.tilesRemaining + "\n");
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                System.out.print(board[i][j].getBombStatus());
                System.out.print(", ");
                System.out.print(board[i][j].getCoverageStatus());
                System.out.print(", ");
                System.out.print("" + board[i][j].getSurrBombCount());
                if (j < board[i].length - 1) {
                    System.out.print(" | ");
                }
            }
            if (i < board.length - 1) {
                System.out.println("\n---------");
            }
        }
    }

    /**
     * reset (re-)sets the game state to start a new game.
     */
    public void reset(int boardHeight, int boardWidth, int bombCount) {
        // set instance variables
        this.board = new Tile[boardHeight][boardWidth];
        this.bombCount = bombCount;
        this.tilesRemaining = (boardHeight * boardWidth) - bombCount;
        isGameLost = false;
        isGameWon = false;
        this.isFirstMove = true;

        // place the bombs randomly in the game
        List<Integer> bombIndices = new ArrayList<>();
        Set<Integer> placedBombIndices = new TreeSet<>();
        int generationIndex = 0;
        for (int i = 0; i < (board.length * board[0].length); i++) {
            bombIndices.add(i);
        }
        // ensures that the bomb locations will be randomized
        Collections.shuffle(bombIndices);

        for (int i = 0; i < this.bombCount; i++) {
            placedBombIndices.add(bombIndices.get(i));
        }

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (placedBombIndices.contains(generationIndex)) {
                    board[i][j] = new Tile(true);
                } else {
                    board[i][j] = new Tile(false);
                }
                generationIndex++;
            }
        }
    }

    /**
     *
     * function that tells us if the game is over
     */
    public boolean gameOverStatus() {
        return (this.isGameWon || this.isGameLost);
    }

    /**
     * getter function for game winning status
     */
    public boolean gameWonStatus() {
        return this.isGameWon;
    }

    /**
     * getter function for game loss status
     */
    public boolean gameLossStatus() {
        return this.isGameLost;
    }

    /**
     *
     * returns a deep copy of the gameboard in its current state
     */
    public Tile[][] getBoard() {
        Tile[][] output = new Tile[this.board.length][this.board[0].length];
        for (int i = 0; i < output.length; i++) {
            for (int j = 0; j < output[i].length; j++) {
                output[i][j] = new Tile(
                        this.board[i][j].getCoverageStatus(), this.board[i][j].getBombStatus(),
                        this.board[i][j].getSurrBombCount());
            }
        }
        return output;
    }

    /**
     * returns the number of tiles remaining to be uncovered
     */
    public int getTilesRemaining() {
        return this.tilesRemaining;
    }

    /**
     * getter for number of bombs in the board
     */
    public int getBombCount() {
        return this.bombCount;
    }

    /**
     * getter for isFirstMove boolean
     */
    public boolean getIsFirstMove() {
        return this.isFirstMove;
    }

    /**
     * getter for isGameWon boolean
     */
    public boolean getIsGameWon() {
        return this.isGameWon;
    }

    /**
     * getter for isGameLost boolean
     */
    public boolean getIsGameLost() {
        return this.isGameLost;
    }
}
