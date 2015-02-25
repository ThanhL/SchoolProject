/* SWEN20003 Object Oriented Software Development
 * RPG Game Engine
 * Author: <Your name> <Your login>
 */

import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;

/** Represents the camera that controls our viewpoint.
 */
public class Camera
{

	/** The unit this camera is following */
	private Player unitFollow;
	
	/** The width and height of the screen */
    /** Screen width, in pixels. */
    public final int screenwidth;
    /** Screen height, in pixels. */
    public final int screenheight;

	
	/** The camera's position in the world, in x and y coordinates. */
	private int xPos;
	private int yPos;

	/** The xPos of the camera (pixels) */
	public int getxPos() {
		return xPos;
	}
	/** The yPos of the camera (pixels) */
	public int getyPos() {
		return yPos;
	}

	
    /** Create a new World object. */
    public Camera(Player player, int screenwidth, int screenheight)
    {	
       	this.unitFollow = player;
       	this.screenwidth = screenwidth;
       	this.screenheight = screenheight - RPG.PANELHEIGHT; //Adjust height with panel
    }

    /** Update the game camera to recentre it's viewpoint around the player 
     */
    public void update()
    throws SlickException
    {
    	   // Update the camera based on the player's position
    	xPos = (int) unitFollow.getX() - (screenwidth/2);
    	yPos = (int) unitFollow.getY() - (screenheight/2); 
    }
    
    /** Returns the minimum x value on screen 
     */
    public int getMinX(){
    	return xPos;
    }
    
    /** Returns the maximum x value on screen 
     */
    public int getMaxX(){
    	return xPos + screenwidth;
    }
    
    /** Returns the minimum y value on screen 
     */
    public int getMinY(){
    	return yPos;
    }
    
    /** Returns the maximum y value on screen 
     */
    public int getMaxY(){
    	return yPos+screenheight;
    }

    /** Tells the camera to follow a given unit. 
     */
    public void followUnit(Object unit)
    throws SlickException
    {
    	if(unit.getClass() == Player.class){
    		unitFollow = (Player) unit;
    	}
    }
    
    /** Prevents the camera from going off the Tiledmap
     * @param map The Tiledmap to check the camera's position.
     */
    public void cameraCheck( TiledMap map ) {
    	//Maximum offsets
    	int offsetMaxX = map.getWidth()*map.getTileWidth() - screenwidth;
    	int offsetMaxY = map.getHeight()*map.getTileHeight() - screenheight;
    	//Minimum offsets
    	int offsetMinX = 0;
    	int offsetMinY = 0;
    	
    	// Apply the offset restrictions if necessary
    	if ( xPos < offsetMinX ) {
    		this.xPos = offsetMinX;
    	}
    	if ( yPos < offsetMinY ) {
    		this.yPos = offsetMinY;
    	}
    	if ( xPos > offsetMaxX ) {
    		this.xPos = offsetMaxX;
    	}
    	if ( yPos > offsetMaxY ) {
    		this.yPos = offsetMaxY;
    	}
    }
    
}