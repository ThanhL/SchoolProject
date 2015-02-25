import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;
/** Represents all the entities in the wilderness (the monsters) in the game world.
 * @author Thanh
 *
 */
public class Wilderness {
	
	// Array to store all the Passive Monsters
	private ArrayList<PassiveMonster> passiveMonsters;
	
	// Array to store all the Agressive Monsters
	private ArrayList<AggressiveMonster> aggressiveMonsters;
	
	//BufferReader to read the data of NPC locations
	private BufferedReader inFile;
	
	/** Creates a new Wilderness object */
	public Wilderness() throws SlickException {
		
		passiveMonsters = new ArrayList<PassiveMonster>();
		aggressiveMonsters = new ArrayList<AggressiveMonster>();
		
		//Adding all the Passive Monsters
		addAllEntities( "data/GiantBat.txt" , "Giant Bat" );
		
		//Adding all the Aggressive Monsters
		addAllEntities( "data/Skeleton.txt", "Skeleton" );
		addAllEntities( "data/Bandit.txt", "Bandit" );
		addAllEntities( "data/Zombie.txt", "Zombie" );
		addAllEntities( "data/Draelic.txt", "Draelic" );
	
	}
	/** Gets the array of all the passive monsters */
	public ArrayList<PassiveMonster> getPassiveMonster() { 
		return passiveMonsters;
	}
	/** Gets the array of all the aggressive monsters */
	public ArrayList<AggressiveMonster> getAggressiveMonster() {
		return aggressiveMonsters;
	}
	
	/** Reads a dataFile of all the positions of a certain monster and adds it to the game world
	 * @param data The data to be read from. Contains all the positions of a certain type of monster.
	 * @param monster The type of monster to be added.
	 */
	public void addAllEntities( String data, String monster ) throws SlickException {
		try
		{
			String lineRead;
			
			FileReader fr = new FileReader ( data );
			
			inFile = new BufferedReader (fr);

			// Read a line from the file
			lineRead = inFile.readLine();

			while (lineRead != null)
			{
				StringTokenizer tokens = new StringTokenizer( lineRead, ", "  );
				
				// Starting locations of the monster
				int x = 0;
				int y = 0;
				
				//This variable is used to determine which variable to assign to
				int tokenNum = 0; 
				
				while ( tokens.hasMoreTokens() ) {	
					try {
						// Assign the locations of the monster accordingly
						if ( tokenNum == 0 ) {
							x = Integer.parseInt( tokens.nextToken() ); 
						} else { 
							y = Integer.parseInt( tokens.nextToken() );
						}
						tokenNum++;			
					} catch( NumberFormatException e) {
						// If its not an int, continue
						continue;
					}
					
				}
				
				addMonster( monster, x, y);
				
				tokenNum = 0;
				lineRead = inFile.readLine(); 
			}
		}
		catch (FileNotFoundException exception)
		{
			System.out.println ("The file " + " was not found");
		}	
		catch (IOException exception)
		{
			System.out.println (exception);
		}
	}
	
	/** Add the monster type to the appropiate arrayList
	 * @param monster The name of the monster to be added.
	 * @param x The location of the monster in x. (pixels)
	 * @param y The location of the monster in y. (pixels)
	 */
	public void addMonster( String monster, double x, double y ) throws SlickException {
		
		if ( monster.equals( "Giant Bat") ) {
			passiveMonsters.add( PassiveMonster.addBat( x, y ) );
		} else if ( monster.equals( "Zombie" ) ) {
			aggressiveMonsters.add( AggressiveMonster.addZombie( x, y) );
		} else if ( monster.equals( "Bandit" ) ) {
			aggressiveMonsters.add( AggressiveMonster.addBandit( x, y) );
		} else if ( monster.equals( "Skeleton" ) ) {
			aggressiveMonsters.add( AggressiveMonster.addSkeleton( x, y) );
		} else if ( monster.equals( "Draelic" ) ) {
			aggressiveMonsters.add( AggressiveMonster.addDraelic( x, y) );
		} else {
			System.out.println("Monster not in database");
		}
	}
	

	
	/** Render all the monsters in the game world
     * @param g The current Graphics context.
     * @param cam_x Camera x position in pixels.
     * @param cam_y Camera y position in pixels.
	 */
	public void renderMonsters( Graphics g, int cam_x, int cam_y ) {
		for ( PassiveMonster pM:passiveMonsters ) {
			pM.render(g, cam_x, cam_y);	
			
			pM.renderPanel(g, cam_x, cam_y);
		}
		for ( AggressiveMonster aM:aggressiveMonsters ) {
			aM.render(g, cam_x, cam_y);
			
			aM.renderPanel(g, cam_x, cam_y);
		}
	}
	
	/** Update all the monsters with respect to the players actions
	 * @param player The player in the game world.
	 * @param world The world the monsters and players are in.
	 * @param delta Time passed since the last frame. (milliseconds)
	 */
	public void update( Player player, World world, int delta ) {
		
		for ( int i = 0; i < aggressiveMonsters.size(); i++ ) {
			
			// assign aggressiveMonster to aM
			AggressiveMonster aM = aggressiveMonsters.get(i);
			
			// First check if the monster is still alive
			if (aM.getHP() == 0 ) {
				// If it's dead remove the monster from the game world
				aggressiveMonsters.remove(i); 
				i--; // decrement array
			} else { // Update the monster's game state
				
				// attack the player if the player is near
				aM.MoveAttackPlayer( player, world );
				// Update the CoolDownTimer of the aggressive Monster
				aM.updateCoolDownTimer(delta);
				
			}
		}
		
		for ( int i = 0; i < passiveMonsters.size(); i++ ) {
			
			// assign passiveMonster to pM
			PassiveMonster pM = passiveMonsters.get(i);
			
			// First Check if the monster is still alive
			if ( pM.getHP() == 0 ) {
				// If it's dead remove the monster from the game world
				passiveMonsters.remove(i); 
				i--; // decrement array
			} else if ( pM.isIfHit() && pM.getTimeHit() <= PassiveMonster.TIME_RUNAWAY) {
				// If the monster is hit by the player, runaway from the player for TIME_RUNAWAY seconds
				pM.moveIfHit(player, world, delta);
				
			} else { // Else move wander the map randomly
				pM.moveRandom(player, world, delta);
				
				// Reset the status of the Passive monster after the monster is Hit
				pM.setIfHit( false );
				pM.setTimeHit( 0 );
			}

		}
		
		
	}
}
