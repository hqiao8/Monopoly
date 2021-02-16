import javafx.scene.paint.Color;

public class Railroad extends Landmark {
	
	private final int price = 200;
	private final int[] rents = {25, 50, 100, 200};
	private boolean mortgaged = false;
	private int ownerID = -1;
	private static int groupOwnerID = -1;
	private final int[] groupLocations = {5, 15, 25, 35};
	
	public Railroad(String aDescription, Color aColor) {
		super(aDescription, aColor);
	}
	
	public int getPrice() {
		return price;
	}
	
	public int[] getRents() {
		return rents;
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
