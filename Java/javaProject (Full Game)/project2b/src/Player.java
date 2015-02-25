/* 433-294 Object Oriented Software Development
 * RPG Game Engine
 * Sample Solution
 * Author: Matt Giuca <mgiuca>
 */

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/** The character which the user plays as.
 */
public class Player extends Character
{
    private Image img = null;
    private Image img_flipped = null;

    // In pixels
    private double x;
    private double y;
    private boolean face_left = false;

    // Pixels per millisecond
    private static final double SPEED = 0.55;
    
    // Panel to render the status bar of the player
    private Image panel = new Image( RPG.ASSETS_PATH + "/panel.png" );
    
    // The inventory of the player
    private Inventory inventory;
    

    /** The x coordinate of the player (pixels). */
    public double getX()
    {
        return x;
    }

    /** The y coordinate of the player (pixels). */
    public double getY()
    {
        return y;
    }

    /** Creates a new Player.
     * @param image_path Path of player's image file.
     */
    public Player(String image_path)
        throws SlickException
    {

    	super( 100, 26, 600 );
    	
        // Start off at (756,684)
        img = new Image(image_path);
        img_flipped = img.getFlippedCopy(true, false);
        this.x = 756;
        this.y = 684;
        
        // Inventory for the player
        inventory = new Inventory();
    }

    // Methods for Player class
    
    /** Move the player in a given direction.
     * Prevents the player from moving outside the map space, and also updates
     * the direction the player is facing.
     * @param world The world the player is on (to check blocking).
     * @param dir_x The player's movement in the x axis (-1, 0 or 1).
     * @param dir_y The player's movement in the y axis (-1, 0 or 1).
     * @param delta Time passed since last frame (milliseconds).
     */
    public void move(World world, double dir_x, double dir_y, double delta)
    {
        // Update player facing based on X direction
        if (dir_x > 0)
            this.face_left = false;
        else if (dir_x < 0)
            this.face_left = true;

        // Move the player by dir_x, dir_y, as a multiple of delta * speed
        double new_x = this.x + dir_x * delta * SPEED;
        double new_y = this.y + dir_y * delta * SPEED;


        if(!world.terrainBlocks(new_x,this.y)){
            this.x = new_x;
        }if(!world.terrainBlocks(this.x,new_y)){
            this.y = new_y;
        }
   
    }
    
    /** Checks if the player has pressed the attack button. If so, update a specific monsters health accordingly
     * if the player is close to that monster.
     * @param pM The array of passive monster's in the world. (to check if the player has hit this type of monster) 
     * @param aM The aray of aggressive monster's in the world.(to check if the player has hit this type of monster)
     * @param attack Check if the player has attacked.
     */
    public void checkAttack( ArrayList<PassiveMonster> pM, ArrayList<AggressiveMonster> aM, int attack ) {
    	
    	// Pixel Range that is okay for an attack
    	int pixel = 50;
    	
    	// If attack key is pressed
    	if (attack > 0) {
    		
    		// Iterate through all the monsters and see if the attack hits the monster
    		for ( int i = 0; i < pM.size(); i++ ) {
    			PassiveMonster passM = pM.get(i);
    			double disX = Math.abs( passM.getX() - this.x );
    			double disY = Math.abs( passM.getY() - this.y );
    			
    			// Check if monster is in range and coolDown is 0ms
    			if ( disX <= pixel && disY <= pixel && (this.getCoolDownTimer() == 0) ) {
    				passM.updateHP( this.getDmg() ); // Update the monster's HP
    				passM.setIfHit( true ); // The Passive Monster is hit, now the movement of it has changed
    				
    	    		// Since attack, coolDownTimer is now set
    	    		this.setCoolDownTimer( this.getCoolDown() );
    			}
    		}
    		
    		for ( int i = 0; i < aM.size(); i++ ) {
    			AggressiveMonster aggressiveM = aM.get(i);
    			double disX = Math.abs( aggressiveM.getX() - this.x );
    			double disY = Math.abs( aggressiveM.getY() - this.y );
    			
    			// Check if monster is in range and coolDown is 0ms
    			if ( disX <= pixel && disY <= pixel && (this.getCoolDownTimer() == 0) ) {
    				aggressiveM.updateHP( this.getDmg() ); // Update the monster's HP
    				
    	    		// Since attack, coolDownTimer is now set
    	    		this.setCoolDownTimer( this.getCoolDown() );
    			}
    		}
    		

    	}
    	
    }
    
    /** Check if the player is still alive. If the player is dead, 
     * the player is teleported back to Village location at (TELEPORT_X, TELEPORT_X)
     * 
     */
    public void isDead() {
    	if ( this.getHP() == 0 ) {
    		
    		// Teleport the player back to the village
    		this.x = Village.TELEPORT_X;
    		this.y = Village.TELEPORT_Y;
    		
    		// Reset the player HP
    		this.setHP( this.getMaxHp() );
    		
    	}
    	
    }
    
    /** Add item to the player's inventory.
     * @param item The item to be added into the player's inventory.
     */
    public void addItem( Item item ) {
    	inventory.addItem( item );
    }
 
