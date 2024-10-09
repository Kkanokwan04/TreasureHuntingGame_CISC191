package edu.sdccd.cisc191.template;

import edu.sdccd.cisc191.template.template.NetworkManager;

import javax.swing.*;

/**
 * The Client class represents the entry point for the 2D game.
 * It creates the game window, sets up the game panel, and starts the game thread.
 */
public class Client {

    /**
     * The main method is the entry point of the application.
     * It creates the game window and initializes the game panel.
     *
     * @param args Command-line arguments (not used in this application).
     */
    public static void main(String[] args) {

        // Create the main game window (JFrame)
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("2D GAME");

        // Create the GamePanel (where the game will be displayed)
        GamePanel gamePanel = new GamePanel();
        window.add(gamePanel); // Add the game panel to the window

        window.pack();// Adjust the window size based on the components

        window.setLocationRelativeTo(null); // Center the window on the screen
        window.setVisible(true); // Make the window visible

        // Set up and start the game
        gamePanel.setupGame(); // Set up initial game objects and configuration
        gamePanel.startGameThread(); // Start the game loop thread
    }
}