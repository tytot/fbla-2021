
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class SolidBlock extends MapBlock {

	private Image img;

	public SolidBlock(int relativePosition) throws IOException {
		String path = "src/res/img/sprites/stone/stone"
		+ ends[relativePosition] + ".png";
		img = ImageIO.read(new File(path));
	}

	public Image getImage() {
		return img;
	}

	public boolean isSolid() {
		return true;
	}
}
