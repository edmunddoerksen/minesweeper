// Class that represents a single square in the minesweeper gameboard
public class Tile {

    // instance variables
    private boolean isUncovered;
    private boolean hasBomb;
    private int surrBombCount; // count of bombs that surround the tile

    public Tile(boolean bombStatus) {
        this.isUncovered = false;
        this.hasBomb = bombStatus;
        this.surrBombCount = 0;
    }

    // Tile constructor with custom coverage status and surrounding bomb counts
    public Tile(boolean coverageS, boolean bombStatus, int sBC) {
        this.isUncovered = coverageS;
        this.hasBomb = bombStatus;
        this.surrBombCount = sBC;
    }

    // getter methods for instance variables
    public boolean getCoverageStatus() {
        return this.isUncovered;
    }

    public boolean getBombStatus() {
        return this.hasBomb;
    }

    public int getSurrBombCount() {
        return this.surrBombCount;
    }

    // setter methods (flip methods)
    public void setCoverageStatus(boolean cStatus) {
        this.isUncovered = cStatus;
    }

    public void setBombStatus(boolean bS) {
        this.hasBomb = bS;
    }

    public void setSurrBombCount(int input) {
        this.surrBombCount = input;
    }
}
