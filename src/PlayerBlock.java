import java.awt.Image;
import java.awt.Point;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class PlayerBlock extends Block {
	
	private Point worldCoords;
	private Point pixelCoords;
	private int relativePosition;
	private boolean onGoalBlock = false;
	
	PlayerBlock(int worldX, int worldY) {
		this.worldCoords = new Point(worldX, worldY);
		this.pixelCoords = new Point(worldX * Block.SIZE, worldY * Block.SIZE);
	}
	
	PlayerBlock(PlayerBlock other) {
		this.worldCoords = new Point(other.getWorldCoords());
		this.pixelCoords = new Point(this.worldCoords.x * Block.SIZE, this.worldCoords.y * Block.SIZE);
		setRelativePosition(other.getRelativePosition());
	}

	public Point getWorldCoords() {
		return worldCoords;
	}

	public Point getPixelCoords() {
		return pixelCoords;
	}
	
	public int getRelativePosition() {
		return relativePosition;
	}
	
	public boolean isOnGoalBlock() {
		return onGoalBlock;
	}
	
	public void setOnGoalBlock(boolean onGoalBlock) {
		this.onGoalBlock = onGoalBlock;
	}

	public void setRelativePosition(int relativePos) {
		relativePosition = relativePos;
	}
	
	@Override
	public int hashCode() {
		return worldCoords.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof PlayerBlock))
			return false;
		return worldCoords.equals(((PlayerBlock) obj).getWorldCoords());
	}
	
	public String getImagePathPrefix() {
		if (!onGoalBlock) {
			return "img/sprites/player/body";
		}
		return "img/sprites/player/small/body";
	}
	
	@Override
	public Image getImage() {
		return ImageFactory.fetchImage(getImagePathPrefix() + ENDS[relativePosition] + ".png");
	}
}
