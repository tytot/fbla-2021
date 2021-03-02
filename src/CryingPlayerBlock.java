import java.awt.Image;

public class CryingPlayerBlock extends MapBlock {
	
	private Image img;
	
	public CryingPlayerBlock(int relativePosition) {
		img = ImageFactory.fetchImage("img/sprites/crying/crying" + ENDS[relativePosition] + ".png");
	}
	
	@Override
	public Image getImage() {
		return img;
	}
	
	@Override
	public boolean isSolid() {
		return true;
	}
	
}
