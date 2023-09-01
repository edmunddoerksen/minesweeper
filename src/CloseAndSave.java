import java.io.*;
import java.nio.file.Paths;

/**
 * Library for transferring game data to and from Minesweeper
 */
public class CloseAndSave {

    static Minesweeper loadGame() {
        // variables to input to the constructor
        Tile[] boardArr = new Tile[100];
        boolean isFirstMoveEntry = true;
        int tilesRemainingEntry = 0;
        int bombCountEntry = 0;
        boolean isGameWonEntry = true;
        boolean isGameLostEntry = true;

        Reader reader = null;
        try {
            reader = new FileReader("files/save_file.csv");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BufferedReader buff = new BufferedReader(reader);
        String csvLine = "";
        Tile addition;
        for (int i = 0; i < 100; i++) {
            try {
                csvLine = buff.readLine();
                String[] inputPrep = csvLine.split(",");
                boolean coverage = Boolean.parseBoolean(inputPrep[0]);
                boolean bomb = Boolean.parseBoolean(inputPrep[1]);
                int surrBomb = Integer.parseInt(inputPrep[2]);
                addition = new Tile(coverage, bomb, surrBomb);
                boardArr[i] = addition;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            isFirstMoveEntry = Boolean.parseBoolean(buff.readLine());
            tilesRemainingEntry = Integer.parseInt(buff.readLine());
            bombCountEntry = Integer.parseInt(buff.readLine());
            isGameWonEntry = Boolean.parseBoolean(buff.readLine());
            isGameLostEntry = Boolean.parseBoolean(buff.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }

        // convert Tile 1-D array to 2-D
        Tile[][] boardArray = new Tile[10][10];
        int entryCounter = 0;
        for (int i = 0; i < boardArray.length; i++) {
            for (int j = 0; j < boardArray[i].length; j++) {
                boardArray[j][i] = boardArr[entryCounter];
                entryCounter++;
            }
        }
        try {
            buff.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Minesweeper(
                boardArray, isFirstMoveEntry, tilesRemainingEntry, bombCountEntry, isGameWonEntry,
                isGameLostEntry);
    }

    static void saveGame(Minesweeper m) {
        File file = Paths.get("files/save_file.csv").toFile();
        FileWriter f = null;
        try {
            f = new FileWriter(file);
        } catch (IOException e) {
            System.out.println("test");
            e.printStackTrace();
        }
        BufferedWriter buff = new BufferedWriter(f);
        Tile[][] tileArr = m.getBoard();
        for (int i = 0; i < tileArr.length; i++) {
            for (int j = 0; j < tileArr[i].length; j++) {
                try {
                    buff.write("" + tileArr[j][i].getCoverageStatus() + ",");
                    buff.write("" + tileArr[j][i].getBombStatus() + ",");
                    buff.write("" + tileArr[j][i].getSurrBombCount());
                    //System.out.println(tileArr[i][j].getSurrBombCount());
                    buff.newLine();
                } catch (IOException e) {
                    System.out.println("Test");
                    e.printStackTrace();
                }

            }
        }
        try {
            buff.write("" + m.getIsFirstMove());
            buff.newLine();
            buff.write("" + m.getTilesRemaining());
            buff.newLine();
            buff.write("" + m.getBombCount());
            buff.newLine();
            buff.write("" + m.getIsGameWon());
            buff.newLine();
            buff.write("" + m.getIsGameLost());
        } catch (IOException e) {
            e.printStackTrace();
        }

        // close the writer
        try {
            buff.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
