import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;


public class AggressiveMonster extends Monster {
	
	private Image img;
	
	/** Create a new Aggressive Monster */
	public AggressiveMonster( String img_path, double x, double y, String name ) throws SlickException {
		super( x, y, name );
		img = new Image( img_path );	
		
	}
	
	// Methods for AggressiveMonster class
	
	/** Adds a zombie entity into the world. Sets the stats for the zombie entity as well.	
	 * @param x The starting location in the x direction.
	 * @param y The starting locating in the y direction.
	 * @return  Returns the zombie entity to be added.
	 */
	public static AggressiveMonster addZombie( double x, double y) throws SlickException {
		// Create Zombie Entity
		AggressiveMonster zombie = new AggressiveMonster( RPG.ASSETS_PATH + "/units/zombie.png", x, y, "Zombie");
		
		// Stats for the Zombie
		double HP = 60;
		double dmg = 10;
		double Cooldown = 800;
		
		//Now set its stats
		zombie.setMaxDmg( dmg );
		zombie.setHP( HP );
		zombie.setMaxHp( HP );
		zombie.setCoolDown( Cooldown );
		
		// Set the movement speed
		zombie.setAmount( 0.1 );
		
		return zombie;
		
	}
	/** Adds a bandit  entity into the world. Sets the stats for the bandit entity as well. 
	 * @param x The starting location in the x direction.
	 * @param y The starting locating in the y direction.
	 * @return  Returns the bandit entity to be added.
	 */
	public static AggressiveMonster addBandit( double x, double y) throws SlickException {
		// Create Bandit Entity
		AggressiveMonster bandit = new AggressiveMonster( RPG.ASSETS_PATH + "/units/bandit.png", x, y, "Bandit");
		
		// Stats for the Bandit
		double HP = 40;
		double dmg = 8;
		double Cooldown = 200;
		
		//Now set its stats
		bandit.setMaxDmg( dmg );
		bandit.setHP( HP );
		bandit.setMaxHp( HP );
		bandit.setCoolDown( Cooldown );
		
		// Set the movement speed
		bandit.setAmount( 0.25 );
		
		return bandit;
		
	}
	/** Adds a skeleton entity into the world. Sets the stats for the skeleton entity as well,
	 * @param x The starting location in the x direction.
	 * @param y The starting locating in the y direction.
	 * @return  Returns the skeleton entity to be added.
	 */
	public static AggressiveMonster addSkeleton( double x, double y) throws SlickException {
		// Create Skeleton Entity
		AggressiveMonster skeleton = new AggressiveMonster( RPG.ASSETS_PATH + "/units/skeleton.png", x, y, "Skeleton");
		
		// Stats for the Skeleton
		double HP = 100;
		double dmg = 16;
		double Cooldown = 500;
		
		//Now set its stats
		skeleton.setMaxDmg( dmg );
		skeleton.setHP( HP );
		skeleton.setMaxHp( HP );
		skeleton.setCoolDown( Cooldown );
		
		// Set the movement speed
		skeleton.setAmount( 0.3 );
		
		return skeleton;
		
	}
	/** Adds a draelic entity into the world. Sets the stats for the draelic entity as well,
	 * @param x The starting location in the x direction.
	 * @param y The starting locating in the y direction.
	 * @return  Returns the draelic entity to be added.
	 */
	public static AggressiveMonster addDraelic( double x, double y) throws SlickException {
		// Create Draelic Entity
		AggressiveMonster draelic = new AggressiveMonster( RPG.ASSETS_PATH + "/units/necromancer.png", x, y, "Draelic");
		
		// Stats for the Draelic
		double HP = 140;
		double dmg = 30;
		double Cooldown = 400;
		
		//Now set its stats
		draelic.setMaxDmg( dmg );
		draelic.setHP( HP );
		draelic.setMaxHp( HP );
		draelic.setCoolDown( Cooldown );
		
		// Set the movement speed
		draelic.setAmount( 0.5 );
		
		return draelic;
		
	}
	
	// Chase and Attack Player methods
	
    /** Draw the monster to the screen at the correct place.
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
	
	/** Check if the player is near
	 * @return True if the player is within 150 pixels of the monster. Otherwise, False.
	 */
	public boolean IfPlayerNear() {
		
		// Min Range for the monster to attack
		int pixel = 150;
		
		if ( this.getDistX() <= pixel && this.getDistY() <= pixel ) {
			return true;
		}
		return false;
	}
	
	/** Moves towards the player and attacks them if they are within a certain pixel range
	 * @param player The player to move to.
	 * @param world The world the monster is on. (To check blocking)
	 */
	public void MoveAttackPlayer( Player player , World world) {
		
		this.calcDxAndDy( player );
		
		if ( this.IfPlayerNear() ) {
			// new positions to move towards the player
			double new_x = 0;
			double new_y = 0;
			
			// Calculate the new co-ordinates to follow the player
			if ( player.getX() < this.getX() )
				new_x =  this.getX() - this.getDX();
			if ( player.getX() > this.getX() ) 
				new_x =  this.getX() + this.getDX();
			if ( player.getY() < this.getY() )
				new_y = this.getY() - this.getDY();
			if ( player.getY() > this.getY() )
				new_y = this.getY() + this.getDY();
			
			// Check whether or not the monster is in attacking range
			if ( this.getDistX() <= 50 && this.getDistY() <= 50 ) {
				
				// Check if the attack is valid
				if ( this.getCoolDownTimer() == 0 ) { 
					
					// Update player HP
					player.updateHP( this.getDmg() );
					// Update coolDownTimer
					this.setCoolDownTimer( this.getCoolDown() );
					
				}
			} else {
				// If not in range move towards the player
		        if(!world.terrainBlocks(new_x,this.getY())){
		        	this.setX( new_x );
		        }if(!world.terrainBlocks(this.getX(),new_y)){
		        	this.setY( new_y );
		        }
				
			}
		}
		
		
	}
	
}
