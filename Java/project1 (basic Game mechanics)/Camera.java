/* SWEN20003 Object Oriented Software Development
 * RPG Game Engine
 * Author: <Your name> <Your login>
 */

import org.newdawn.slick.SlickException;

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
    
    /** Map Width                */
    public final int mapWidth;
    /** Map Height               */
    public final int mapHeight;

    
    /** The camera's position in the world, in x and y coordinates. */
    private int xPos;
    private int yPos;

    /** Getters for the Camera position */
    public int getxPos() {
        // TO DO: Fill In
    	return (int)xPos;
    }

    public int getyPos() {
        // TO DO: Fill In
    	return (int)yPos;
    }
    
    /** Setters for the Camera position */
    public void setxPos(int xPos) {
    	this.xPos = xPos;
    }
    
    public void setyPos(int yPos) {
    	this.yPos = yPos;
    }

    
    /** Assign Camera values, setting up the camera. */
    public Camera(Player player, int screenwidth, int screenheight,
    		int mapWidth, int mapHeight, int TileWidth, int TileHeight)
    {   
        // TO DO: Fill In
    	
    	this.screenwidth = screenwidth;
    	this.screenheight = screenheight;
    	
    	this.mapWidth = mapWidth*TileWidth;
    	this.mapHeight = mapHeight*TileHeight;
    	
    	xPos = player.getxPos();
    	yPos = player.getyPos();
    	
    	unitFollow = player;
    	
    	
    }
    


    /** Update the game camera to recentre it's viewpoint around the player 
     */
    public void update()
    throws SlickException
    {
        // TO DO: Fill In
    	xPos = (int)(unitFollow.getxPos() - screenwidth/2.0);
    	yPos = (int)(unitFollow.getyPos() - screenheight/2.0);
    	
    

    }
    
    /** Returns the minimum x value on screen 
     */
    public int getMinX(){
        // TO DO: Fill In
    	return (int)(xPos - screenwidth/2.0);
    }
    
    /** Returns the maximum x value on screen 
     */
    public int getMaxX(){
        // TO DO: Fill In
    	return (int)(xPos + screenwidth/2.0);
    }
    
    /** Returns the minimum y value on screen 
     */
    public int getMinY(){
        // TO DO: Fill In
    	return (int)(yPos - screenheight/2.0);
    }
    
    /** Returns the maximum y value on screen 
     */
    public int getMaxY(){
        // TO DO: Fill In
    	return (int)(yPos + screenheight/2.0);
    }

    /** Tells the camera to follow a given unit. 
     */
    public void followUnit(Object unit)
    throws SlickException
    {
        // TO DO: Fill In
    	
    	Player followUnit = (Player)unit;
    	
    	xPos = (int)(followUnit.getxPos() - screenwidth/2.0);
    	yPos = (int)(followUnit.getyPos() - screenheight/2.0);
    	
    }
    
}