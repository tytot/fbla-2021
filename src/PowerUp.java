import java.awt.Image;

public abstract class PowerUp extends MapBlock {
	
	private Image img;
	
	PowerUp() {
		img = ImageFactory.fetchImage(getImagePath());
	}
	
	public abstract String getImagePath();
	
	public boolean isSolid() {
		return false;
	}
	
	public Image getImage() {
		return img;
	}
}
