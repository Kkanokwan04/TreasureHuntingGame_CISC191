package edu.sdccd.cisc191.template;

import edu.sdccd.cisc191.template.OBJS.SuperObject;
import edu.sdccd.cisc191.template.template.NetworkManager;
import edu.sdccd.cisc191.template.entity.Player;
import edu.sdccd.cisc191.template.template.PlayerTimeManager;
import edu.sdccd.cisc191.template.tile.TileManager;
import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;

/**
 * The GamePanel class is responsible for rendering and managing the game state.
 * It handles input, updates the player, and manages game objects and tiles.
 * This class extends JPanel and implements the Runnable interface.
 */
public class GamePanel extends JPanel implements Runnable {

    // Screen settings
    final int size = 16;  // 16*16
    final int scale = 3;
    final int FPS = 60;

    public final int tileSize = size * scale;
    public final int maxScreenRow = 12;
    public final int maxScreenCol = 16; // these row and column may change
    public final int screenWidth = tileSize * maxScreenCol; // 768 pixels
    public final int screenHeight = tileSize * maxScreenRow; // 576 pixels

    // World Settings
    public final int maxWorldCol = 50;
    public final int maxWorldRow = 50;
    public final int screedWidth = tileSize * maxWorldCol;
    public final int screedHeight = tileSize * maxWorldRow;

    // System
    TileManager tileManager = new TileManager(this);
    KeyHandler keyH = new KeyHandler(this);
    Sound music = new Sound();
    Sound soundEffect = new Sound();
    public CollisionChecker cChecker = new CollisionChecker(this);
    public AssetSetter aSetter = new AssetSetter(this);
    public Player player = new Player(this, keyH);
    public UI ui = new UI(this);
    Thread gameThread;
    UI playTime;
    DecimalFormat dFormat = new DecimalFormat("0.00");

    // Creates 10 slots for the object
    public SuperObject[] obj = new SuperObject[10];

    // Set player's default position
    int playerX, playerY = 100;
    int playerSpeed = 4;

    // Game State
    public int gameState;
    public final int playState = 1;
    public final int pauseState = 2;
    public final int titleState = 0;

    // Networking
    private NetworkManager networkManager;  // Use NetworkManager
    private PlayerTimeManager timeManager = new PlayerTimeManager();

    // Constructor initializes game panel settings and adds key listeners
    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);

        // Initialize NetworkManager
        networkManager = new NetworkManager();
    }

    /**
     * Set up the game environment by initializing objects and playing background music.
     */
    public void setupGame() {
        aSetter.setObject();
        playMusic(0);
        gameState = titleState;
    }

    /**
     * Starts the game loop by creating a new thread for the game.
     */
    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();

        if (JOptionPane.showConfirmDialog(this, "Do you want to run the server") == 0) {
            networkManager.startClient("localhost");
        }
    }

    /**
     * The main game loop, responsible for updating game logic and rendering frames.
     */
    @Override
    public void run() {
        double drawInterval = 1000000000.0 / FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        int drawCount = 0;

        // Check if the gameThread object exists
        while (gameThread != null) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            timer += currentTime - lastTime;
            lastTime = currentTime;

            if (delta >= 1) {
                update();
                repaint();
                delta--;
                drawCount++;
            }
        }
    }

    /**
     * Updates the game state by updating the player and checking game state.
     */
    public void update() {
        // Game logic updates here
        if (gameState == playState) {
            player.update();
            // Update playtime
            timeManager.updateTime();

            networkManager.updateServerTimeState();

            networkManager.checkGameCompletion();
        }
    }

    /**
     * Paints the game screen by drawing titles, objects, and the player.
     *
     * @param g The graphics object used for rendering.
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // Debug
        long drawStart = 0;
        if (keyH.checkDrawTime) {
            drawStart = System.nanoTime();
        }

        // Tile Screen
        if (gameState == titleState) {
            ui.draw(g2);
        } else {
            // Tile
            tileManager.draw(g2);

            // Object
            for (int i = 0; i < obj.length; i++) {
                if (obj[i] != null) {
                    obj[i].draw(g2, this);
                }
            }

            // Draw method on the player instance
            player.draw(g2); // Example player render

            // UI
            ui.draw(g2);
        }

        if (keyH.checkDrawTime) {
            long drawEnd = System.nanoTime();
            long passed = drawEnd - drawStart;
            g2.setColor(Color.white);
            g2.drawString("Draw Time: " + passed, 10, 400);
            System.out.println("Draw Time: " + passed);
        }

        g2.dispose();
    }

    /**
     * Play background music by setting the sound file and starting playback
     *
     * @param i The index of the sound file to play
     */
    public void playMusic(int i) {
        music.setFile(i);
        music.play();
        music.loop();
    }

    /**
     * Stops the currently playing music.
     */
    public void stopMusic() {
        music.stop();
    }

    /**
     * Plays a music effect.
     *
     * @param i The index of the sound effect file.
     */
    public void playSE(int i) {
        soundEffect.setFile(i);
        soundEffect.play();
    }

    /**
     *  Returns the PlayTimeManager instance used to track and manage
     *  the player's playtime during the game.
     *
     *  @return PLayTimeManager instance the manages playtime
     */
    public PlayerTimeManager getTimeManager() {
        return timeManager;
    }

    /**
     * Returns the UI instance for the game. This instance handles
     * all user interface rendering and interactions.
     *
     * @return the UI instance that manages game UI elements.
     */
    public UI getUi() {
        return ui;
    }

    /**
     * Sets the current state of the game.
     *
     * @param state the new state to set for the game (e.g., playState, pauseState)
     */
    public void setGameState(int state) {
        gameState = state;
    }
}
