import javafx.scene.paint.Color;

public class ColorGroup extends Landmark {
	
	private int price;
	private boolean toBeImproved = true;
	private int buildingCost;
	private static int availableHouseNumber = 32;
	private static int availableHotelNumber = 12;
	private int buildingNumber = 0;
	private int[] rents;
	private boolean toBeUnimproved = false;
	private boolean mortgaged = false;
	private int ownerID = -1;
	private static int[] groupOwnerIDs = {-1, -1, -1, -1, -1, -1, -1, -1};
	private final int[][] groupLocations = {{1, 3}, {6, 8, 9}, {11, 13, 14}, {16, 18, 19}, {21, 23, 24}, {26, 27, 29}, {31, 32, 34}, {37, 39}};
	
	public ColorGroup(String aDescription, Color aColor, int aPrice, int someBuildingCost, int[] someRents) {
		super(aDescription, aColor);
		setPrice(aPrice);
		setBuildingCost(someBuildingCost);
		setRents(someRents);
	}
	
	public void setPrice(int aPrice) {
		price = aPrice;
	}
	
	public int getPrice() {
		return price;
	}
	
	public void setToBeImproved(boolean whetherToBeImproved) {
		toBeImproved = whetherToBeImproved;
	}
	
	public boolean getToBeImproved() {
		return toBeImproved;
	}
	
	public void setBuildingCost(int someBuildingCost) {
		buildingCost = someBuildingCost;
	}
	
	public int getBuildingCost() {
		return buildingCost;
	}
	
	public void addAvailableHouseNumber() {
		availableHouseNumber++;
	}
	
	public void reduceAvailableHouseNumber() {
		availableHouseNumber--;
	}
	
	public int getAvailableHouseNumber() {
		return availableHouseNumber;
	}
	
	public void addAvailableHotelNumber() {
		availableHotelNumber++;
	}
	
	public void reduceAvailableHotelNumber() {
		availableHotelNumber--;
	}
	
	public int getAvailableHotelNumber() {
		return availableHotelNumber;
	}
	
	public void addBuildingNumber() {
		buildingNumber++;
	}
	
	public void reduceBuildingNumber() {
		buildingNumber--;
	}
	
	public int getBuildingNumber() {
		return buildingNumber;
	}
	
	public void setRents(int[] someRents) {
		rents = someRents;
	}
	
	public int[] getRents() {
		return rents;
	}
	
	public void setToBeUnimproved(boolean whetherToBeUnimproved) {
		toBeUnimproved = whetherToBeUnimproved;
	}
	
	public boolean getToBeUnimproved() {
		return toBeUnimproved;
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
		groupOwnerIDs[getGroupID(aLocation)] = aGroupOwnerID;
	}
	
	public int getGroupOwnerID(int aLocation) {
		return groupOwnerIDs[getGroupID(aLocation)];
	}
	
	public int[] getGroupLocations(int aLocation) {
		return groupLocations[getGroupID(aLocation)];
	}
	
	public int getGroupID(int aLocation) {
		int groupID = 0;
		loop:
		for(int i = 0; i < groupLocations.length; i++) {
				for(int j = 0; j < groupLocations[i].length; j++) {
					if(groupLocations[i][j] == aLocation) {
						groupID = i;
						break loop;
					}
				}
		}
		return groupID;
	}
}
