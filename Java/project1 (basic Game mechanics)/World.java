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
	private static boolean[][] blocked; //store the tiles for terrain bounding
	
	private float x = 756f, y = 684f; // starting position of the player
	
	
	// Height and Width
	private static int mapHeight, mapWidth;
	// Tile Width+Height
	private static int TileWidth, TileHeight;
	
    // players and cameras in the world
	private Player player;
	private Camera cam;
	
	// speed of the world rendering (same as player speed)
	private float speed = 0.5f;
	
	//Tiled map used
	private TiledMap map;
	
	
    /** Create a new World object. */
    public World()
    throws SlickException
    {
        // TODO: Fill in
    	
    	// initialize the components of the world ( map, player and camera)
    	map = new TiledMap("assets/map.tmx", "assets");
    	
        // initialize static variables
    	mapHeight = map.getHeight();
    	mapWidth = map.getWidth();
    	
    	TileWidth = map.getTileWidth();
    	TileHeight = map.getTileHeight();
    	
        player = new Player();
        cam = new Camera(player, RPG.screenwidth, RPG.screenheight, 
        		map.getWidth(), map.getHeight(), TileWidth, TileHeight );
        

    	
    	
    	//Build a collision map based on Tiles
    	blocked = new boolean[map.getWidth()][map.getHeight()];
    	for (int x = 0; x < map.getWidth(); x++) 
    	{
    		for (int y = 0; y < map.getHeight(); y++) 
    		{
    			int tileId = map.getTileId( x, y, 0 );
    			String value = map.getTileProperty( tileId, "block", "0" );
    			
    			if ("1".equals(value)) 
    			{
    				blocked[x][y] = true; //assign true to blocking tiles
    			} else {
    				blocked[x][y] = false;
    			}

    		}
    	}
    
    }
    
    /** check whether if the block is blocking the player */
    public static boolean isBlock( float xPos, float yPos ) 
    {
    	// check whether it is within the map
    	if ( xPos < 0 ) xPos = 0;
    	if ( yPos < 0 ) yPos = 0;

    	// get the tile in that position
    	xPos = Math.round( xPos/(float)TileWidth );
    	yPos = Math.round( yPos/(float)TileHeight );
    	
    	// check whether the tile exists in the map
    	if ( xPos >= mapWidth ) xPos = mapWidth - 1;
    	if ( yPos >= mapHeight ) yPos = mapHeight - 1;
    	
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
    	if ( dir_x == -1 && !World.isBlock( x - delta * speed, y ) ) {
    		x -= delta * speed;

    	}
    	if ( dir_x == 1 && !World.isBlock( x + delta * speed, y )  ) {
    		x += delta * speed;

    	}
    	if ( dir_y == 1 && !World.isBlock( x , y + delta * speed ) ) {
    		y += delta * speed;

    	}
    	if ( dir_y == -1 && !World.isBlock( x , y - delta * speed  ) ) {
    		y -= delta * speed;

    	}
    	
    	
    	/** check whether or not if we are still within the bounds */
    	if ( x < 0 ) x = 0;
    	if ( y < 0 ) y = 0;
    	
    	// Assign one less tile space to prevent going over bounds.
    	if ( x > map.getWidth()*TileWidth ) 
    		x = map.getWidth()*TileWidth - TileWidth; 
    	if ( y > map.getHeight()*TileHeight ) 
    		y = map.getHeight()*TileHeight - TileHeight;
   	
    	
        // Player moves
        player.update(dir_x, dir_y, delta);
        player.PlayerCheck(map);
        
        //Follow the player
        cam.followUnit(player);
        cameraCheck( cam );


    }

    /** Render the entire screen, so it reflects the current game state.
     * @param g The Slick graphics object, used for drawing.
     */
    public void render(Graphics g)
    throws SlickException
    {
        // TODO: Fill in
    	
    	// Translate the camera position with respect to the player
    	g.translate( -cam.getxPos(), -cam.getyPos());
    	
    	
    	// Render a section of the map
    	drawLayers(cam.getxPos(),cam.getyPos(), 800, 600);
    	// Render the player
        player.render(g);

    }
    
    private void drawLayers(int x, int y, int w, int h){
    	//calculate the offsets and tile indexes for the render
        int tileOffsetX = (-1*x%72);
        int tileOffsetY = (-1*y%72);
        int tileIndexX  = x/72;
        int tileIndexY  = y/72;
        
        //render the map
        map.render(x + tileOffsetX,y+ tileOffsetY, tileIndexX, tileIndexY,
                        (w - tileOffsetX)/72 + 1,
                        (h - tileOffsetY)/72 + 1
                        );
        
    }
    
    /** keeps the camera within the world, prevents black margins */
    private void cameraCheck( Camera cam ) {
    	//Maximum offsets
    	int offsetMaxX = map.getWidth()*TileWidth - cam.screenwidth;
    	int offsetMaxY = map.getHeight()*TileHeight - cam.screenheight;
    	//Minimum offsets
    	int offsetMinX = 0;
    	int offsetMinY = 0;
    	
    	// Apply the offset restrictions if necessary
    	if ( cam.getxPos() < offsetMinX ) {
    		cam.setxPos( offsetMinX );
    	}
    	if ( cam.getyPos() < offsetMinY ) {
    		cam.setyPos( offsetMinY );
    	}
    	if ( cam.getxPos() > offsetMaxX ) {
    		cam.setxPos( offsetMaxX );
    	}
    	if ( cam.getyPos() > offsetMaxY ) {
    		cam.setyPos( offsetMaxY );
    	}
    }
}
