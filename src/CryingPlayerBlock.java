import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class CryingPlayerBlock extends MapBlock {

	private Image img;
	
	public CryingPlayerBlock(int relativePosition) throws IOException {
		String path = "img/sprites/crying/crying" + ends[relativePosition] + ".png";
		img = ImageIO.read(new File(path));
	}
	
	public Image getImage() {
		return img;
	}
	
	@Override
	public boolean isSolid() {
		return true;
	}
	
}
