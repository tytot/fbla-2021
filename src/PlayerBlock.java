
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

	public int hashCode() {
		return worldCoords.hashCode();
	}

	public boolean equals(PlayerBlock other) {
		return worldCoords.equals(other.getWorldCoords());
	}
}
