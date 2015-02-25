import java.util.ArrayList;

/** The inventory of a character in the game world. (specifically for the player)
 * @author Thanh
 *
 */
public class Inventory {
	
	// All the items in the inventory
	private ArrayList<Item> inventory;
	
	/** Constructor of the inventory */
	public Inventory() {
		inventory = new ArrayList<Item>();
	}
	
	/** Add items to the player inventory
	 * @param item The item to be added.
	 */
	public void addItem( Item item ) {
		inventory.add( item );
	}
	
	/** Remove item to the player inventory
	 * @param item The item to be item.
	 */
	public void removeItem( Item item ) {
		inventory.remove( item );
	}
	
	/** get the inventory */
	public ArrayList<Item> getInventory() {
		return  inventory;
	}

}
