import java.awt.Color;
import java.awt.Point;

public class PlayerBlock {

	public static int SIZE = 50;

	private Point worldCoords;
	private Point pixelCoords;
	private Color color;

	PlayerBlock(int worldX, int worldY) {
		this.worldCoords = new Point(worldX, worldY);
		this.pixelCoords = new Point(worldX * SIZE, worldY * SIZE);
	}

	PlayerBlock(int worldX, int worldY, Color color) {
		this.worldCoords = new Point(worldX, worldY);
		this.pixelCoords = new Point(worldX * SIZE, worldY * SIZE);
		this.color = color;
	}

	public Point getWorldCoords() {
		return worldCoords;
	}

	public Point getPixelCoords() {
		return pixelCoords;
	}

	public Color getColor() {
		return color;
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
	
	@Override
	public int hashCode() {
		return worldCoords.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof PlayerBlock))
			return false;
		return worldCoords.equals(((PlayerBlock) obj).getWorldCoords());
	}
}
