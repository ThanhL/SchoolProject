
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;


/** Represents all the methods and attributes of all the items in the game world.
 * @author Thanh
 *
 */
public class Item {
	
	// Item image
	private Image item;
	
	// Location of item
	private double locX;
	private double locY;
	
	// Name of item
	private String itemName;
	
	/** The item constructor.
	 * @param img_path The image of the item.
	 * @param name The name of the item.
	 * @param locX The location in the x-direction of the item. (pixels)
	 * @param locY The location in the y-direction of the item. (pixels)
	 */
	public Item( String img_path, String name, int locX, int locY ) throws SlickException {
		
		// Set the image of the item
		item = new Image( img_path );
		
		// Set location of item
		this.setLocX(locX);
		this.setLocY(locY);
		
		// Set item name
		this.setItemName(name);
	}
	

	
	// Getters and Setters for Item class
	
	/** The item name */
	public String getItemName() {
		return itemName;
	}
	/** Set the item name.
	 * @param itemName The name of the item to be set.
	 */
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	/** The location in the y-direction of the item. */
	public double getLocY() {
		return locY;
	}
	/** Set location in the y-direction of the item.
	 * @param locY The location in the y-direction to be set.
	 */
	public void setLocY(double locY) {
		this.locY = locY;
	}
	/** The location in the x-direction of the item. */
	public double getLocX() {
		return locX;
	}
	/** Set location in the x-direction of the item.
	 * @param locY The location in the x-direction to be set.
	 */
	public void setLocX(double locX) {
		this.locX = locX;
	}
	/** Gets the image of the item */
	public Image getImage() {
		return item;
	}
	
	// Methods for Item class
	
	/** Renders the  Item in the game world.
     * @param g The current Graphics context.
     * @param cam_x Camera x position in pixels.
     * @param cam_y Camera y position in pixels.
	 */
	public void renderItem( Graphics g, int cam_x, int cam_y ) {
		
        int screen_x, screen_y;
        screen_x = (int) (this.locX - cam_x);
        screen_y = (int) (this.locY - cam_y);		
		item.drawCentered( screen_x, screen_y );	
	}
	
	
	/** Add the item effect to the player.
	 * @param player The player that item affects.
	 */
	public void addItemEffect( Player player ) {
		
		if ( this.getItemName().equals( "Amulet of Vitality") ) {
			// Amulet gives a health boost on both MaxHP and HP
			int healthBoost = 80;
			player.setHP( player.getHP() + healthBoost );
			player.setMaxHp( player.getMaxHp() + healthBoost );
		} else if ( this.getItemName().equals( "Sword of Strength" ) ) {
			// Sword gives increased damage
			player.setMaxDmg( player.getMaxDmg() + 30 );
		} else if ( this.getItemName().equals( "Tome of Agility") ) {
			// Tome of agility reduces Cool Down
			int reduceCoolDown = 300;
			player.setCoolDown(  player.getCoolDown() - reduceCoolDown );
		} else {
			// Do nothing
		}
		
	}
	

}
