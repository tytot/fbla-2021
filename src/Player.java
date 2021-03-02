
import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class Player {

	private Movement movement = Movement.STILL;
	private static final int SPEED_X = 8;
	private int speedY;
	private static final int ACC_Y = 1;

	private ArrayList<PlayerBlock> playerBlocks = new ArrayList<PlayerBlock>();
	private ArrayList<PlayerBlock> buildBlocks = new ArrayList<PlayerBlock>();
	@SuppressWarnings("unchecked")
	private ArrayList<PlayerBlock>[] splitBlocks = new ArrayList[2];
	private PlayerBlock highlightedBlock = null;
	private Point[] splitLine = null;
	private int chosenSide = -1;
	
	private final int[][] ADJACENCIES = new int[][] { { 1, 0 }, { 0, 1 }, { -1, 0 }, { 0, -1 } };

	public static final int NORMAL = 0;
//	public static final int BUILDING = 1;
	public static final int SPLITTING = 2;
	public static final int CHOOSING = 3;

	private int state = NORMAL;
	
	public void setState(int state) {
		this.state = state;
	}
	public Movement getMovement() {
		return movement;
	}

	public boolean isFalling() {
		return speedY > 0;
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
	
	public boolean intersectsPoint(Point point) {
		for (PlayerBlock pBlock : playerBlocks) {
			Point pixelCoords = pBlock.getPixelCoords();
			if (point.x >= pixelCoords.x && point.x <= pixelCoords.x + Block.SIZE && point.y >= pixelCoords.y && point.y <= pixelCoords.y + Block.SIZE) {
				return true;
			}
		}
		return false;
	}

	public void squishBlocks(Set<PlayerBlock> blocksToSquish) {
		boolean change = false;
		for (PlayerBlock squishedBlock : blocksToSquish) {
			if (!squishedBlock.isOnGoalBlock()) {
				squishedBlock.setOnGoalBlock(true);
				change = true;
			}
		}
		for (PlayerBlock pBlock : playerBlocks) {
			if (!blocksToSquish.contains(pBlock) && pBlock.isOnGoalBlock()) {
				pBlock.setOnGoalBlock(false);
				change = true;
			}
		}
		if (change) {
			calculateRelativePositions(playerBlocks);
		}
	}

	public void calculateRelativePositions(ArrayList<PlayerBlock> pBlocks) {
		for (PlayerBlock pBlock : pBlocks) {
			Point worldCoords = pBlock.getWorldCoords();
			int x = worldCoords.x, y = worldCoords.y;
			int leftIndex = playerBlocks.indexOf(new PlayerBlock(x - 1, y));
			int rightIndex = playerBlocks.indexOf(new PlayerBlock(x + 1, y));
			boolean left = leftIndex != -1;
			boolean right = rightIndex != -1;
			boolean top = playerBlocks.contains(new PlayerBlock(x, y - 1));
			boolean bottom = playerBlocks.contains(new PlayerBlock(x, y + 1));
			int relativePos;
			if (left) {
				// block to left
				if (right) {
					// block to right
					if (top) {
						// block on top
						relativePos = Block.CENTER;
					} else {
						// no block on top
						relativePos = Block.MIDDLE;
					}
				} else {
					// no block to right
					if (top) {
						// block on top
						if (bottom) {
							// block below
							relativePos = Block.CENTER;
						} else {
							// no block below
							relativePos = Block.BOTTOM_RIGHT;
						}
					} else {
						// no block on top
						if (bottom) {
							// block below
							relativePos = Block.TOP_RIGHT;
						} else {
							// no block below
							relativePos = Block.RIGHT;
						}
					}
				}
			} else {
				// no block to left
				if (right) {
					// block to right
					if (top) {
						// block on top
						if (bottom) {
							// block below
							relativePos = Block.CENTER;
						} else {
							// no block below
							relativePos = Block.BOTTOM_LEFT;
						}
					} else {
						// no block on top
						if (bottom) {
							// block below
							relativePos = Block.TOP_LEFT;
						} else {
							// no block below
							relativePos = Block.LEFT;
						}
					}
				} else {
					// no block to right
					if (top) {
						// block on top
						if (bottom) {
							// block below
							relativePos = Block.CENTER;
						} else {
							// no block below
							relativePos = Block.BOTTOM;
						}
					} else {
						// no block on top
						if (bottom) {
							// block below
							relativePos = Block.TOP;
						} else {
							// no block below
							relativePos = Block.ALONE;
						}
					}
				}
			}
			if (!pBlock.isOnGoalBlock()) {
				boolean leftGoal = left && playerBlocks.get(leftIndex).isOnGoalBlock();
				boolean rightGoal = right && playerBlocks.get(rightIndex).isOnGoalBlock();
				if (leftGoal && !rightGoal) {
					if (relativePos == Block.MIDDLE || relativePos == Block.CENTER)
						relativePos = Block.BOTTOM_LEFT;
					else if (relativePos == Block.BOTTOM_RIGHT)
						relativePos = Block.BOTTOM;
					else if (relativePos == Block.RIGHT)
						relativePos = Block.RIGHT_AND_BOTTOM_LEFT;
				} else if (!leftGoal && rightGoal) {
					if (relativePos == Block.MIDDLE || relativePos == Block.CENTER)
						relativePos = Block.BOTTOM_RIGHT;
					else if (relativePos == Block.BOTTOM_LEFT)
						relativePos = Block.BOTTOM;
					else if (relativePos == Block.LEFT)
						relativePos = Block.LEFT_AND_BOTTOM_RIGHT;
				} else if (leftGoal && rightGoal) {
					if (relativePos == Block.MIDDLE || relativePos == Block.CENTER)
						relativePos = Block.BOTTOM;
				}
			}
			pBlock.setRelativePosition(relativePos);
		}
	}

	public void addBlock(int worldX, int worldY) {
		playerBlocks.add(new PlayerBlock(worldX, worldY));
	}

	public void highlightBlock(int mouseX, int mouseY) {
		PlayerBlock block = new Crosshair(mouseX / Block.SIZE, mouseY / Block.SIZE);
		if (buildBlocks.contains(block)) {
			highlightedBlock = block;
		} else {
			highlightedBlock = null;
		}
	}

	public void setBuildBlocks(Map map) {
		buildBlocks.clear();
		for (PlayerBlock block : playerBlocks) {
			Point worldPos = block.getWorldCoords();
			for (int[] offset : new int[][] { { 1, 0 }, { 0, 1 }, { -1, 0 }, { 0, -1 } }) {
				int testX = worldPos.x + offset[0];
				int testY = worldPos.y + offset[1];
				if (map.isValidBlock(testX, testY)) {
					PlayerBlock testBlock = new PlayerBlock(testX, testY);
					if (!map.getBlocks()[testY][testX].isSolid() && !playerBlocks.contains(testBlock)) {
						buildBlocks.add(testBlock);
					}
				}
			}
		}
	}

	public void confirmBuild(Map map) {
		playerBlocks.add(new PlayerBlock(highlightedBlock));
		calculateRelativePositions(playerBlocks);
		setBuildBlocks(map);
		highlightedBlock = null;
	}

	public void highlightSplitLine(Map map, int mouseX, int mouseY) {
		int mapHeight = map.getBlocks().length;
		int mapWidth = map.getBlocks()[0].length;
		int xLine = (int) (Math.round((double) mouseX / Block.SIZE));
		int yLine = (int) (Math.round((double) mouseY / Block.SIZE));
		Point[] xSplitLine = null, ySplitLine = null;
		int startY = yLine;
		if (playerBlockAt(xLine - 1, startY) && playerBlockAt(xLine, startY)) {
			while (playerBlockAt(xLine - 1, startY - 1) && playerBlockAt(xLine, startY - 1))
				startY--;
			int endY = startY + 1;
			while (playerBlockAt(xLine - 1, endY) && playerBlockAt(xLine, endY))
				endY++;
			xSplitLine = new Point[] { new Point(xLine, startY), new Point(xLine, endY) };
		} else {
			boolean topFound = true, bottomFound = true;
			int topY = startY - 1;
			while (topY >= 0 && (!playerBlockAt(xLine - 1, topY) || !playerBlockAt(xLine, topY)))
				topY--;
			if (topY < 0)
				topFound = false;
			int bottomY = startY + 1;
			while (bottomY < mapHeight && (!playerBlockAt(xLine - 1, bottomY) || !playerBlockAt(xLine, bottomY)))
				bottomY++;
			if (bottomY >= mapHeight)
				bottomFound = false;
			int side = -1;
			if (topFound && !bottomFound) {
				side = 0;
			} else if (!topFound && bottomFound) {
				side = 1;
			} else if (topFound && bottomFound) {
				if (startY - topY - 1 <= bottomY - startY) {
					side = 0;
				} else {
					side = 1;
				}
			}
			if (side == 0) {
				int endY = topY + 1;
				startY = topY;
				while (playerBlockAt(xLine - 1, startY - 1) && playerBlockAt(xLine, startY - 1))
					startY--;
				xSplitLine = new Point[] { new Point(xLine, startY), new Point(xLine, endY) };
			} else if (side == 1) {
				startY = bottomY;
				int endY = startY + 1;
				while (playerBlockAt(xLine - 1, endY) && playerBlockAt(xLine, endY))
					endY++;
				xSplitLine = new Point[] { new Point(xLine, startY), new Point(xLine, endY) };
			}
		}

		int startX = xLine;
		if (playerBlockAt(startX, yLine - 1) && playerBlockAt(startX, yLine)) {
			while (playerBlockAt(startX - 1, yLine - 1) && playerBlockAt(startX - 1, yLine))
				startX--;
			int endX = startX + 1;
			while (playerBlockAt(endX, yLine - 1) && playerBlockAt(endX, yLine))
				endX++;
			ySplitLine = new Point[] { new Point(startX, yLine), new Point(endX, yLine) };
		} else {
			boolean leftFound = true, rightFound = true;
			int leftX = startX - 1;
			while (leftX >= 0 && (!playerBlockAt(leftX, yLine - 1) || !playerBlockAt(leftX, yLine)))
				leftX--;
			if (leftX < 0)
				leftFound = false;
			int rightX = startX + 1;
			while (rightX < mapWidth && (!playerBlockAt(rightX, yLine - 1) || !playerBlockAt(rightX, yLine)))
				rightX++;
			if (rightX >= mapWidth)
				rightFound = false;
			int side = -1;
			if (leftFound && !rightFound) {
				side = 0;
			} else if (!leftFound && rightFound) {
				side = 1;
			} else if (leftFound && rightFound) {
				if (startX - leftX - 1 <= rightX - startX) {
					side = 0;
				} else {
					side = 1;
				}
			}
			if (side == 0) {
				int endX = leftX + 1;
				startX = leftX;
				while (playerBlockAt(startX - 1, yLine - 1) && playerBlockAt(startX - 1, yLine))
					startX--;
				ySplitLine = new Point[] { new Point(startX, yLine), new Point(endX, yLine) };
			} else if (side == 1) {
				startX = rightX;
				int endX = startX + 1;
				while (playerBlockAt(endX, yLine - 1) && playerBlockAt(endX, yLine))
					endX++;
				ySplitLine = new Point[] { new Point(startX, yLine), new Point(endX, yLine) };
			}
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
			if (Math.abs(mouseX - xLine * Block.SIZE) <= Math.abs(mouseY - yLine * Block.SIZE))
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
		if (splitLine[0].x == splitLine[1].x) {
			splitBlocks[0] = blocksConnectedTo(new PlayerBlock(splitLine[0].x - 1, splitLine[0].y));
			splitBlocks[1] = blocksConnectedTo(new PlayerBlock(splitLine[0].x, splitLine[0].y));
		} else {
			splitBlocks[0] = blocksConnectedTo(new PlayerBlock(splitLine[0].x, splitLine[0].y - 1));
			splitBlocks[1] = blocksConnectedTo(new PlayerBlock(splitLine[0].x, splitLine[0].y));
		}
		if (splitBlocks[0].size() + splitBlocks[1].size() > playerBlocks.size()) {
			splitBlocks[0].clear();
			for (PlayerBlock pBlock : playerBlocks) {
				splitBlocks[0].add(new HighlightedPlayerBlock(pBlock));
			}
			splitBlocks[1].clear();
		}
		calculateRelativePositions(splitBlocks[0]);
		calculateRelativePositions(splitBlocks[1]);
		splitLine = null;
	}

	private ArrayList<PlayerBlock> blocksConnectedTo(PlayerBlock block) {
		boolean horizontalCut = splitLine[0].y == splitLine[1].y;
		int splitLoc = splitLine[0].x;
		if (horizontalCut)
			splitLoc = splitLine[0].y;
		ArrayList<PlayerBlock> output = new ArrayList<PlayerBlock>();
		Queue<Point> queue = new LinkedList<Point>();
		int x = block.getWorldCoords().x, y = block.getWorldCoords().y;
		queue.offer(new Point(x, y));
		block = new HighlightedPlayerBlock(x, y);
		output.add(block);
		while (!queue.isEmpty()) {
			Point currPos = queue.poll();
			for (int[] offset : ADJACENCIES) {
				int testX = currPos.x + offset[0];
				int testY = currPos.y + offset[1];
				boolean otherSide;
				int notConsidered = splitLoc;
				if (horizontalCut) {
					if (block.getWorldCoords().y >= splitLoc)
						notConsidered--;
					otherSide = testX >= splitLine[0].x && testX < splitLine[1].x && testY == notConsidered;
				} else {
					if (block.getWorldCoords().x >= splitLoc)
						notConsidered--;
					otherSide = testY >= splitLine[0].y && testY < splitLine[1].y && testX == notConsidered;
				}
				if (!otherSide) {
					PlayerBlock testBlock = new HighlightedPlayerBlock(testX, testY);
					if (playerBlocks.contains(testBlock) && !output.contains(testBlock)) {
						queue.offer(new Point(testX, testY));
						output.add(testBlock);
					}
				}
			}
		}
		return output;
	}

	public void setChosenSide(int mouseX, int mouseY) {
		PlayerBlock testBlock = new PlayerBlock(mouseX / Block.SIZE, mouseY / Block.SIZE);
		if (splitBlocks[0].contains(testBlock))
			chosenSide = 0;
		else if (splitBlocks[1].contains(testBlock))
			chosenSide = 1;
		else
			chosenSide = -1;
	}

	@SuppressWarnings("unchecked")
	public ArrayList<PlayerBlock> chooseSide(int side) {
		// returns blocks to be made solid
		state = NORMAL;
		chosenSide = -1;
		playerBlocks.clear();
		for (PlayerBlock chosenBlock : splitBlocks[side]) {
			playerBlocks.add(new PlayerBlock(chosenBlock));
		}
		calculateRelativePositions(playerBlocks);
		ArrayList<PlayerBlock> output;
		if (side == 1)
			output = (ArrayList<PlayerBlock>) splitBlocks[0].clone();
		else
			output = (ArrayList<PlayerBlock>) splitBlocks[1].clone();
		splitBlocks[0] = null;
		splitBlocks[1] = null;
		return output;
	}

	// Merge all adjacent crying player blocks into the player
	public ArrayList<PlayerBlock> merge(Map map) {
		// Breadth-first search implementation using LinkedList Queue
		Queue<Point> queue = new LinkedList<Point>();
		ArrayList<PlayerBlock> mergedBlocks = new ArrayList<PlayerBlock>();
		// Place the positions of all current player blocks into the queue
		for (PlayerBlock block : playerBlocks) {
			queue.offer(block.getWorldCoords());
		}
		// While there are positions in the queue
		while (!queue.isEmpty()) {
			Point currPos = queue.poll();
			// For each of the 4 blocks adjacent to the polled position
			for (int[] offset : ADJACENCIES) {
				int testX = currPos.x + offset[0];
				int testY = currPos.y + offset[1];
				// Check to see if the block is valid and an instance of CryingPlayerBlock
				if (map.isValidBlock(testX, testY)) {
					PlayerBlock newPBlock = new PlayerBlock(testX, testY);
					if (map.getBlocks()[testY][testX] instanceof CryingPlayerBlock
							&& !mergedBlocks.contains(newPBlock)) {
						// If so, merge the CryingPlayerBlock into a PlayerBlock and add its position to
						// the queue
						mergedBlocks.add(newPBlock);
						queue.offer(new Point(testX, testY));
					}
				}
			}
		}
		return mergedBlocks;
	}

	public void move(Map map) {
		int minDX;
		int minDY = Integer.MAX_VALUE;
		if (movement == Movement.LEFT || movement == Movement.STILL_LEFT) {
			minDX = Integer.MIN_VALUE;
		} else if (movement == Movement.RIGHT || movement == Movement.STILL_RIGHT) {
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
				int nearestLeftX = worldPos.x * Block.SIZE;
				int worldPosY2 = worldPos.y + 1;
				if (temp.y % Block.SIZE == 0)
					worldPosY2 = worldPos.y;
				if (((map.isValidBlock(worldPos.x - 1, worldPos.y)
						&& map.getBlocks()[worldPos.y][worldPos.x - 1].isSolid())
						|| (map.isValidBlock(worldPos.x - 1, worldPosY2)
								&& map.getBlocks()[worldPosY2][worldPos.x - 1].isSolid()))
						&& temp.x - SPEED_X < nearestLeftX) {
					temp.setLocation(nearestLeftX, temp.y);
				} else {
					temp.translate(-SPEED_X, 0);
				}
			} else if (movement == Movement.RIGHT) {
				int nearestRightX = Block.SIZE * (int) Math.ceil(temp.getX() / Block.SIZE);
				int rightBlockX = (int) Math.ceil(temp.getX() / Block.SIZE) + 1;
				int worldPosY2 = worldPos.y + 1;
				if (temp.y % Block.SIZE == 0)
					worldPosY2 = worldPos.y;
				if (((map.isValidBlock(rightBlockX, worldPos.y) && map.getBlocks()[worldPos.y][rightBlockX].isSolid())
						|| (map.isValidBlock(rightBlockX, worldPosY2)
								&& map.getBlocks()[worldPosY2][rightBlockX].isSolid()))
						&& temp.x + SPEED_X > nearestRightX) {
					temp.setLocation(nearestRightX, temp.y);
				} else {
					temp.translate(SPEED_X, 0);
				}
			} else if (movement == Movement.STILL_LEFT) {
				int nearestLeftX = worldPos.x * Block.SIZE;
				if (temp.x - nearestLeftX < SPEED_X) {
					temp.setLocation(nearestLeftX, temp.y);
					movement = Movement.STILL;
				} else {
					temp.translate(-SPEED_X, 0);
				}
			} else if (movement == Movement.STILL_RIGHT) {
				int nearestRightX = Block.SIZE * (int) Math.ceil(temp.getX() / Block.SIZE);
				if (nearestRightX - temp.x < SPEED_X) {
					temp.setLocation(nearestRightX, temp.y);
					movement = Movement.STILL;
				} else {
					temp.translate(SPEED_X, 0);
				}
			}

			int nearestX = Block.SIZE * (int) Math.round(temp.getX() / Block.SIZE);
			if (Math.abs(nearestX - temp.x) <= SPEED_X / 2) {
				temp.setLocation(nearestX, temp.y);
			}

			int belowBlockX = temp.x / Block.SIZE;
			int belowBlockY = (int) Math.ceil(temp.getY() / Block.SIZE) + 1;
			int tempSpeed = speedY;
			int belowBlockX2 = belowBlockX + 1;
			if (temp.x % Block.SIZE == 0) {
				belowBlockX2 = belowBlockX;
			}
			if (!((map.isValidBlock(belowBlockX, belowBlockY) && map.getBlocks()[belowBlockY][belowBlockX].isSolid())
					|| (map.isValidBlock(belowBlockX2, belowBlockY)
							&& map.getBlocks()[belowBlockY][belowBlockX2].isSolid()))) {
				tempSpeed += ACC_Y;
			} else if (tempSpeed > 0) {
				tempSpeed += ACC_Y;
				if (temp.y + tempSpeed >= (belowBlockY - 1) * Block.SIZE) {
					tempSpeed = 0;
					temp.setLocation(temp.x, (belowBlockY - 1) * Block.SIZE);
				}
			}
			if (tempSpeed != 0) {
				temp.translate(0, tempSpeed);
			}

			int dx = temp.x - pixelPos.x;
			int dy = temp.y - pixelPos.y;
			if (startMvmt == Movement.LEFT || startMvmt == Movement.STILL_LEFT)
				minDX = Math.max(minDX, dx);
			else if (startMvmt == Movement.RIGHT || startMvmt == Movement.STILL_RIGHT)
				minDX = Math.min(minDX, dx);
			minDY = Math.min(minDY, dy);
		}
		speedY = minDY;
		for (PlayerBlock block : playerBlocks) {
			Point pixelPos = block.getPixelCoords();
			pixelPos.translate(minDX, minDY);
			block.getWorldCoords().setLocation(Math.floor(pixelPos.getX() / Block.SIZE),
					Math.floor(pixelPos.getY() / Block.SIZE));
		}
		if (movement == Movement.STILL && !isFalling()) {
			if (buildBlocks.isEmpty()) {
				setBuildBlocks(map);
			}
		} else if (!buildBlocks.isEmpty()) {
			buildBlocks.clear();
			highlightedBlock = null;
		}
	}

	public boolean isOutOfBounds(Map map) {
		boolean out = true;
		for (PlayerBlock pBlock : playerBlocks) {
			Point playerPos = pBlock.getWorldCoords();
			if (map.isValidBlock(playerPos.x, playerPos.y) || playerPos.x == -1) {
				out = false;
			}
		}
		return out;
	}

	private boolean playerBlockAt(int worldX, int worldY) {
		return playerBlocks.contains(new PlayerBlock(worldX, worldY));
	}

	public void resetPositions(ArrayList<Point> positions) {
		playerBlocks.clear();
		for (Point pos : positions) {
			playerBlocks.add(new PlayerBlock(pos.x, pos.y));
		}
		calculateRelativePositions(playerBlocks);
		movement = Movement.STILL;
	}

	public ArrayList<PlayerBlock> blocksOnGoalBlock(Point point) {
		ArrayList<PlayerBlock> output = new ArrayList<PlayerBlock>();
		Point pixelCoords = new Point(point.x * Block.SIZE, point.y * Block.SIZE);
		for (PlayerBlock pBlock : playerBlocks) {
			Point checkCoords = pBlock.getPixelCoords();
			int dx = pixelCoords.x - checkCoords.x;
			int dy = pixelCoords.y - checkCoords.y;
			if (dy <= GoalBlock.PRESSED_SIZE && dy >= 0 && dx < Block.SIZE && dx > -Block.SIZE) {
				output.add(pBlock);
			}
		}
		return output;
	}

	public boolean reachedGoal(Map map) {
		ArrayList<PlayerBlock> onGoal = new ArrayList<PlayerBlock>();
		for (int i = 0; i < map.getGoalBlocks().size(); i++) {
			PlayerBlock goal = new PlayerBlock(map.getGoalBlocks().get(i).x, map.getGoalBlocks().get(i).y);
			if (!playerBlocks.contains(goal))
				return false;
			onGoal.add(goal);

		}
		for (PlayerBlock pBlock : playerBlocks) {
			Point worldCoords = pBlock.getWorldCoords();
			if (!onGoal.contains(pBlock)) {
				if (map.isValidBlock(worldCoords.x, worldCoords.y + 1)
						&& map.getBlocks()[worldCoords.y + 1][worldCoords.x] instanceof SolidBlock
						|| map.getBlocks()[worldCoords.y + 1][worldCoords.x] instanceof CryingPlayerBlock)
					return false;
			}
		}
		return true;
	}
}