import java.util.Random;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
/** Represents all of the character attributes of all the entities in the game world
 * that is in common
 * @author Thanh
 */
public class Character {
	// Location of Characters
	private double x;
	private double y;
	
	private Random r = new Random(); // Used to pick a random attack damage
	
	// Damage and Cooldown Attributes
	private double MaxDmg;
	private double coolDown;
	private double coolDownTimer;
	
	// Health stats
	private double MaxHp;
	private double HP;
	
	// Name of unit
	private String name;
	
	

	/** Character constructor
	 * @param HP The health of the character.
	 * @param dmg The maximum damage of the character.
	 * @param coolDown The maximum cooldown of the character.
	 */
	public Character( double HP, double dmg, double coolDown ) {
		this.HP = HP;
		this.MaxHp = HP;
		this.MaxDmg = dmg;
		this.coolDown = coolDown;
	}
	
	/** Character constructor
	 * @param x The x starting location.
	 * @param y The y starting location.
	 * @param name The name of the character.
	 */
	public Character( double x, double y, String name ) {
		this.x = x;
		this.y = y;
		this.name = name;
	}
	
	// Getters and setters for each attribute
	 /** The x coordinate of the character (pixels). */
	public double getX() {
		return x;
	}
	 /** Set the x coordinate of the character (pixels). */
	public void setX(double x) {
		this.x = x;
	}
	 /** The y coordinate of the character (pixels). */
	public double getY() {
		return y;
	}
	 /** Set the y coordinate of the character (pixels). */
	public void setY(double y) {
		this.y = y;
	}
	/** Returns the maximum damage */
	public double getMaxDmg() {
		return MaxDmg;
	}
	/** Set the maximum damage that the character can do.
	 * @param maxDmg The maximum damage to be set.
	 */
	public void setMaxDmg(double maxDmg) {
		MaxDmg = maxDmg;
	}
	/** Gets the random damage from (0 - maximum damage that the character can do)
	 * @return Gets the damage the character does. (gets a random number to make things interesting..)
	 */
	public double getDmg() {
		return Math.abs(r.nextInt() % MaxDmg); // Get random attack damage ( 0 to Max Damage inclusive )
	}	
	/** Get the maximum cooldown time */
	public double getCoolDown() {
		return coolDown;
	}
	/** Set the maximum cooldown time for the character
	 * @param coolDown The time of the max cooldown to be set.
	 */
	public void setCoolDown(double coolDown) {
		this.coolDown = coolDown;
	}
	/** The maximum health points the character has */
	public double getMaxHp() {
		return MaxHp;
	}
	/** The maximum HP to be set.
	 * @param maxHp The maximum health of the character.
	 */
	public void setMaxHp(double maxHp) {
		MaxHp = maxHp;
	}
	
	public double getHP() {
		return HP;
	}
	/** Set the HP of the character
	 * @param hP The health point to be set.
	 */
	public void setHP(double hP) {
		HP = hP;
	}
	
	/** Get the name of the character */
	public String getName() {
		return name;
	}
	/** Set the name of the character.
	 * @param name The, er.. name.
	 */
	public void setName(String name) {
		this.name = name;
	}
	/** The cooldown time (seconds) */ 
	public double getCoolDownTimer() {
		return coolDownTimer;
	}
	/** Set the cooldown time on the character
	 * @param coolDownTimer The time of the cooldown to be set.
	 */
	public void setCoolDownTimer(double coolDownTimer) {
		this.coolDownTimer = coolDownTimer;
	}
	
	// Methods for Character Class
	
	/** Updates the characters HP after recieving a certain amount of damage.
	 * @param dmg The damage that character has recieved.
	 */
	public void updateHP( double dmg ) {
		double HP = this.getHP() - dmg;
		if ( HP < 0 ) {
			this.setHP(0);
		} else {
			this.setHP(HP);
		}

	}
	
	/** Updates the character's cooldown timer.
	 * @param delta Time passed since last frame (milliseconds).
	 */
	public void updateCoolDownTimer( int delta ) {
		coolDownTimer -= delta;
		if ( coolDownTimer <= 0 ) 
			coolDownTimer = 0;

	}
	
    /** Renders the character's status panel.
     * @param g The current Slick graphics context.
     */
	public void renderPanel( Graphics g, int cam_x, int cam_y ) {
		
        Color VALUE = new Color(1.0f, 1.0f, 1.0f);          // White
        Color BAR_BG = new Color(0.0f, 0.0f, 0.0f, 0.8f);   // Black, transp
        Color BAR = new Color(0.8f, 0.0f, 0.0f, 0.8f);      // Red, transp
		
        // Variables for layout
        String text;                // Text to display
        int text_x, text_y;         // Coordinates to draw text
        int bar_x, bar_y;           // Coordinates to draw rectangles
        int bar_width, bar_height;  // Size of rectangle to draw
        int hp_bar_width;           // Size of red (HP) rectangle


        float health_percent = (float)(this.getHP() / this.getMaxHp());       // Monster's and Villager's health, as a percentage
        text = this.name;
        
        int screen_x, screen_y;
        screen_x = (int) (this.x - cam_x);
        screen_y = (int) (this.y - cam_y);
        
        // Display NPC's health

        bar_x = (int)(screen_x - Math.round(72.0/2));
        bar_y = (int)(screen_y - 50);
        bar_width = g.getFont().getWidth(text)+6;
        bar_height = 20;
        hp_bar_width = (int) (bar_width * health_percent);
        text_x = bar_x + (bar_width - g.getFont().getWidth(text)) / 2;
        text_y = screen_y - 50;
        
        
        g.setColor(BAR_BG);
        g.fillRect(bar_x, bar_y, bar_width, bar_height);
        g.setColor(BAR);
        g.fillRect(bar_x, bar_y, hp_bar_width, bar_height);
        g.setColor(VALUE);
        g.drawString(text, text_x, text_y);
		
	}
	

	

}
