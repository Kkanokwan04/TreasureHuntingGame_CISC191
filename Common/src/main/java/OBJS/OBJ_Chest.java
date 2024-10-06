package OBJS;

import edu.sdccd.cisc191.GamePanel;
import javafx.scene.image.Image;
import java.io.InputStream;

public class OBJ_Chest extends SuperObject {

    GamePanel gp;

    public OBJ_Chest(GamePanel gp) {
        this.gp = gp;

        name = "Chest";
        try {
            InputStream is = getClass().getResourceAsStream("/object/chest.png");
            image1 = new Image(is, gp.tileSize, gp.tileSize, false, true);  // Load and scale image natively
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
