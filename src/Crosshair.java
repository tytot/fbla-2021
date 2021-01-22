
public class Crosshair extends PlayerBlock {
	
	Crosshair(int worldX, int worldY) {
		super(worldX, worldY);
		setRelativePosition(Block.ALONE);
	}
	
	public String getImagePathPrefix() {
		return "img/sprites/crosshair";
	}
}
