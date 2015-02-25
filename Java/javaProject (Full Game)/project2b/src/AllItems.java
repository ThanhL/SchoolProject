import java.util.ArrayList;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;


public class AllItems {
	
	// All the items in the world
	private ArrayList<Item> allItems;
	
	public AllItems() throws SlickException {
		
		allItems = new ArrayList<Item>();
		
		addItem( new Item( RPG.ASSETS_PATH + "/items/amulet.png", "Amulet of Vitality", 956, 3563) );
		addItem( new Item( RPG.ASSETS_PATH + "/items/sword.png", "Sword of Strength", 4791, 1253) );
		addItem( new Item( RPG.ASSETS_PATH + "/items/book.png", "Tome of Agility", 546, 6707) );
		addItem( new Item( RPG.ASSETS_PATH + "/items/elixir.png", "Elixir of Life", 1976, 402) );
	}
	
	// Methods for AllItems class
	
	// Add and Remove item from allItems
	public void addItem( Item item ) {
		allItems.add( item );
	}
	public void removeItem( Item item ) {
		allItems.remove( item );
	}
	

	// Render all items in the world
	public void renderAllItems( Graphics g, int cam_x, int cam_y ) {
		for ( Item item:allItems ) {
			item.renderItem( g, cam_x, cam_y );
		}
	}
	
	// Update the item's game state
	public void update( Player player, World world ) {
		
		// Iterate through the items and see if the player has picked it up yet
		for ( int i = 0; i < allItems.size(); i++ ){
			Item item = allItems.get(i);
			
			// The distance from the player and the item
			double distX = Math.abs( player.getX() - item.getLocX() );
			double distY = Math.abs( player.getY() - item.getLocY() );
			
			// If player is within range of the item, player picks it up
			if ( distX <= 50 && distY <= 50 ) {
				
				// Add the item to players inventory
				player.addItem( item );
				
				// Add the effect of the item
				item.addItemEffect( player );
				
				// Remove the item after pickup
				allItems.remove(i);
				i--; // decrement array
				
			}
		}
		
		
	}
	
	

}
