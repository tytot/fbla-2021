
import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;

public class Player {

	private Movement movement = Movement.STILL;
	private static final int SPEED_X = 4;
	private int speedY;
	private static final int ACC_Y = 1;

	private ArrayList<PlayerBlock> playerBlocks = new ArrayList<PlayerBlock>();
	private ArrayList<PlayerBlock> buildBlocks = new ArrayList<PlayerBlock>();
	private PlayerBlock highlightedBlock = null;
	private boolean building = false;

	public Movement getMovement() {
		return movement;
	}

	public boolean isFalling() {
		return speedY == 0;
	}

	public ArrayList<PlayerBlock> getBlocks() {
		return playerBlocks;
	}

	public ArrayList<PlayerBlock> getBuildBlocks() {
		return buildBlocks;
	}

	public PlayerBlock getHighlightedBlock() {
		return highlightedBlock;
	}

	public boolean isBuilding() {
		return building;
	}

	public void setMovement(Movement movement) {
		this.movement = movement;
	}

	public void addBlock(int worldX, int worldY, Color color) {
		playerBlocks.add(new PlayerBlock(worldX, worldY, color));
	}

	public void highlightBlock(int mouseX, int mouseY) {
		PlayerBlock block = new PlayerBlock(mouseX, mouseY, Color.PINK);
		// no idea why .contains() doesn't work >:(
		for (PlayerBlock bBlock : buildBlocks) {
			if (bBlock.equals(block)) {
				highlightedBlock = block;
				return;
			}
		}
		highlightedBlock = null;
	}

	public void startBuilding(MapBlock[][] map) {
		building = true;
		for (PlayerBlock block : playerBlocks) {
			Point worldPos = block.getWorldCoords();
			for (int[] offset : new int[][] { { 1, 0 }, { 0, 1 }, { -1, 0 },
					{ 0, -1 } }) {
				int testX = worldPos.x + offset[0];
				int testY = worldPos.y + offset[1];
				if (testX >= 0 && testX < map[0].length && testY >= 0
						&& testY < map.length) {
					PlayerBlock testBlock = new PlayerBlock(
							worldPos.x + offset[0], worldPos.y + offset[1]);
					if (!map[testY][testX].isSolid()) {
						boolean valid = true;
						for (PlayerBlock pBlock : playerBlocks) {
							// no idea why .contains() doesn't work >:(
							if (pBlock.equals(testBlock)) {
								valid = false;
								break;
							}
						}
						if (valid)
							buildBlocks.add(testBlock);
					}
				}
			}
		}
	}

	public void confirmBuild() {
		playerBlocks.add(highlightedBlock);
		stopBuilding();
	}

	public void stopBuilding() {
		buildBlocks.clear();
		highlightedBlock = null;
		building = false;
	}

