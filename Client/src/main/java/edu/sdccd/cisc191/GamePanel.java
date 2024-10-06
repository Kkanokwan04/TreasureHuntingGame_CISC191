package edu.sdccd.cisc191;

import edu.sdccd.cisc191.GameClient;
import edu.sdccd.cisc191.GameServer;
import OBJS.SuperObject;
import edu.sdccd.cisc191.Player;
import edu.sdccd.cisc191.PlayerTimeManager;
import tile.TileManager;
import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;


public class GamePanel extends JPanel implements Runnable{

    //Screen setting
    final int size = 16;  // 16*16
    final int scale = 3;
    final int FPS = 60;

    public final int tileSize = size * scale;
    public final int maxScreenRow = 12;
    public final int maxScreenCol = 16; //these row and column may change
    public final int screenWidth = tileSize * maxScreenCol; // 768 pixels
    public final int screenHeight = tileSize * maxScreenRow;// 576 pixels

    //World Settings
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

    //Creates 10 slots for the object
    public SuperObject obj[] = new SuperObject[10];

    // Set player's default position
    int playerX, playerY = 100;
    int playerSpeed = 4;

    //Game State
    public int gameState;
    public final int playState = 1;
    public final int pauseState = 2;
    public final int titleState =0;

    //Networking
    private GameClient socketClient;
    private GameServer socketServer;
    private PlayerTimeManager timeManager = new PlayerTimeManager();
    private long lastTimeSent = 0;
    private long sendInterval = 1000;


    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);

        // this GamePanel can be focused to receive key input
        this.setFocusable(true);

    }

    public void setupGame(){

        aSetter.setObject();
        playMusic(0);
        gameState = titleState;
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();


        if(JOptionPane.showConfirmDialog(this, "Do you want to run the server") ==0){
            socketServer = new GameServer(this);
            socketServer.start();
        }

        socketClient = new GameClient(this, "localhost");
        socketClient.start();
    }


    public void run() {
        double drawInterval = 1000000000 / FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer =0;
        int drawCount = 0;

        // check if the gameThred object exists
        while(gameThread != null) {
            currentTime = System.nanoTime();

            delta += (currentTime - lastTime) / drawInterval;
            timer += currentTime - lastTime;
            lastTime = currentTime;


            if(delta >= 1) {
                update();
                repaint();
                delta--;
                drawCount++;
            }


//            if(timer >= 1000000000) {
//                System.out.println("FPS: " + drawCount);
//                drawCount = 0;
//                timer =0;
//            }
        }
    }

    public void update() {
        // Game logic updates here
        if(gameState == playState) {
            player.update();
            // Update playtime
            timeManager.updateTime();

            // Send intermediate playtime to the server
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastTimeSent >= sendInterval) {
                String playTimeString = "Total time in the Game: " + timeManager.getFormattedPlayTime();
                socketClient.sendData(playTimeString.getBytes());
                lastTimeSent = currentTime;  // Reset the last time sent
            }

            // Check if the game is completed and send the final playtime if so
            checkGameCompletion();  // Call without arguments
        }

        if(gameState == pauseState) {
            // nothing happen yet
        }

    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // Debug
        long drawStart = 0;
        if(keyH.checkDrawTime == true){
            drawStart = System.nanoTime();
        }

        //Tile Screen
        if(gameState == titleState) {
            ui.draw(g2);
        }
        else{
            //Tile
            tileManager.draw(g2);

            // Object
            for(int i=0; i < obj.length; i++) {
                // Check if the slot is empty or not, to avoid NullPointer error
                if(obj[i] != null) {
                    obj[i].draw(g2, this);
                }
            }

            // Draw method on the player instance
            player.draw(g2); // Example player render

            //UI
            ui.draw(g2);

        }

        if(keyH.checkDrawTime == true){
            long drawEnd = System.nanoTime();
            long passed = drawEnd - drawStart;
            g2.setColor(Color.white);
            g2.drawString("Draw Time: " + passed, 10, 400);
            System.out.println("Draw Time: " + passed);
        }

        g2.dispose();
    }
    public void checkGameCompletion() {
        // Example logic to check if the game is finished (replace with your condition)
        if (ui.gameFinished == true) {
            // Stop sending intermediate times
            gameState = pauseState;  // Or some state that stops the game

            // Calculate the total playtime
            String finalTimeString = "Total playtime: " + timeManager.getFormattedPlayTime();

            // Send the final playtime to the server
            socketClient.sendData(finalTimeString.getBytes());
//
//            // Optionally, stop the client if needed
//            socketClient.stopClient();
        }
    }


    public void playMusic(int i){
        music.setFile(i);
        music.play();
        music.loop();
    }

    public void stopMusic(){

        music.stop();
    }

    public void playSE(int i){
        soundEffect.setFile(i);
        soundEffect.play();
    }


}
