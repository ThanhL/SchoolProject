import java.util.Random;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
/** Class that represents the attributes and methods regarding passive Monsters */
public class PassiveMonster extends Monster {
	
	private Image img;
	
	private double time; // Time elapsed (in seconds)
	private static final int TIME_DELAY = 3; // Time Delay for Passive Monster to wander
	private double timeHit; // Time elapsed after attacked
	
	public static final int TIME_RUNAWAY = 5;
	
	private boolean ifHit = false; // whether the monster is hit by the player
	
	private Random r = new Random(); // Used to pick a random direction
	
	private int direction = Math.abs(r.nextInt() % 8); // the direction for the monster to move
	
	private static final double SPEED = 0.1; // Movement speed of the passiveMonster (Pixels per ms)
	
	/** Constructor for passive monster.
	 * @param img_path The image of the passive monster.
	 * @param x The x location to be added.
	 * @param y The y location to be added.
	 * @param name The name of the passive monster.
	 */
	public PassiveMonster( String img_path, double x, double y, String name ) throws SlickException {
		super( x, y, name );
		img = new Image( img_path );		
		
	}
	
	// PassiveMonsters in the game world.
	
	/** Add Giant Bats to the game world.
	 * @param x The x location to be added.
	 * @param y The y location to be added.
	 * @return Returns the bat entity added.
	 */
	public static PassiveMonster addBat( double x, double y ) throws SlickException {
		// Create Giant Bat  entity
		PassiveMonster giantBat =  new PassiveMonster( RPG.ASSETS_PATH + "/units/dreadbat.png", x, y, "Giant Bat" );
		
		// Stats for the Bat
		double HP = 40; // Also equal to MaxHP (since its initial)
		double dmg = 0;
		double Cooldown = 0;
		
		// Now set its stats
		giantBat.setMaxDmg( dmg );
		giantBat.setHP( HP );
		giantBat.setMaxHp( HP );
		giantBat.setCoolDown( Cooldown );
		
		
		return giantBat;
	}
	
	/** Renders the passive monster in the game world
     * @param g The current Graphics context.
     * @param cam_x Camera x position in pixels.
     * @param cam_y Camera y position in pixels.
	 */
	public void render( Graphics g, int cam_x, int cam_y ) {
		
        int screen_x, screen_y;
        screen_x = (int) (this.getX() - cam_x);
        screen_y = (int) (this.getY() - cam_y);		
		img.drawCentered( screen_x, screen_y );		
	}
	
	/** Allows the passive monster to randomly roam around the game world.
	 * @param player The player in the game world.
	 * @param world The world the monster is in. (to check terrain blocking)
	 * @param delta The time that has passed since the last frame. (milliseconds)
	 */
	public void moveRandom( Player player, World world, int delta ) {
		
		
		time += delta/1000.0; // Time elapsed converted to seconds
		
		// New directions to move to
		double new_x = this.getX();
		double new_y = this.getY();

		// If 3 seconds have passed
		if ( time <= TIME_DELAY ) {
			if ( direction == 0 ) { // Move up
				new_y = this.getY() - delta*SPEED;
			} else if ( direction == 1 ) { // Move down
				new_y = this.getY() + delta*SPEED;
			} else if ( direction == 2 ) { // Move left
				new_x = this.getX() - delta*SPEED;
			} else if ( direction == 3 ) { // Mover right
				new_x = this.getX() + delta*SPEED;
			} else if ( direction == 4 ) { // Move up-right
				new_x = this.getX() + delta*SPEED;
				new_y = this.getY() - delta*SPEED;
			} else if ( direction == 5 ) { // Move down-right
				new_x = this.getX() + delta*SPEED;
				new_y = this.getY() + delta*SPEED;
			} else if ( direction == 6 ) { // Move down-left
				new_x = this.getX() - delta*SPEED;
				new_y = this.getY() + delta*SPEED;
			} else if ( direction == 7 ) { // Move up-left
				new_x = this.getX() - delta*SPEED;
				new_y = this.getY() - delta*SPEED;
			} else {
				// Do nothing
			}
			
			// Check and set the new positions
	        if(!world.terrainBlocks(new_x,this.getY())){
	        	this.setX( new_x );
	        }if(!world.terrainBlocks(this.getX(),new_y)){
	        	this.setY( new_y );
	        }
			
			

		} else {
			direction = Math.abs(r.nextInt() % 8);
			
			// Reset the timer
			time = 0;
		}
	}
	/** Moves away from the player if it is hit.
	 * @param player The player to move away from.
	 * @param world The world the monster is in. (to check terrain blocking)
	 * @param delta The time that has passed since the last frame. (milliseconds)
	 */
	public void moveIfHit( Player player, World world, int delta ) {
		
		this.setAmount( SPEED*delta );
		
		setTimeHit(getTimeHit() + delta/1000.0);
		
		this.calcDxAndDy( player );
		
		double new_x = 0;
		double new_y = 0;
		
		// Calculate the new co-ordinates to run away from the player
		if ( player.getX() < this.getX() )
			new_x =  this.getX() + this.getDX();
		if ( player.getX() > this.getX() ) 
			new_x =  this.getX() - this.getDX();
		if ( player.getY() < this.getY() )
			new_y = this.getY() + this.getDY();
		if ( player.getY() > this.getY() )
			new_y = this.getY() - this.getDY();
		
		// Check whether the next posiion is valid, then set it
        if(!world.terrainBlocks(new_x,this.getY())){
        	this.setX( new_x );
        }if(!world.terrainBlocks(this.getX(),new_y)){
        	this.setY( new_y );
        }
        
		
	}

	/** Returns whether the monster was hit */
	public boolean isIfHit() {
		return ifHit;
	}
	/** Sets the boolean of the ifHit.
	 * @param ifHit True if hit, else false.
	 */
	public void setIfHit(boolean ifHit) {
		this.ifHit = ifHit;
	}
	/** The time elapsed after being hit */
	public double getTimeHit() {
		return timeHit;
	}
	/** Sets the time after being hit.
	 * @param timeHit The time be set after being hit.
	 */
	public void setTimeHit(double timeHit) {
		this.timeHit = timeHit;
	}

}
