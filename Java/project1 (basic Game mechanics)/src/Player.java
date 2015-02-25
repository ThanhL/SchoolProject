import org.newdawn.slick.Animation;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Image; 

public class Player {
	//private float x = 0f, y = 0f;
	
	private Animation player;
	//private float x = 400f, y = 300f;
	private float x = 756f, y = 684f;
	
	public int getxPos() {
		return (int)x;
	}
	
	public int getyPos() {
		return (int)y;
	}
	
	public Player() throws SlickException {
		
		Image [] sprite = {new Image("assets/units/player.png"),new Image("assets/units/player.png")};
		int [] duration = {300, 300};
		
		player = new Animation(sprite, duration, false);
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
    	if ( dir_x == -1  && !World.isBlock( x - delta * 0.5f, y )) { 
    		// go left

    		player.update(delta);
    		x -= delta * 0.5f;
    		
    	}  
    	
    	if ( dir_x == 1 && !World.isBlock( x + delta * 0.5f, y ) ) {
    		// go right
    		player.update(delta);
    		x += delta * 0.5f;
    	}  
    	
    	if ( dir_y == 1 && !World.isBlock( x , y + delta * 0.5f ) ) {
    		// go down
    		player.update(delta);
    		y += delta * 0.5f;
    	} 
    	
    	if ( dir_y == -1 && !World.isBlock( x , y - delta * 0.5f ) ) {
    		// go up
    		player.update(delta);
    		y -= delta * 0.5f;
    	} 
    	
    	
    }

    /** Render the entire screen, so it reflects the current game state.
     * @param g The Slick graphics object, used for drawing.
     */
    public void render(Graphics g)
    throws SlickException
    {
        // TODO: Fill in
    	player.draw( (int)x, (int)y);
    }
	

}
