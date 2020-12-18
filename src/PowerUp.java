

import java.awt.Color;

public abstract class PowerUp extends MapBlock {
	
	public Color getColor() {
		return Color.BLACK;
	}

	public boolean isSolid() {
		return false;
	}
	
	public abstract String imagePath();
}
