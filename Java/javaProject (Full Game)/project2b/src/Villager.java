
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Villager extends Character {
	
	// Image of the villager
	private Image img;
	
	// Dialogue of the villager
	private String dialogue = null;
	
	private static final int TIME_DELAY = 4; // Time to display a certain dialogue
	private double time;                     // Time elapsed since last action
	
	// Used to check whether the time of the current dialogue being displayed is still valid
	private boolean dialogueTime = false;    
	
	/** Village Constructor.
	 * @param img_path The image of the villager
	 * @param x The location of the villager in the x-direction. (pixels)
	 * @param y The location of the villager in the y-direction. (pixels)
	 * @param name The name of the villager.
	 */
	public Villager( String img_path, double x, double y, String name ) throws SlickException {
		super( x, y, name );
		img = new Image( img_path );
		
		// Stats for each villager (They have the same stats)
		setMaxDmg( 0 );
		setCoolDown( 0 );
		setHP( 1 );
		setMaxHp( 1 );
	
	}
	
	// Methods for Villager class
	
	/** If the player is near, perform an action depending on the NPC
	 * @param player The player in the game world.
	 * @param delta The time that has passed since the last frame (milliseconds)
	 */
	public void performAction( Player player, int delta ) {
		
		
		// If the dialogue still has time to be displayed
		if ( dialogueTime ) { 
			time += delta/1000.0; // Increment the time
			if ( time > TIME_DELAY ) { // Check whether it has been more than 4 secs
				
				// If so....
				time = 0;            // Reset the timer
				dialogue = null;     // Reset the dialogue
				dialogueTime = false;// Reset the check
				return;              // Exit out of method
			}
			return; // If the current dialogue still has time to be displayed, exit method (Don't get new dialogue)
		}
		
		
		// Else if there is no dialogue to be displayed, get new dialogue and perform a certain action
		
		// The distance from the player and the villager
		double distX = Math.abs( player.getX() - this.getX() );
		double distY = Math.abs( player.getY() - this.getY() );
		
		// If the player is near the villager, perform a certain action and get dialogue
		if ( distX <= 50 && distY <= 50 ) {
			
			// Since we are getting a new dialogue, start the dialogueTime by setting this to true
			dialogueTime = true;
			// check which villager it is first
			if ( this.getName().equals( "Aldric" ) ) {
				
				// if the Player is holding the elixir
				if ( player.ItemCheck( "Elixir of Life" ) ) {
					
					// Dialogue for finding the elixir
					dialogue = "The elixir! My father is cured! Thankyou!";
					// Remove item from the player's Inventory
					player.removeItem( "Elixir of Life" ); 
				} else { 
					dialogue = "Please seek out the Elixir of Life to cure the king.";
				}
				
			} else if ( this.getName().equals( "Garth") ) {
				if ( !player.ItemCheck( "Amulet of Vitality" ) ) {
					dialogue =  "Find the Amulet of Vitality, across the river to the west.";
				} else if ( !player.ItemCheck( "Sword of Strength" ) ) {
					dialogue = "Find the Sword of Strength - cross the river and back, on the east side.";
				} else if ( !player.ItemCheck( "Tome of Agility" ) ) {
					dialogue = "Find the Tome of Agility, in the Land of Shadows.";
				} else {
					dialogue = "You have found all the treasure I know of.";
				}
				
			} else if ( this.getName().equals( "Elvira" ) ) {
				if ( player.getHP() == player.getMaxHp() ) { // Is player on full health?
					dialogue = "Return to me if you ever need healing.";
				}
				
				if ( player.getHP() != player.getMaxHp() ) { // If player is injured
					// Heal the player to full health
					player.setHP( player.getMaxHp() );
					dialogue = "You're looking much healthier now.";
				}			
			}
		
		}

	}
	
	/** Render the villager with the appropiate dialogue.
     * @param g The current Graphics context.
     * @param cam_x Camera x position in pixels.
     * @param cam_y Camera y position in pixels.
	 */
	@Override
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


        float health_percent = (float)(this.getHP() / this.getMaxHp());       // Villager's health as a percentage
        text = this.getName();
        
        int screen_x, screen_y;
        screen_x = (int) (this.getX() - cam_x);
        screen_y = (int) (this.getY() - cam_y);
        
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
		
        // Display viallger dialogue if the player is near
        
        // Check if there is dialogue to be displayed
        if (dialogue != null) {
        
        // Variables for layout
        int dialogue_x, dialogue_y;                //Coordinates to draw dialogue
        int bar_xDialogue, bar_yDialogue;          //Coordinates to draw rectangle
        int bar_widthDialogue, bar_heightDialogue; //Size of rectangle
        
        
        bar_xDialogue = (int)(screen_x - Math.round( (g.getFont().getWidth(dialogue)+6)/2.0 ) );
        bar_yDialogue = (int)(screen_y - 70);
        bar_widthDialogue = g.getFont().getWidth(dialogue)+6;
        bar_heightDialogue = 20;
        dialogue_x = bar_xDialogue + (bar_widthDialogue - g.getFont().getWidth(dialogue)) / 2;
        dialogue_y = screen_y - 70;
        
        
        // Display dialogue bar
        g.setColor(BAR_BG);
        g.fillRect( bar_xDialogue, bar_yDialogue, bar_widthDialogue, bar_heightDialogue);
        g.setColor(VALUE);
        g.drawString(dialogue, dialogue_x, dialogue_y);
        }
       
	}
	
	/** Render the villager
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

	// Getters and Setters for the dialogue
	
	/** The dialogue of the villager */
	public String getDialogue() {
		return dialogue;
	}
	/** Sets the dialogue of the villager 
	 * @param dialogue The dialogue to be set.
	 */
	public void setDialogue(String dialogue) {
		this.dialogue = dialogue;
	}

}
