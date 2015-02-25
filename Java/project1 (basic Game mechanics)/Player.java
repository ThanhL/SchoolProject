import org.newdawn.slick.Animation;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Image; 
import org.newdawn.slick.tiled.TiledMap;

/** Represents the player in the game World */
public class Player 
{
	
	
	private Animation player; // Animation Declaration for the player
	private float x = 756f, y = 684f; // starting position of the player
	private float speed = 0.5f; // player's speed
	
	/** used to check if the player's position is still within the map */
	public void PlayerCheck(TiledMap map) 
	{
		if ( x <= 0 ) 
		{
			x = 0;
		}
		if ( y <= 0 ) 
		{
			y = 0;
		}
		if ( x >= map.getWidth()*map.getTileWidth() )
		{
			x = map.getWidth()*map.getTileWidth() - map.getTileWidth();
		}
		if ( y >= map.getHeight()*map.getTileHeight() ) {
			y = map.getHeight()*map.getTileHeight() - map.getTileHeight();
			
		}
	}
	
	/** getters for the players Position */
	public int getxPos() {
		return (int)x;
	}
	
	public int getyPos() {
		return (int)y;
	}
	
	/** player creation */
	public Player() throws SlickException {
		// Image array for the player's movement
		Image [] sprite = {new Image("assets/units/player.png"),new Image("assets/units/player.png")};
		int [] duration = {300, 300}; // Duration to show each frame (300ms)
		
		// Create an instance of the player
		player = new Animation(sprite, duration, false);
	}
	
    /** Update the players position for a frame.
     * @param dir_x The player's movement in the x axis (-1, 0 or 1).
     * @param dir_y The player's movement in the y axis (-1, 0 or 1).
     * @param delta Time passed since last frame (milliseconds).
     */
    public void update(double dir_x, double dir_y, int delta)
    throws SlickException
    {
        // TODO: Fill in
    	/** update the player animation and position */
    	if ( dir_x == -1  && !World.isBlock( x - delta * speed, y )) { 
    		// go left
    		player.update(delta); 
    		x -= delta * speed; 
    		
    	}  
    	
    	if ( dir_x == 1 && !World.isBlock( x + delta * speed, y ) ) {
    		// go right
    		player.update(delta);
    		x += delta * speed;
    	}  
    	
    	if ( dir_y == 1 && !World.isBlock( x , y + delta * speed ) ) {
    		// go down
    		player.update(delta);
    		y += delta * speed;
    	} 
    	
    	if ( dir_y == -1 && !World.isBlock( x , y - delta * speed  ) ) {
    		// go up
    		player.update(delta);
    		y -= delta * speed;
    	} 
    	
    	
    }

    /** Draw the player in the current game state.
     * @param g The Slick graphics object, used for drawing.
     */
    public void render(Graphics g)
    throws SlickException
    {
        // TODO: Fill in
    	player.draw( (int)x, (int)y);
    	g.drawString("xPos = " + x, x, y); 
    	g.drawString("yPos = " + y, x, y-10);
    }
	

}
