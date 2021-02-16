import java.util.ArrayList;
import java.util.Collections;

import javafx.scene.image.ImageView;

public class Player {
	
	private String username;
	private int id;
	private ImageView token;
	private static int currentPlayerIndex = 0;
	private int inJailNumber = -1;
	private static int outOfJailCOwnerID = -1;
	private static int outOfJailCCOwnerID = -1;
	private boolean justOutOfJail = false;
	private int position = 0;
	private int money = 1500;
	private ArrayList<Integer> propertyIDs = new ArrayList<Integer>();
	
	public Player(String aUsername, int aID, ImageView aToken) {
		setUsername(aUsername);
		setID(aID);
		setToken(aToken);		
	}
	
	public void setUsername(String aUsername) {
		username = aUsername;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setID(int aID) {
		id = aID;
	}
	
	public int getID() {
		return id;
	}
	
	public void setToken(ImageView aToken) {
		token = aToken;
		token.setPreserveRatio(false);
		token.setFitWidth(30);
		token.setFitHeight(30);
	}
	
	public ImageView getToken() {
		return token;
	}
	
	public void setNextPlayerIndex(int aPlayersSize) {
		currentPlayerIndex = ++currentPlayerIndex % aPlayersSize;
	}
	
	public int getCurrentPlayerIndex() {
		return currentPlayerIndex;
	}
	
	public void setInJailNumber() {
		inJailNumber = -1;
	}
	
	public void addInJailNumber() {
		inJailNumber++;
	}
	
	public int getInJailNumber() {
		return inJailNumber;
	}
	
	public void setOutOfJailCOwnerID(int anOwnerID) {
		outOfJailCOwnerID = anOwnerID;
	}
	
	public int getOutOfJailCOwnerID() {
		return outOfJailCOwnerID;
	}
	
	public void setOutOfJailCCOwnerID(int anOwnerID) {
		outOfJailCCOwnerID = anOwnerID;
	}
	
	public int getOutOfJailCCOwnerID() {
		return outOfJailCCOwnerID;
	}
	
	public void setJustOutOfJail(boolean whetherJustOutOfJail) {
		justOutOfJail = whetherJustOutOfJail;
	}
	
	public boolean getJustOutOfJail() {
		return justOutOfJail;
	}
	
	public void setPosition(int aPosition) {
		if(aPosition < getPosition() && getInJailNumber() == -1) {
			addMoney(200);
		}
		position = aPosition;
	}
	
	public void moveForward(int steps) {
		if(getPosition() + steps >= 40) {
			addMoney(200);
		}
		position = (getPosition() + steps) % 40;
		
	}
	
	public void moveBackward(int steps) {
		position = getPosition() - steps;
	}
	
	public int getPosition() {
		return position;
	}
	
	public void addMoney(int someMoney) {
		money += someMoney;
	}
	
	public void reduceMoney(int someMoney) {
		money -= someMoney;
	}
	
	public int getMoney() {
		return money;
	}
	
	public void addPropertyID(int aPropertyID) {
		propertyIDs.add(aPropertyID);
		Collections.sort(propertyIDs);
	}
	
	public void removePropertyID(int aPropertyID) {
		propertyIDs.remove(propertyIDs.indexOf(aPropertyID));
	}
	
	public ArrayList<Integer> getPropertyIDs() {
		ArrayList<Integer> tempPropertyIDs = new ArrayList<>(propertyIDs);
		return tempPropertyIDs;
	}
}
