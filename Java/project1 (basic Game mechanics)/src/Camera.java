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

    
    /** The camera's position in the world, in x and y coordinates. */
    private int xPos;
    private int yPos;

    
    public int getxPos() {
        // TO DO: Fill In
    	return (int)xPos;
    }

    public int getyPos() {
        // TO DO: Fill In
    	return (int)yPos;
    }

    
    /** Create a new World object. */
    public Camera(Player player, int screenwidth, int screenheight)
    {   
        // TO DO: Fill In
    	
    	this.screenwidth = screenwidth;
    	this.screenheight = screenheight;
    	
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
    

    }
    
    /** Returns the minimum x value on screen 
     */
    public int getMinX(){
        // TO DO: Fill In
    }
    
    /** Returns the maximum x value on screen 
     */
    public int getMaxX(){
        // TO DO: Fill In
    }
    
    /** Returns the minimum y value on screen 
     */
    public int getMinY(){
        // TO DO: Fill In
    }
    
    /** Returns the maximum y value on screen 
     */
    public int getMaxY(){
        // TO DO: Fill In
    }

    /** Tells the camera to follow a given unit. 
     */
    public void followUnit(Object unit)
    throws SlickException
    {
        // TO DO: Fill In
    	
    	Player followUnit = (Player)unit;
    	
    	xPos = followUnit.getxPos() - screenwidth/2;
    	yPos = followUnit.getyPos() - screenheight/2;
    	
    }
    
}