	public void move(MapBlock[][] map) {
		int minDX;
		int minDY = Integer.MAX_VALUE;
		if (movement == Movement.LEFT || movement == Movement.STILL_LEFT) {
			minDX = Integer.MIN_VALUE;
		} else if (movement == Movement.RIGHT
				|| movement == Movement.STILL_RIGHT) {
			minDX = Integer.MAX_VALUE;
		} else {
			minDX = 0;
		}
		Movement startMvmt = movement;
		for (PlayerBlock pBlock : playerBlocks) {
			Point worldPos = pBlock.getWorldCoords();
			Point pixelPos = pBlock.getPixelCoords();
			Point temp = new Point();
			temp.setLocation(pixelPos);
			if (movement == Movement.LEFT) {
				int nearestLeftX = worldPos.x * PlayerBlock.SIZE;
				if (temp.y % PlayerBlock.SIZE == 0) {
					if (map[worldPos.y][worldPos.x - 1].isSolid()
							&& temp.x - SPEED_X < nearestLeftX) {
						temp.setLocation(nearestLeftX, temp.y);
					} else {
						temp.translate(-SPEED_X, 0);
					}
				} else {
					if ((map[worldPos.y][worldPos.x - 1].isSolid()
							|| map[worldPos.y + 1][worldPos.x - 1].isSolid())
							&& temp.x - SPEED_X < nearestLeftX) {
						temp.setLocation(nearestLeftX, temp.y);
					} else {
						temp.translate(-SPEED_X, 0);
					}
				}
			} else if (movement == Movement.RIGHT) {
				int nearestRightX = PlayerBlock.SIZE
						* (int) Math.ceil(temp.getX() / PlayerBlock.SIZE);
				int rightBlockX = (int) Math
						.ceil(temp.getX() / PlayerBlock.SIZE) + 1;
				if (temp.y % PlayerBlock.SIZE == 0) {
					if (map[worldPos.y][rightBlockX].isSolid()
							&& temp.x + SPEED_X > nearestRightX) {
						temp.setLocation(nearestRightX, temp.y);
					} else {
						temp.translate(SPEED_X, 0);
					}
				} else {
					if ((map[worldPos.y][rightBlockX].isSolid()
							|| map[worldPos.y + 1][rightBlockX].isSolid())
							&& temp.x + SPEED_X > nearestRightX) {
						temp.setLocation(nearestRightX, temp.y);
					} else {
						temp.translate(SPEED_X, 0);
					}
				}
			} else if (movement == Movement.STILL_LEFT) {
				int nearestLeftX = worldPos.x * PlayerBlock.SIZE;
				if (temp.x - nearestLeftX < SPEED_X) {
					temp.setLocation(nearestLeftX, temp.y);
					movement = Movement.STILL;
				} else {
					temp.translate(-SPEED_X, 0);
				}
			} else if (movement == Movement.STILL_RIGHT) {
				int nearestRightX = PlayerBlock.SIZE
						* (int) Math.ceil(temp.getX() / PlayerBlock.SIZE);
				if (nearestRightX - temp.x < SPEED_X) {
					temp.setLocation(nearestRightX, temp.y);
					movement = Movement.STILL;
				} else {
					temp.translate(SPEED_X, 0);
				}
			}

			int nearestX = PlayerBlock.SIZE
					* (int) Math.round(temp.getX() / PlayerBlock.SIZE);
			if (Math.abs(nearestX - temp.x) <= SPEED_X / 2) {
				temp.setLocation(nearestX, temp.y);
			}

			int belowBlockX = temp.x / PlayerBlock.SIZE;
			int belowBlockY = (int) Math.ceil(temp.getY() / PlayerBlock.SIZE)
					+ 1;
			int tempSpeed = speedY;
			if (temp.x % PlayerBlock.SIZE == 0) {
				if (!map[belowBlockY][belowBlockX].isSolid()) {
					tempSpeed += ACC_Y;
				} else if (tempSpeed > 0) {
					tempSpeed += ACC_Y;
					if (temp.y + tempSpeed >= (belowBlockY - 1)
							* PlayerBlock.SIZE) {
						tempSpeed = 0;
						temp.setLocation(temp.x,
								(belowBlockY - 1) * PlayerBlock.SIZE);
					}
				}
			} else {
				if (!(map[belowBlockY][belowBlockX].isSolid()
						|| map[belowBlockY][belowBlockX + 1].isSolid())) {
					tempSpeed += ACC_Y;
				} else if (tempSpeed > 0) {
					tempSpeed += ACC_Y;
					if (temp.y + tempSpeed >= (belowBlockY - 1)
							* PlayerBlock.SIZE) {
						tempSpeed = 0;
						temp.setLocation(temp.x,
								(belowBlockY - 1) * PlayerBlock.SIZE);
					}
				}
			}
			if (tempSpeed != 0) {
				temp.translate(0, tempSpeed);
			}

			int dx = temp.x - pixelPos.x;
			int dy = temp.y - pixelPos.y;
			if (startMvmt == Movement.LEFT || startMvmt == Movement.STILL_LEFT)
				minDX = Math.max(minDX, dx);
			else if (startMvmt == Movement.RIGHT
					|| startMvmt == Movement.STILL_RIGHT)
				minDX = Math.min(minDX, dx);
			minDY = Math.min(minDY, dy);
		}
		speedY = minDY;
		for (PlayerBlock block : playerBlocks) {
			Point pixelPos = block.getPixelCoords();
			pixelPos.translate(minDX, minDY);
			block.getWorldCoords().setLocation(pixelPos.x / PlayerBlock.SIZE,
					pixelPos.y / PlayerBlock.SIZE);
		}
	}
}