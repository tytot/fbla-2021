
import java.awt.Color;

public abstract class MapBlock {

	public static final int SIZE = 50;

	public abstract Color getColor();

	public abstract boolean isSolid();
}
