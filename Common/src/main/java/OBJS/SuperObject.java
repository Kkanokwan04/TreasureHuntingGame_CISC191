package OBJS;

import edu.sdccd.cisc191.GamePanel;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.geometry.Rectangle2D;

public class SuperObject {

    public Image image1;
    public String name;
    public boolean collision = false;
    public int worldX, worldY;

    // Solid area for collision detection
    public Rectangle2D solidArea = new Rectangle2D(0, 0, 48, 48); // Default size of 48x48 (can be adjusted)
    public int solidAreaDefaultX = 0;
    public int solidAreaDefaultY = 0;

    // Drawing the object on the canvas
    public void draw(GraphicsContext gc, GamePanel gp) {
        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        // Only draw the object if it's within the visible range
        if (worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
                worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
                worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
                worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {

            gc.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize); // Draw image scaled to tile size
        }
    }

    // Update solid area position if needed (for dynamic collision checks)
    public void updateSolidArea(int x, int y, int width, int height) {
        this.solidArea = new Rectangle2D(x, y, width, height);
    }
}