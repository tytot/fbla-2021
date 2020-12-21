import java.awt.Color;

public class CryingPlayerBlock extends MapBlock {

	@Override
	public Color getColor() {
		return Color.ORANGE;
	}

	@Override
	public boolean isSolid() {
		return true;
	}
	
}
