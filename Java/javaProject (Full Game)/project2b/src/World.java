/* 433-294 Object Oriented Software Development
 * RPG Game Engine
 * Sample Solution
 * Author: Matt Giuca <mgiuca>
 */

import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;

/** Represents the entire game world.
 * (Designed to be instantiated just once for the whole game).
 */
public class World
{
    Player player = null;
    private TiledMap map = null;
    private Camera camera = null;
        
    // Village and its villagers
    Village village = null;
    
    // Wilderness and its monsters
    Wilderness wilderness = null;
    
    // All items and its items
    AllItems allItems =  null;


    /** Map width, in tiles. */
    public int getMapWidth()
    {
        return map.getWidth();
    }

    /** Map height, in tiles. */
    public int getMapHeight()
    {
        return map.getHeight();
    }

    /** Tile width, in pixels. */
    public int getTileWidth()
    {
        return map.getTileWidth();
    }

    /** Tile height, in pixels. */
    public int getTileHeight()
    {
        return map.getTileHeight();
    }

    /** Create a new World object. */
    public World()
    throws SlickException
    {
        map = new TiledMap(RPG.ASSETS_PATH + "/map.tmx", RPG.ASSETS_PATH);
        player = new Player(RPG.ASSETS_PATH + "/units/player.png");
        camera = new Camera(player, RPG.screenwidth, RPG.screenheight);
        
        //Initialize the village
        village = new Village();
        
        //Initialize the wilderness
        wilderness = new Wilderness();
        
        //Initialize the items in the world
        allItems = new AllItems();
        
    }

    /** Update the game state for a frame.
     * @param dir_x The player's movement in the x axis (-1, 0 or 1).
     * @param dir_y The player's movement in the y axis (-1, 0 or 1).
     * @param attack The check if the player has pressed the attack button.
     * @param delta Time passed since last frame (milliseconds).
     */
    public void update(int dir_x, int dir_y, int attack, int delta)
    throws SlickException
    {
    	// Check if the player is still alive first
    	player.isDead();
    	
    	// player moves
        player.move(this, dir_x, dir_y, delta);
        
        // update the cooldown of the player's attack
        player.updateCoolDownTimer(delta);
        
        // attack enemies
        player.checkAttack( 
        		wilderness.getPassiveMonster(), 
        		wilderness.getAggressiveMonster(), 
        		attack );
        
        // Update Villager's gamestate
        village.update(player, this, delta);
        
        // Update Aggressive Enemies
        wilderness.update(player, this, delta);
        
        // Update item's gamestate
        allItems.update(player, this);
        
        // Update the camera
        camera.update();
        camera.cameraCheck(map);
     
    }

    /** Render the entire screen, so it reflects the current game state.
     * @param g The Slick graphics object, used for drawing.
     */
    public void render(Graphics g)
    throws SlickException
    {
  
        map.render(-(camera.getxPos() % getTileWidth()), -(camera.getyPos() % getTileHeight()), camera.getxPos() / getTileWidth(), camera.getyPos()/ getTileHeight(),
        		camera.screenwidth / getTileWidth() + 2, camera.screenheight / getTileHeight() + 2);
        
        // Render the village
        village.renderVillagers( g, camera.getxPos(), camera.getyPos() );
        
        // Render the Monsters
        wilderness.renderMonsters( g, camera.getxPos(), camera.getyPos() );
        
        // Render the Item
        allItems.renderAllItems( g, camera.getxPos(), camera.getyPos() );

        // Render the player
        player.render(g, camera.getxPos(), camera.getyPos());
        
        
        
    }

    /** Determines whether a particular map coordinate blocks movement.
     * @param x Map x coordinate (in pixels).
     * @param y Map y coordinate (in pixels).
     * @return true if the coordinate blocks movement.
     */
    public boolean terrainBlocks(double x, double y)
    {
        int tile_x = (int) x / getTileWidth();
        int tile_y = (int) y / getTileHeight();
        int tileid = map.getTileId(tile_x, tile_y, 0);
        String block = map.getTileProperty(tileid, "block", "0");
        return !block.equals("0");
    }
}
