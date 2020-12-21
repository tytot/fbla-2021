
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
	private ArrayList<PlayerBlock>[] splitBlocks = new ArrayList[2];
	private PlayerBlock highlightedBlock = null;
	private Point[] splitLine = null;
	private int chosenSide = -1;
	
	public static final int NORMAL = 0;
	public static final int BUILDING = 1;
	public static final int SPLITTING = 2;
	public static final int CHOOSING = 3;
	
	private int state = NORMAL;

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
	
	public ArrayList<PlayerBlock> getSplitBlocks(int side) {
		return splitBlocks[side];
	}

	public PlayerBlock getHighlightedBlock() {
		return highlightedBlock;
	}
	
	public Point[] getSplitLine() {
		return splitLine;
	}
	
	public int getChosenSide() {
		return chosenSide;
	}

	public int getState() {
		return state;
	}

	public void setMovement(Movement movement) {
		this.movement = movement;
	}

	public void addBlock(int worldX, int worldY, Color color) {
		playerBlocks.add(new PlayerBlock(worldX, worldY, color));
	}

	public void highlightBlock(int mouseX, int mouseY) {
		PlayerBlock block = new PlayerBlock(mouseX / PlayerBlock.SIZE, mouseY / PlayerBlock.SIZE, Color.PINK);
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
		state = BUILDING;
		for (PlayerBlock block : playerBlocks) {
			Point worldPos = block.getWorldCoords();
			for (int[] offset : new int[][] { { 1, 0 }, { 0, 1 }, { -1, 0 },
					{ 0, -1 } }) {
				int testX = worldPos.x + offset[0];
				int testY = worldPos.y + offset[1];
				if (isValidBlock(map, testX, testY)) {
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
		state = NORMAL;
	}
	
	public void highlightSplitLine(MapBlock[][] map, int mouseX, int mouseY) {
		int xLine = (int) (Math.round((double) mouseX / PlayerBlock.SIZE));
		int yLine = (int) (Math.round((double) mouseY / PlayerBlock.SIZE));
		Point[] xSplitLine = null, ySplitLine = null;
		int startY = 0;
		while (startY < map.length && (!playerBlockAt(xLine - 1, startY) || !playerBlockAt(xLine, startY)))
			startY++;
		if (startY < map.length) {
			int endY = startY + 1;
			while (playerBlockAt(xLine - 1, endY) && playerBlockAt(xLine, endY)) {
				endY++;
			}
			xSplitLine = new Point[] { new Point(xLine, startY), new Point(xLine, endY) };
		}
		int startX = 0;
		while (startX < map[0].length && (!playerBlockAt(startX, yLine - 1) || !playerBlockAt(startX, yLine)))
			startX++;
		if (startX < map[0].length) {
			int endX = startX + 1;
			while (playerBlockAt(endX, yLine - 1) && playerBlockAt(endX, yLine)) {
				endX++;
			}
			ySplitLine = new Point[] { new Point(startX, yLine), new Point(endX, yLine) };
		}
		if (xSplitLine == null) {
			if (ySplitLine != null)
				splitLine = ySplitLine;
			else
				splitLine = null;
		} else if (ySplitLine == null) {
			if (xSplitLine != null)
				splitLine = xSplitLine;
		} else {
			if (Math.abs(mouseX - xLine * PlayerBlock.SIZE) <= Math.abs(mouseY - yLine * PlayerBlock.SIZE))
				splitLine = xSplitLine;
			else
				splitLine = ySplitLine;
		}
	}
	
	public void startSplitting() {
		state = SPLITTING;
	}
	
	public void splitIntoSides() {
		state = CHOOSING;
		splitBlocks[0] = new ArrayList<PlayerBlock>();
		splitBlocks[1] = new ArrayList<PlayerBlock>();
		if (splitLine[0].x == splitLine[1].x) {
			int xLine = splitLine[0].x;
			for (PlayerBlock pBlock : playerBlocks) {
				if (pBlock.getWorldCoords().x < xLine)
					splitBlocks[0].add(pBlock);
				else
					splitBlocks[1].add(pBlock);
			}
		} else {
			int yLine = splitLine[0].y;
			for (PlayerBlock pBlock : playerBlocks) {
				if (pBlock.getWorldCoords().y < yLine)
					splitBlocks[0].add(pBlock);
				else
					splitBlocks[1].add(pBlock);
			}
		}
		splitLine = null;
	}
	
	public void setChosenSide(int mouseX, int mouseY) {
		PlayerBlock testBlock = new PlayerBlock(mouseX / PlayerBlock.SIZE, mouseY / PlayerBlock.SIZE);
		for (PlayerBlock pBlock : splitBlocks[0]) {
			// no idea why .contains() doesn't work >:(
			if (pBlock.equals(testBlock)) {
				chosenSide = 0;
				return;
			}
		}
		for (PlayerBlock pBlock : splitBlocks[1]) {
			// no idea why .contains() doesn't work >:(
			if (pBlock.equals(testBlock)) {
				chosenSide = 1;
				return;
			}
		}
		chosenSide = -1;
	}
	
	public ArrayList<PlayerBlock> chooseSide(int side) {
		// returns blocks to be made solid
		state = NORMAL;
		chosenSide = -1;
		playerBlocks = splitBlocks[side];
		ArrayList<PlayerBlock> output;
		if (side == 1)
			output = (ArrayList<PlayerBlock>) splitBlocks[0].clone();
		else
			output = (ArrayList<PlayerBlock>) splitBlocks[1].clone();
		splitBlocks[0] = null;
		splitBlocks[1] = null;
		return output;
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
				int worldPosY2 = worldPos.y + 1;
				if (temp.y % PlayerBlock.SIZE == 0)
					worldPosY2 = worldPos.y;
				if (((isValidBlock(map, worldPos.x - 1, worldPos.y) && map[worldPos.y][worldPos.x - 1].isSolid())
						|| (isValidBlock(map, worldPos.x - 1, worldPosY2) && map[worldPosY2][worldPos.x - 1].isSolid()))
						&& temp.x - SPEED_X < nearestLeftX) {
					temp.setLocation(nearestLeftX, temp.y);
				} else {
					temp.translate(-SPEED_X, 0);
				}
			} else if (movement == Movement.RIGHT) {
				int nearestRightX = PlayerBlock.SIZE
						* (int) Math.ceil(temp.getX() / PlayerBlock.SIZE);
				int rightBlockX = (int) Math
						.ceil(temp.getX() / PlayerBlock.SIZE) + 1;
				int worldPosY2 = worldPos.y + 1;
				if (temp.y % PlayerBlock.SIZE == 0)
					worldPosY2 = worldPos.y;
				if (((isValidBlock(map, rightBlockX, worldPos.y) && map[worldPos.y][rightBlockX].isSolid())
						|| (isValidBlock(map, rightBlockX, worldPosY2) && map[worldPosY2][rightBlockX].isSolid()))
						&& temp.x + SPEED_X > nearestRightX) {
					temp.setLocation(nearestRightX, temp.y);
				} else {
					temp.translate(SPEED_X, 0);
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
			int belowBlockX2 = belowBlockX + 1;
			if (temp.x % PlayerBlock.SIZE == 0) {
				belowBlockX2 = belowBlockX;
			}
			if (!((isValidBlock(map, belowBlockX, belowBlockY) && map[belowBlockY][belowBlockX].isSolid())
					|| (isValidBlock(map, belowBlockX2, belowBlockY) && map[belowBlockY][belowBlockX2].isSolid()))) {
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
	
	private boolean isValidBlock(MapBlock[][] map, int width, int height) {
		return height >= 0 && height < map.length && width >= 0 && width < map[0].length;
	}
	
	private boolean playerBlockAt(int worldX, int worldY) {
		for (PlayerBlock pBlock : playerBlocks) {
			Point worldCoords = pBlock.getWorldCoords();
			if (worldCoords.x == worldX && worldCoords.y == worldY)
				return true;
		}
		return false;
	}
}