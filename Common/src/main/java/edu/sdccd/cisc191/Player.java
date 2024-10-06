package edu.sdccd.cisc191;


import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import edu.sdccd.cisc191.KeyHandler;
import edu.sdccd.cisc191.GamePanel;

public class Player  extends Entity {

    GamePanel gp;
    KeyHandler keyH;
    public int hasKey =0;

    public final int screenX;
    public final int screenY;


    public Player(GamePanel gp, KeyHandler keyH) {
        this.gp = gp;
        this.keyH = keyH;

        screenX = gp.screenWidth/2 - (gp.tileSize/2);
        screenY = gp.screenHeight/2 - (gp.tileSize/2);

        solidArea = new Rectangle();
        solidArea.x = 8;
        solidArea.y = 16;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        solidArea.width = 32;
        solidArea.height = 32;

        setDefaultValues();
        getPlayerImage();
    }

    public void setDefaultValues(){
        worldX = gp.tileSize * 23;
        worldY = gp.tileSize * 21;
        speed = 4;
        direction = "down";
    }

    public void getPlayerImage() {

        up1 = setUp("Sprite-up1");
        up2 = setUp("Sprite-up2");
        down1 = setUp("Sprite-down11");
        down2 = setUp("Sprite-down2");
        left1 = setUp("Sprite-left1");
        left2 = setUp("Sprite-left2");
        right1 = setUp("Sprite-right1");
        right2 = setUp("Sprite-right2");

    }

    public BufferedImage setUp(String imageName){
        UtilityTool uTool = new UtilityTool();
        BufferedImage image = null;

        try{
            image = ImageIO.read(getClass().getResourceAsStream("/player/" + imageName + ".png"));
            image = uTool.scaleImage(image, gp.tileSize, gp.tileSize);
        }catch(IOException e){
            e.printStackTrace();
        }
        return image;

    }

    public  void update(){
        // Game logic updates here
        // Y values increases when they go down
        // X values increases when they go to the right

        if(keyH.upPressed == true || keyH.downPressed == true
                || keyH.leftPressed == true || keyH.rightPressed == true){
            if(keyH.upPressed == true){
                direction = "up";
            }
            else if(keyH.downPressed == true){
                direction = "down";
            }
            else if(keyH.leftPressed == true){
                direction = "left";
            }
            else if(keyH.rightPressed == true){
                direction = "right";
            }

            //Check Tile Collision
            collisionOn = false;
            gp.cChecker.checkTile(this);

            //Check object collision
            int objIndex = gp.cChecker.checkObject(this, true);
            pickUpObject(objIndex);

            //If collision is false, player cannot move
            if(collisionOn == false){

                switch(direction){
                    case "up":
                        worldY -= speed;
                        break;
                    case "down":
                        worldY += speed;
                        break;
                    case "left":
                        worldX -= speed;
                        break;
                    case "right":
                        worldX += speed;
                        break;
                }
            }

            spriteCounter++;
            if(spriteCounter >= 12){
                if(spriteNumber == 1){
                    spriteNumber =2;
                }
                else if(spriteNumber == 2){
                    spriteNumber =1;
                }
                spriteCounter = 0;
            }
        }

    }

    public void pickUpObject(int i){
        if(i != 999){
            String objectName = gp.obj[i].name;

            switch(objectName){
                case "Key":
                    gp.playSE(1);
                    hasKey++;
                    gp.obj[i] = null;
                    gp.ui.showMessage("You got a KEY!");
                    break;
                case "Door":
                    gp.playSE(3);
                    if(hasKey > 0){
                        gp.obj[i] = null;
                        hasKey--;
                    }
                    else{
                        gp.ui.showMessage("You need a KEY!!");
                    }
                    break;
                case "Blueheart":
                    gp.playSE(2);
                    speed += 2;
                    gp.obj[i] = null;
                    gp.ui.showMessage("RUN! RUNN!!");
                    break;
                case "Chest":
                    gp.ui.gameFinished = true;
                    gp.playSE(4);
                    gp.stopMusic();
                    break;

            }
        }

    }

    public void draw(Graphics2D g2){

        BufferedImage image = null;

        //Character Movement
        switch (direction){
            case "up":
                if( spriteNumber ==1){
                    image = up1;
                }
                else if(spriteNumber ==2){
                    image = up2;
                }
                break;
            case "down":
                if( spriteNumber ==1){
                    image = down1;
                }
                else if(spriteNumber ==2){
                    image = down2;
                }
                break;
            case "left":
                if( spriteNumber ==1){
                    image = left1;
                }
                else if(spriteNumber ==2){
                    image = left2;
                }
                break;
            case "right":
                if( spriteNumber ==1){
                    image = right1;
                }
                else if(spriteNumber ==2){
                    image = right2;
                }
                break;

        }
        g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
    }
}


