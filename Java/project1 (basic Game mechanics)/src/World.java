/* 433-294 Object Oriented Software Development
 * RPG Game Engine
 * Author: <Your name> <Your login>
 */

import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;

/** Represents the entire game world.
 * (Designed to be instantiated just once for the whole game).
 */
public class World
{
	private static boolean[][] blocked;
	
	//private float x = 756f, y = 684f;
	private float x = 0f, y = 0f;
	
	private TiledMap map;
    /** Create a new World object. */
    public World()
    throws SlickException
    {
        // TODO: Fill in
    	map = new TiledMap("assets/map.tmx", "assets");
    	
    	//Build a collision map based on Tiles
    	blocked = new boolean[map.getWidth()][map.getHeight()];
    	for (int x = 0; x < map.getWidth(); x++) {
    		for (int y = 0; y < map.getHeight(); y++) {
    			int tileId = map.getTileId( x, y, 0 );
    			String value = map.getTileProperty( tileId, "block", "0" );
    			if ("1".equals(value)) {
    				blocked[x][y] = true;
    			}
    		}
    	}
    
    }
    
    public static boolean isBlock( float xPos, float yPos ) {
    	xPos = Math.round( xPos/72 );
    	yPos = Math.round( yPos/72 );
    	return blocked[ (int)xPos ][ (int)yPos ];
    }

    /** Update the game state for a frame.
     * @param dir_x The player's movement in the x axis (-1, 0 or 1).
     * @param dir_y The player's movement in the y axis (-1, 0 or 1).
     * @param delta Time passed since last frame (milliseconds).
     */
    public void update(double dir_x, double dir_y, int delta)
    throws SlickException
    {
        // TODO: Fill in



    }

    /** Render the entire screen, so it reflects the current game state.
     * @param g The Slick graphics object, used for drawing.
     */
    public void render(Graphics g)
    throws SlickException
    {
        // TODO: Fill in
    	map.render( (int)x, (int)y);
    }
}
