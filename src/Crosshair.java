import java.awt.Image;

public class Crosshair extends PlayerBlock {
	
	Crosshair(int worldX, int worldY) {
		super(worldX, worldY);
	}
	
	@Override
	public Image getImage() {
		return ImageFactory.fetchImageBilinear("img/sprites/crosshair.png");
	}
}
