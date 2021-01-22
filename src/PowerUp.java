import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public abstract class PowerUp extends MapBlock {
	
	private Image img;
	
	PowerUp() throws IOException {
		img = ImageIO.read(new File(imagePath()));
	}
	
	public boolean isSolid() {
		return false;
	}
	
	public abstract String imagePath();
	
	public Image getImage() {
		return img;
	}
}
