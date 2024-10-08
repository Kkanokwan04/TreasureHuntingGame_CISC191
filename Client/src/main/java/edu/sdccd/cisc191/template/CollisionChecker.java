package edu.sdccd.cisc191.template;

import edu.sdccd.cisc191.template.OBJS.SuperObject;
import edu.sdccd.cisc191.template.entity.Entity;

/**
 * This class is responsible for checking collisions between entities and tiles or objects in the game.
 * It handles collision detection based on the direction and movement of the entity.
 */

public class CollisionChecker {
    GamePanel gp;

    /**
     * Constructor for the CollisionChecker class.
     * It initializes the class with a reference to the current game panel.
     *
     * @param gp The current game panel.
     */
    public CollisionChecker(GamePanel gp) {
        this.gp = gp;
    }

    /**
     * Checks for collisions between an entity and the surrounding tiles based on the entity's direction.
     * If a collision is detected, the entity's collision flag is set to true.
     *
     * @param entity The entity whose collision is being checked.
     */
    public void checkTile(Entity entity) {
        int entityLeftCol = (entity.worldX + entity.solidArea.x) / gp.tileSize;
        int entityRightCol = (entity.worldX + entity.solidArea.x + entity.solidArea.width) / gp.tileSize;
        int entityTopRow = (entity.worldY + entity.solidArea.y) / gp.tileSize;
        int entityBottomRow = (entity.worldY + entity.solidArea.y + entity.solidArea.height) / gp.tileSize;

        // Switch based on the entity's direction to check for collisions
        switch (entity.direction) {
            case "up":
                checkTileCollision(entity, entityLeftCol, entityRightCol, entityTopRow - entity.speed / gp.tileSize);
                break;
            case "down":
                checkTileCollision(entity, entityLeftCol, entityRightCol, entityBottomRow + entity.speed / gp.tileSize);
                break;
            case "left":
                checkTileCollision(entity, entityLeftCol - entity.speed / gp.tileSize, entityLeftCol,
                        entityTopRow);
                break;
            case "right":
                checkTileCollision(entity, entityRightCol + entity.speed / gp.tileSize, entityRightCol,
                        entityTopRow);
                break;
        }
    }

    /**
     * Checks for collisions with tiles based on the given row and columns.
     *
     * @param entity    The entity being checked for collisions.
     * @param leftCol   The left column of the entity.
     * @param rightCol  The right column of the entity.
     * @param targetRow The target row for collision checking.
     */
    private void checkTileCollision(Entity entity, int leftCol, int rightCol, int targetRow)
    {
        int tileNum1 = gp.tileManager.mapTileNum[leftCol][targetRow];
        int tileNum2 = gp.tileManager.mapTileNum[rightCol][targetRow];

        if (gp.tileManager.tile[tileNum1].collision || gp.tileManager.tile[tileNum2].collision)
        {
            entity.collisionOn = true;
        }
    }


    /**
     * Checks player and object collisions
     * @param entity object in the world
     * @param player boolean
     * @return the index of the collision
     */
    public int checkObject(Entity entity, boolean player)
    {
        int index = 999;
        for (int i = 0; i < gp.obj.length; i++)
        {
            if (gp.obj[i] != null)
            {
                // Get entity's solid area
                entity.solidArea.x = entity.worldX + entity.solidArea.x;
                entity.solidArea.y = entity.worldY + entity.solidArea.y;

                // Get the solid area object
                gp.obj[i].solidArea.x = gp.obj[i].worldX + gp.obj[i].solidArea.x;
                gp.obj[i].solidArea.y = gp.obj[i].worldY + gp.obj[i].solidArea.y;

                // Check for collision
                if (checkObjCollision(entity, gp.obj[i]))
                {
                    if (gp.obj[i].collision)
                    {
                        entity.collisionOn = true;
                    }
                    if (player) {
                        index = i;
                    }
                }

                // Reset solid areas
                resetSolidAreas(entity, gp.obj[i]);
            }
        }
        return index;
    }

    /**
     * Checks the collisions based on the direction of movement
     * @param entity object in the world
     * @param obj the SuperObject of entities int the world
     * @return a boolean if objects are colliding.
     */
    private boolean checkObjCollision(Entity entity, SuperObject obj)
    {
        switch (entity.direction)
        {
            case "up":
                entity.solidArea.y -= entity.speed;
                break;
            case "down":
                entity.solidArea.y += entity.speed;
                break;
            case "left":
                entity.solidArea.x -= entity.speed;
                break;
            case "right":
                entity.solidArea.x += entity.speed;
                break;
        }
        boolean isColliding = entity.solidArea.intersects(obj.solidArea);

        // Reset position after check
        switch (entity.direction)
        {
            case "up":
                entity.solidArea.y += entity.speed;
                break;
            case "down":
                entity.solidArea.y -= entity.speed;
                break;
            case "left":
                entity.solidArea.x += entity.speed;
                break;
            case "right":
                entity.solidArea.x -= entity.speed;
                break;
        }
        return isColliding;
    }

    /**
     * Resets the solid areas of both the specified entity and super object to their default positions.
     * @param entity the entity whose solid area needs to be reset
     * @param obj the super object whose solid area needs to be reset
     */
    private void resetSolidAreas(Entity entity, SuperObject obj)
    {
        entity.solidArea.x = entity.solidAreaDefaultX;
        entity.solidArea.y = entity.solidAreaDefaultY;
        obj.solidArea.x = obj.solidAreaDefaultX;
        obj.solidArea.y = obj.solidAreaDefaultY;
    }
}
