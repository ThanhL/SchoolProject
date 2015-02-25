/** The methods and attributes common to all monsters */
public class Monster extends Character {
	
	// The distance from the monster to the player in pixels
	private double distX;
	private double distY;
	
	// The distance to move this frame
	private double dX;
	private double dY;
	
	// The amount the monster may move
	private double amount;
	
	/** Monster constructor
	 * @param x The location of the monster in the x-direction. (pixels)
	 * @param y The location of the monster in the y-direction. (pixels)
	 * @param name The name of the monster.
	 */
	public Monster( double x, double y, String name ) {
		super( x, y, name);
	}
	
	// Getters and Setters for distX and distY
	/** The distance from the monster to the player in the x-direction (pixels) */
	public double getDistX() {
		return distX;
	}
	/** Set the distance from the monster to the player in the x-direction (pixels) */
	public void setDistX(double distX) {
		this.distX = distX;
	}
	/** The distance from the monster to the player in the y-direction (pixels) */
	public double getDistY() {
		return distY;
	}
	/** Set the distance from the monster to the player in the y-direction (pixels) */
	public void setDistY(double distY) {
		this.distY = distY;
	}

	// Getters and Setters for disX and disY
	/** The distance to move this frame in the x-direction (pixels) */
	public double getDX() {
		return dX;
	}
	/** Set the distance to move this frame in the x-direction (pixels) */
	public void setDisX(double dX) {
		this.dX = dX;
	}
	/** The distance to move this frame in the y-direction (pixels) */
	public double getDY() {
		return dY;
	}
	/** Set the distance to move this frame in the y-direction (pixels) */
	public void setDY(double dY) {
		this.dY = dY;
	}
	
	// Getters and Setters for amount
	
	/** The amount that the monster may move (pixels) */
	public double getAmount() {
		return amount;
	}
	/** Set the amount the monster may move */
	public void setAmount(double amount) {
		this.amount = amount;
	}
	
	
	// Methods for Monster Class
	
	/** Calculate dX and dY.
	 * @param player The player to calculate the amount the monster may move to or from.
	 */
	public void calcDxAndDy( Player player ) {
		
		// Distance from the monster to the player
		this.distX = Math.abs( player.getX() - this.getX() );
		this.distY = Math.abs( player.getY() - this.getY() );
		
		double distTotal = Math.sqrt( (distX*distX) + (distY*distY) );
		
		this.dX = (distX/distTotal)*amount;
		this.dY = (distY/distTotal)*amount;
	}

}
