import javafx.scene.paint.Color;

public class Utility extends Landmark {
	
	private final int price = 150;
	private boolean mortgaged = false;
	private int ownerID = -1;
	private static int groupOwnerID = -1;
	private final int[] groupLocations = {12, 28};
	
	public Utility(String aDescription, Color aColor) {
		super(aDescription, aColor);
	}
	
	public int getPrice() {
		return price;
	}
	
	public void setMortgaged(boolean whetherMortgaged) {
		mortgaged = whetherMortgaged;
	}
	
	public boolean getMortgaged() {
		return mortgaged;
	}
	
	public void setOwnerID(int anOwnerID) {
		ownerID = anOwnerID;
	}
	
	public int getOwnerID() {
		return ownerID;
	}
	
	public void setGroupOwnerID(int aLocation, int aGroupOwnerID) {
		groupOwnerID = aGroupOwnerID;
	}
	
	public int getGroupOwnerID() {
		return groupOwnerID;
	}
	
	public int[] getGroupLocations(int aLocation) {
		return groupLocations;
	}
}
