
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import java.util.ArrayList;
/** Represents all the entities in the village.
 * @author Thanh
 *
 */
public class Village {
	
	// The village teleport locations when the player dies
	public static final int TELEPORT_X = 738;
	public static final int TELEPORT_Y = 549;
	
	// Array to store all the villagers
	private ArrayList<Villager> villagers;
	
	/** Creates the village in game world */
	public Village() throws SlickException {
		
		// Add all the villagers in the game world in this constructor
		villagers = new ArrayList<Villager>();
		
		// Prince Aldric
		addVillager( new Villager( RPG.ASSETS_PATH + "/units/prince.png", 467, 679, "Aldric" ) ); 
		// Garth
		addVillager( new Villager( RPG.ASSETS_PATH + "/units/peasant.png", 756, 870, "Garth" ) ); 
		// Elvira
		addVillager( new Villager( RPG.ASSETS_PATH + "/units/shaman.png", 738, 549, "Elvira" ) );
		
	}
	/** Adds a villager to village.
	 * @param vil Villager to be added.
	 */
	public void addVillager( Villager vil ) {
		villagers.add( vil );
	}
	/** Removes a villager to village.
	 * @param vil Villager to be removed.
	 */
	public void removeVillager( Villager vil ) {
		villagers.remove( vil );
	}
	
	/** Render all the villagers in the game world.
     * @param g The current Graphics context.
     * @param cam_x Camera x position in pixels.
     * @param cam_y Camera y position in pixels.
	 */
	public void renderVillagers( Graphics g, int cam_x, int cam_y ) {
		for ( Villager vil:villagers ) {
			vil.render( g, cam_x, cam_y );
			
			vil.renderPanel( g, cam_x, cam_y);
		}
	}
	
	/** Update the village gamestat according to the player's action.
	 * @param player The player in the world.
	 * @param world The world in which the entities are in.
	 * @param delta The time that has passed in milliseconds.
	 */
	public void update( Player player, World world, int delta ) {
		
		// Iterate through the villagers and update the game state according to player's actions
		for ( Villager vil:villagers ) {
			
			// Perform the action according to the villager
			vil.performAction( player, delta );
			
			
			
		}
		
	}

}
