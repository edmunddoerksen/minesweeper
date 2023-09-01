// package org.cis120.minesweeper;

import org.junit.Test;
import java.util.TreeSet;

import static org.junit.Assert.*;


public class MinesweeperTest {

    @Test
    public void testFirstClickIsNotABombBecauseTheBombLocationIsChanged() {
        TreeSet<Integer> bL = new TreeSet<>();
        for (int i = 0; i < 24; i++) {
            bL.add(i);
        }
        Minesweeper m = new Minesweeper(5, 5, bL);
        m.playTurn(4,3);
        assertFalse(m.gameLossStatus());
        assertTrue(m.getBoard()[4][4].getBombStatus());
        m.playTurn(4,4);
        assertTrue(m.gameOverStatus());
        assertTrue(m.gameLossStatus());
    }

    @Test
    public void testUncoverTilesBaseCase() {
        TreeSet<Integer> bL = new TreeSet<>();
        bL.add(2);
        bL.add(6);
        bL.add(13);
        Minesweeper m = new Minesweeper(5, 5, bL);
        m.playTurn(1,2);
        assertTrue(m.getBoard()[1][2].getCoverageStatus());
        assertEquals(21, m.getTilesRemaining());
        int uncoveredTileCount = 0;
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (m.getBoard()[i][j].getCoverageStatus()) {
                    uncoveredTileCount++;
                }
            }
        }
        assertEquals(1, uncoveredTileCount);
        assertFalse(m.gameOverStatus());
    }

    @Test
    public void testUncoverTilesRecursiveCase() {
        TreeSet<Integer> bL = new TreeSet<>();
        bL.add(2);
        bL.add(6);
        bL.add(13);
        Minesweeper m = new Minesweeper(5, 5, bL);
        m.playTurn(3,1);
        assertTrue(m.getBoard()[3][1].getCoverageStatus());
        assertTrue(m.getBoard()[2][2].getCoverageStatus());
        assertEquals(0, m.getBoard()[3][1].getSurrBombCount());
        assertEquals(9, m.getTilesRemaining());
        int uncoveredTileCount = 0;
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (m.getBoard()[i][j].getCoverageStatus()) {
                    uncoveredTileCount++;
                }
            }
        }
        assertEquals(13, uncoveredTileCount);
        assertFalse(m.gameOverStatus());
    }

    @Test
    public void testNoBombsFirstClickInstantWin() {
        Minesweeper m = new Minesweeper(5, 5, 0);
        m.playTurn(3,1);
        assertTrue(m.getBoard()[3][1].getCoverageStatus());
        assertEquals(0, m.getTilesRemaining());
        int uncoveredTileCount = 0;
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (m.getBoard()[i][j].getCoverageStatus()) {
                    uncoveredTileCount++;
                }
            }
        }
        assertEquals(25, uncoveredTileCount);
        assertTrue(m.gameOverStatus());
        assertTrue(m.gameWonStatus());
    }

    @Test
    public void testClickingBombCausesLoss() {
        TreeSet<Integer> bL = new TreeSet<>();
        bL.add(2);
        bL.add(6);
        bL.add(13);
        Minesweeper m = new Minesweeper(5, 5, bL);
        m.playTurn(1, 2);
        m.playTurn(1, 1);
        assertTrue(m.gameOverStatus());
        assertTrue(m.gameLossStatus());
    }

    @Test
    public void testSurrBombCountMaxBombs() {
        TreeSet<Integer> bL = new TreeSet<>();
        bL.add(2);
        bL.add(3);
        bL.add(4);
        bL.add(7);
        bL.add(9);
        bL.add(12);
        bL.add(13);
        bL.add(14);
        Minesweeper m = new Minesweeper(5, 5, bL);
        m.playTurn(1, 3);
        assertEquals(8, m.getBoard()[1][3].getSurrBombCount());
        assertFalse(m.gameOverStatus());
    }

    @Test
    public void testSurrBombCountFourCorners() {
        TreeSet<Integer> bL = new TreeSet<>();
        bL.add(1);
        bL.add(2);
        bL.add(6);
        bL.add(13);
        bL.add(18);
        bL.add(19);
        bL.add(21);
        bL.add(23);
        Minesweeper m = new Minesweeper(5, 5, bL);
        m.playTurn(0, 0);
        m.playTurn(0, 4);
        m.playTurn(4,0);
        m.playTurn(4,4);
        assertEquals(2, m.getBoard()[0][0].getSurrBombCount());
        assertEquals(0, m.getBoard()[0][4].getSurrBombCount());
        assertEquals(1, m.getBoard()[4][0].getSurrBombCount());
        assertEquals(3, m.getBoard()[4][4].getSurrBombCount());
        assertEquals(10, m.getTilesRemaining());
        assertFalse(m.gameOverStatus());
    }

    @Test
    public void testBombSeeding() {
        TreeSet<Integer> bL = new TreeSet<>();
        bL.add(1);
        bL.add(2);
        bL.add(3);
        Minesweeper m = new Minesweeper(5, 5, bL);
        assertEquals(3, m.getBombCount());
        assertEquals(22, m.getTilesRemaining());
        assertFalse(m.gameOverStatus());
    }

    @Test
    public void testRegularGame() {
        TreeSet<Integer> bL = new TreeSet<>();
        bL.add(2);
        bL.add(4);
        bL.add(13);
        bL.add(17);
        Minesweeper m = new Minesweeper(5, 5, bL);
        assertEquals(4, m.getBombCount());
        assertEquals(21, m.getTilesRemaining());
        assertFalse(m.gameOverStatus());
        m.playTurn(4,4);
        assertFalse(m.gameOverStatus());
        m.playTurn(4,0);
        assertFalse(m.gameOverStatus());
        m.playTurn(0,3);
        assertFalse(m.gameOverStatus());
        m.playTurn(1,2);
        assertFalse(m.gameOverStatus());
        m.playTurn(1,3);
        assertFalse(m.gameOverStatus());
        m.playTurn(1,4);
        assertFalse(m.gameOverStatus());
        m.playTurn(2,2);
        assertFalse(m.gameOverStatus());
        m.playTurn(2,4);
        assertFalse(m.gameOverStatus());
        m.playTurn(4,2);
        assertTrue(m.gameOverStatus());
        assertFalse(m.gameLossStatus());
        assertTrue(m.gameWonStatus());
    }

    @Test
    public void testRegularGameButFinalClickLoses() {
        TreeSet<Integer> bL = new TreeSet<>();
        bL.add(2);
        bL.add(4);
        bL.add(13);
        bL.add(17);
        Minesweeper m = new Minesweeper(5, 5, bL);
        assertEquals(4, m.getBombCount());
        assertEquals(21, m.getTilesRemaining());
        assertFalse(m.gameOverStatus());
        m.playTurn(4,4);
        assertFalse(m.gameOverStatus());
        m.playTurn(4,0);
        assertFalse(m.gameOverStatus());
        m.playTurn(0,3);
        assertFalse(m.gameOverStatus());
        m.playTurn(1,2);
        assertFalse(m.gameOverStatus());
        m.playTurn(1,3);
        assertFalse(m.gameOverStatus());
        m.playTurn(1,4);
        assertFalse(m.gameOverStatus());
        m.playTurn(2,2);
        assertFalse(m.gameOverStatus());
        m.playTurn(2,4);
        assertFalse(m.gameOverStatus());
        m.playTurn(3,2);
        assertTrue(m.gameOverStatus());
        assertTrue(m.gameLossStatus());
        assertFalse(m.gameWonStatus());
        assertEquals(1, m.getTilesRemaining());
    }
}