    /** Remove item from the player's inventory
     * @param ItemName The item to be removed from the players inventory.
     */
    public void removeItem( String ItemName ) {
    	
    	// Look where the item is 
    	for ( int i = 0; i < inventory.getInventory().size(); i++ ) {
    		Item item = inventory.getInventory().get(i);
    		
    		// If found the item, remove it from the player's inventory
    		if ( item.getItemName().equals( ItemName ) ) {
    			
    			// Remove item
    			inventory.removeItem( item );
    			i--; // Decrement array
    		}
    	}
    }
    
    /** Check if the item is in the player's inventory
     * @param item The item to be checked for in the player's inventory.
     * @return Return true if the item is in the player's inventory, otherwise false.
     */
    public boolean ItemCheck( String item ) {
    	for ( Item PlayerItem:inventory.getInventory() ) {
    		if ( PlayerItem.getItemName().equals(item) ) {
    			return true;
    		}
    	}
    	return false;
    }

    
    
    /** Draw the player to the screen at the correct place.
     * Method also renders the panel for the player as well.
     * @param g The current Graphics context.
     * @param cam_x Camera x position in pixels.
     * @param cam_y Camera y position in pixels.
     */
    public void render(Graphics g, int cam_x, int cam_y)
    {
        Image which_img;
        // Calculate the player's on-screen location from the camera
        int screen_x, screen_y;
        screen_x = (int) (this.x - cam_x);
        screen_y = (int) (this.y - cam_y);
        which_img = this.face_left ? this.img_flipped : this.img;
        which_img.drawCentered(screen_x, screen_y);
        
        renderPanel( g );
        
    }
    
    /** Renders the player's status panel.
     * @param g The current Slick graphics context.
     */
    public void renderPanel(Graphics g)
    {
        // Panel colours
        Color LABEL = new Color(0.9f, 0.9f, 0.4f);          // Gold
        Color VALUE = new Color(1.0f, 1.0f, 1.0f);          // White
        Color BAR_BG = new Color(0.0f, 0.0f, 0.0f, 0.8f);   // Black, transp
        Color BAR = new Color(0.8f, 0.0f, 0.0f, 0.8f);      // Red, transp

        // Variables for layout
        String text;                // Text to display
        int text_x, text_y;         // Coordinates to draw text
        int bar_x, bar_y;           // Coordinates to draw rectangles
        int bar_width, bar_height;  // Size of rectangle to draw
        int hp_bar_width;           // Size of red (HP) rectangle
        int inv_x, inv_y;          // Coordinates to draw inventory item

        float health_percent;       // Player's health, as a percentage

        // Panel background image
        panel.draw(0, RPG.screenheight - RPG.PANELHEIGHT);

        // Display the player's health
        text_x = 15;
        text_y = RPG.screenheight - RPG.PANELHEIGHT + 25;
        g.setColor(LABEL);
        g.drawString("Health:", text_x, text_y);
        text = String.valueOf( (int)(this.getHP()) ) + "/" + String.valueOf( (int)(this.getMaxHp()) );                                 // TODO: HP / Max-HP ( CHECK )

        bar_x = 90;
        bar_y = RPG.screenheight - RPG.PANELHEIGHT + 20;
        bar_width = 90;
        bar_height = 30;
        
        
        //health_percent = 0.75f;                         // TODO: HP / Max-HP ( CHECK )
        health_percent = (float)(this.getHP() / this.getMaxHp()) ;
        
        hp_bar_width = (int) (bar_width * health_percent);
        text_x = bar_x + (bar_width - g.getFont().getWidth(text)) / 2;
        g.setColor(BAR_BG);
        g.fillRect(bar_x, bar_y, bar_width, bar_height);
        g.setColor(BAR);
        g.fillRect(bar_x, bar_y, hp_bar_width, bar_height);
        g.setColor(VALUE);
        g.drawString(text, text_x, text_y);

        // Display the player's damage and cooldown
        text_x = 200;
        g.setColor(LABEL);
        g.drawString("Damage:", text_x, text_y);
        text_x += 80;
        text = String.valueOf( (int)(this.getMaxDmg()) );                                    // TODO: Damage ( CHECK )
        g.setColor(VALUE);
        g.drawString(text, text_x, text_y);
        text_x += 40;
        g.setColor(LABEL);
        g.drawString("Rate:", text_x, text_y);
        text_x += 55;
        text = String.valueOf( (int)(this.getCoolDown()) );                                    // TODO: Cooldown ( CHECK )
        g.setColor(VALUE);
        g.drawString(text, text_x, text_y);

        // Display the player's inventory
        g.setColor(LABEL);
        g.drawString("Items:", 420, text_y);
        bar_x = 490;
        bar_y = RPG.screenheight - RPG.PANELHEIGHT + 10;
        bar_width = 288;
        bar_height = bar_height + 20;
        g.setColor(BAR_BG);
        g.fillRect(bar_x, bar_y, bar_width, bar_height);

        inv_x = 490;
        inv_y = RPG.screenheight - RPG.PANELHEIGHT
            + ((RPG.PANELHEIGHT - 72) / 2);
        // for (each item in the player's inventory)                // TODO
        for ( Item item:inventory.getInventory() ) 
        {
            // Render the item to (inv_x, inv_y)
        	item.getImage().draw( (int)inv_x, (int)inv_y );
            inv_x += 72;
        }
    }
}
