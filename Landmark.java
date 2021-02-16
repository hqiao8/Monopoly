import javafx.scene.paint.Color;

public class Landmark {
	
	private String description;
	private Color color;
	private int ownerID = -1;
	private int groupOwnerID = -1;
	
	public Landmark(String aDescription, Color aColor) {
		setDescription(aDescription);
		setColor(aColor);
	}
	
	public void setDescription(String aDescription) {
		description = aDescription;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setColor(Color aColor) {
		color = aColor;
	}
	
	public Color getColor() {
		return color;
	}
	
	public int getPrice() {
		return -1;
	}
	
	public boolean getMortgaged() {
		return false;
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
	
	public int[] getGroupLocations(int aLocation) {
		return new int[] {-1};
	}
}
