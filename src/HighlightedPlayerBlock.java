import java.awt.Image;

public class HighlightedPlayerBlock extends PlayerBlock {
	
	HighlightedPlayerBlock(int worldX, int worldY) {
		super(worldX, worldY);
	}
	
	HighlightedPlayerBlock(PlayerBlock other) {
		super(other);
	}

	@Override
	public String getImagePathPrefix() {
		return "img/sprites/crying/crying";
	}
}
