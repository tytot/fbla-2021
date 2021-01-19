public class HighlightedPlayerBlock extends PlayerBlock {

	HighlightedPlayerBlock(int worldX, int worldY) {
		super(worldX, worldY);
	}

	HighlightedPlayerBlock(PlayerBlock other) {
		super(other);
	}

	public String getImagePathPrefix() {
		return "src/res/img/sprites/crying/crying";
	}
}
