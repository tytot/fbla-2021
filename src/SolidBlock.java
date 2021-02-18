
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class SolidBlock extends MapBlock {
	
	private Image img;
			
	public SolidBlock(Theme theme, int relativePosition) {
		String path = theme.getBlockImagePathStub();
		img = ImageFactory.fetchImage("img/sprites/" + path + "/" + path + ENDS[relativePosition] + ".png");
	}
	
	public Image getImage() {
		return img;
	}

	public boolean isSolid() {
		return true;
	}
}
