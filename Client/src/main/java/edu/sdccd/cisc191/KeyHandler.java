package edu.sdccd.cisc191;
import edu.sdccd.cisc191.GameClient;
import edu.sdccd.cisc191.GameServer;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {

    GameClient socketClient;
    GamePanel gp;
    public boolean upPressed, downPressed, leftPressed, rightPressed;
    //Debug
    boolean checkDrawTime = false;

    public KeyHandler(GamePanel gp) {
        this.gp = gp;
    }


    @Override
    public void keyTyped(KeyEvent e) {}

    // Return the int keyCode associated with the key in this event
    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        // Title State Controls
        if (gp.gameState == gp.titleState) {
            if (code == KeyEvent.VK_W) {
                gp.ui.commandNum--;
                if (gp.ui.commandNum < 0) {
                    gp.ui.commandNum = 1; // Loop back to the last option (Quit)
                }
            }
            if (code == KeyEvent.VK_S) {
                gp.ui.commandNum++;
                if (gp.ui.commandNum > 1) {
                    gp.ui.commandNum = 0; // Loop back to the first option (New Game)
                }
            }
            if (code == KeyEvent.VK_ENTER) {
                if (gp.ui.commandNum == 0) {
                    // New Game selected
                    gp.gameState = gp.playState;
                    // Play background music
                }
                if (gp.ui.commandNum == 1) {
                    // Quit selected
                    System.exit(0);
                }
//                if (code == KeyEvent.VK_ENTER) {
//                    socketClient.sendData("Player pressed enter".getBytes());
//                }

            }
        }

        // Game Play Controls
        if (gp.gameState == gp.playState) {
            if (code == KeyEvent.VK_W) {
                upPressed = true;
            }
            if (code == KeyEvent.VK_A) {
                leftPressed = true;
            }
            if (code == KeyEvent.VK_S) {
                downPressed = true;
            }
            if (code == KeyEvent.VK_D) {
                rightPressed = true;
            }
            if (code == KeyEvent.VK_P) {
                // Pause/Unpause the game
                if (gp.gameState == gp.playState) {
                    gp.gameState = gp.pauseState;
                } else if (gp.gameState == gp.pauseState) {
                    gp.gameState = gp.playState;
                }
            }
        }

        // Debug Mode
        if (code == KeyEvent.VK_T) {
            checkDrawTime = !checkDrawTime;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();

        // Release movement keys
        if (code == KeyEvent.VK_W) {
            upPressed = false;
        }
        if (code == KeyEvent.VK_A) {
            leftPressed = false;
        }
        if (code == KeyEvent.VK_S) {
            downPressed = false;
        }
        if (code == KeyEvent.VK_D) {
            rightPressed = false;
        }
    }
}

