import javafx.scene.paint.Color;

public class TaxSpace extends Landmark {
	
	private int penalties;
	
	public TaxSpace(String aDescription, Color aColor, int somePenalties) {
		super(aDescription, aColor);
		setPenalties(somePenalties);
	}
	
	public void setPenalties(int somePenalties) {
		penalties = somePenalties;
	}
	
	public int getPenalties() {
		return penalties;
	}
}
