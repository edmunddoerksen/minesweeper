// package org.cis120.minesweeper;

/*
 * CIS 120 HW09 - Minesweeper
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * This class sets up the top-level frame and widgets for the GUI.
 */
public class RunMinesweeper implements Runnable {
    public void run() {

        // Top-level frame in which game components live
        final JFrame frame = new JFrame("Minesweeper");
        frame.setLocation(400, 400);

        // Status panel
        final JPanel status_panel = new JPanel();
        frame.add(status_panel, BorderLayout.SOUTH);
        final JLabel status = new JLabel("Setting up...");
        status_panel.add(status);
        final JLabel remainingTiles = new JLabel("[Tiles Remaining]: " + 87);
        status_panel.add(remainingTiles);

        // Gameboard setup
        GameBoard board = new GameBoard(status, remainingTiles);
        frame.add(board, BorderLayout.CENTER);

        // Reset button
        final JPanel control_panel = new JPanel();
        frame.add(control_panel, BorderLayout.NORTH);

        // Note here that when we add an action listener to the reset button, we
        // define it as an anonymous inner class that is an instance of
        // ActionListener with its actionPerformed() method overridden. When the
        // button is pressed, actionPerformed() will be called.
        final JButton reset = new JButton("Reset");
        reset.addActionListener(e -> board.reset());
        control_panel.add(reset);


        // button to open a popup window with game instructions
        final JButton instructions = new JButton("Instructions");
        instructions.addActionListener(e -> JOptionPane.showMessageDialog(frame,
                "In this version of Minesweeper, there exists a 10 x 10 grid of tiles. " +
                        "13 of the 100 tiles \n" + "contain bombs; the rest do not. To begin the " +
                        "game, click on any of the tiles to uncover it. \n From here, uncover " +
                        "all the unmined tiles to win. If at any point you uncover a mined " +
                        "tile, you immediately lose and \n" + "must start a new game. Also, if " +
                        "you'd like to save your progress in the event that you lose, \n hit the " +
                        "Save button. To facilitate gameplay, for any uncovered and unmined tile " +
                        "\n that is surrounded by at least one mine, the number of surrounding " +
                        "mines is displayed in \n its center. Also, when a tile is uncovered " +
                        "that is not surrounded by any mine, all of \n its surrounding tiles" +
                        " will be uncovered. Finally, the number of remaining unmined tiles \n " +
                        "that must be uncovered for the player to win is displayed in the bottom" +
                        " right. Good luck!"));
        control_panel.add(instructions);

        // Save button
        final JButton save = new JButton("Save");
        // GameBoard finalBoard = board;
        save.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                CloseAndSave.saveGame(board.getMinesweeperGame());
            }
        });
        control_panel.add(save);

        // Load button
        final JButton load = new JButton("Load");
        load.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.remove(board);
                GameBoard boardUpdate = new GameBoard(
                        status, new JLabel("[Tiles Remaining]: " + "96"),
                        CloseAndSave.loadGame());
                frame.add(boardUpdate, BorderLayout.CENTER);
            }
        });
        control_panel.add(load);

        // Put the frame on the screen
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // Start the game
        board.reset();
    }
}