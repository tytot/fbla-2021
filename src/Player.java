
import java.awt.Color;
import java.awt.Point;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

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
		if (buildBlocks.contains(block)) {
			highlightedBlock = block;
		} else {
			highlightedBlock = null;
		}
	}

	public void startBuilding(MapBlock[][] map) {
		state = BUILDING;
		for (PlayerBlock block : playerBlocks) {
			Point worldPos = block.getWorldCoords();
			for (int[] offset : new int[][] { { 1, 0 }, { 0, 1 }, { -1, 0 }, { 0, -1 } }) {
				int testX = worldPos.x + offset[0];
				int testY = worldPos.y + offset[1];
				if (isValidBlock(map, testX, testY)) {
					PlayerBlock testBlock = new PlayerBlock(testX, testY);
					if (!map[testY][testX].isSolid() && !playerBlocks.contains(testBlock)) {
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
			while (bottomY < map.length && (!playerBlockAt(xLine - 1, bottomY) || !playerBlockAt(xLine, bottomY)))
				bottomY++;
			if (bottomY >= map.length)
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
			while (rightX < map[0].length && (!playerBlockAt(rightX, yLine - 1) || !playerBlockAt(rightX, yLine)))
				rightX++;
			if (rightX >= map[0].length)
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
		if (splitLine[0].x == splitLine[1].x) {
			splitBlocks[0] = blocksConnectedTo(new PlayerBlock(splitLine[0].x - 1, splitLine[0].y));
			splitBlocks[1] = blocksConnectedTo(new PlayerBlock(splitLine[0].x, splitLine[0].y));
		} else {
			splitBlocks[0] = blocksConnectedTo(new PlayerBlock(splitLine[0].x, splitLine[0].y - 1));
			splitBlocks[1] = blocksConnectedTo(new PlayerBlock(splitLine[0].x, splitLine[0].y));
		}
		splitLine = null;
	}
	
	private ArrayList<PlayerBlock> blocksConnectedTo(PlayerBlock block) {
		int[][] offsets = new int[][] { { 1, 0 }, { 0, 1 }, { -1, 0 }, { 0, -1 } };
		boolean horizontalCut = splitLine[0].y == splitLine[1].y;
		int splitLoc = splitLine[0].x;
		if (horizontalCut)
			splitLoc = splitLine[0].y;
		ArrayList<PlayerBlock> output = new ArrayList<PlayerBlock>();
		Queue<Point> queue = new LinkedList<Point>();
		queue.offer(new Point(block.getWorldCoords().x, block.getWorldCoords().y));
		output.add(block);
		while (!queue.isEmpty()) {
			Point currPos = queue.poll();
			for (int[] offset : offsets) {
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
					PlayerBlock testBlock = new PlayerBlock(testX, testY);
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
		PlayerBlock testBlock = new PlayerBlock(mouseX / PlayerBlock.SIZE, mouseY / PlayerBlock.SIZE);
		if (splitBlocks[0].contains(testBlock))
			chosenSide = 0;
		else if (splitBlocks[1].contains(testBlock))
			chosenSide = 1;
		else
			chosenSide = -1;
	}

	public ArrayList<PlayerBlock> chooseSide(int side) {
		// returns blocks to be made solid
		state = NORMAL;
		chosenSide = -1;
		playerBlocks = new ArrayList<PlayerBlock>(splitBlocks[side]);
		ArrayList<PlayerBlock> output;
		if (side == 1)
			output = (ArrayList<PlayerBlock>) splitBlocks[0].clone();
		else
			output = (ArrayList<PlayerBlock>) splitBlocks[1].clone();
		splitBlocks[0] = null;
		splitBlocks[1] = null;
		return output;
	}
	
	public ArrayList<PlayerBlock> merge(MapBlock[][] map) {
		Queue<Point> queue = new LinkedList<Point>();
		int[][] offsets = new int[][] { { 1, 0 }, { 0, 1 }, { -1, 0 }, { 0, -1 } };
		ArrayList<PlayerBlock> mergedBlocks = new ArrayList<PlayerBlock>();
		for (PlayerBlock block : playerBlocks) {
			queue.offer(block.getWorldCoords());
		}
		while (!queue.isEmpty()) {
			Point currPos = queue.poll();
			for (int[] offset : offsets) {
				int testX = currPos.x + offset[0];
				int testY = currPos.y + offset[1];
				if (isValidBlock(map, testX, testY)) {
					PlayerBlock newPBlock = new PlayerBlock(testX, testY, Color.RED);
					if (map[testY][testX] instanceof CryingPlayerBlock && !mergedBlocks.contains(newPBlock)) {
						mergedBlocks.add(newPBlock);
						playerBlocks.add(newPBlock);
						queue.offer(new Point(testX, testY));
					}
				}
			}
		}
		return mergedBlocks;
	}

	public void move(MapBlock[][] map) {
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
				int nearestRightX = PlayerBlock.SIZE * (int) Math.ceil(temp.getX() / PlayerBlock.SIZE);
				int rightBlockX = (int) Math.ceil(temp.getX() / PlayerBlock.SIZE) + 1;
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
				int nearestRightX = PlayerBlock.SIZE * (int) Math.ceil(temp.getX() / PlayerBlock.SIZE);
				if (nearestRightX - temp.x < SPEED_X) {
					temp.setLocation(nearestRightX, temp.y);
					movement = Movement.STILL;
				} else {
					temp.translate(SPEED_X, 0);
				}
			}

			int nearestX = PlayerBlock.SIZE * (int) Math.round(temp.getX() / PlayerBlock.SIZE);
			if (Math.abs(nearestX - temp.x) <= SPEED_X / 2) {
				temp.setLocation(nearestX, temp.y);
			}

			int belowBlockX = temp.x / PlayerBlock.SIZE;
			int belowBlockY = (int) Math.ceil(temp.getY() / PlayerBlock.SIZE) + 1;
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
				if (temp.y + tempSpeed >= (belowBlockY - 1) * PlayerBlock.SIZE) {
					tempSpeed = 0;
					temp.setLocation(temp.x, (belowBlockY - 1) * PlayerBlock.SIZE);
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
			block.getWorldCoords().setLocation(Math.floor(pixelPos.getX() / PlayerBlock.SIZE), Math.floor(pixelPos.getY() / PlayerBlock.SIZE));
		}
	}

	public boolean isOutOfBounds(MapBlock[][] map) {
		boolean out = true;
		for (PlayerBlock pBlock : playerBlocks) {
			Point playerPos = pBlock.getWorldCoords();
			if (isValidBlock(map, playerPos.x, playerPos.y) || playerPos.x == -1) {
				out = false;
			}
		}
		return out;
	}

	private boolean isValidBlock(MapBlock[][] map, int width, int height) {
		return height >= 0 && height < map.length && width >= 0 && width < map[0].length;
	}

	private boolean playerBlockAt(int worldX, int worldY) {
		return playerBlocks.contains(new PlayerBlock(worldX, worldY));
	}
	
	public void resetPositions(ArrayList<Point> positions) {
		playerBlocks.clear();
		for (Point pos : positions) {
			playerBlocks.add(new PlayerBlock(pos.x, pos.y, Color.RED));
		}
		movement = Movement.STILL;
	}

	public boolean reachedGoal(ArrayList<Point> goalCoord, MapBlock[][] map) {
		boolean finished = true;
		ArrayList<PlayerBlock> bottomBlocks = getBottomBlocks(playerBlocks);
		for(int i=0; i < goalCoord.size(); i++) {
			PlayerBlock goal = new PlayerBlock( (int) goalCoord.get(i).getX(),  (int) goalCoord.get(i).getY() - 1);
			if(!playerBlocks.contains(goal)) {
				finished = false;
			}

		}
		for(int i=0; i < bottomBlocks.size(); i++) {
			if(!((map[(int) bottomBlocks.get(i).getWorldCoords().getY()][(int) bottomBlocks.get(i).getWorldCoords().getX() + 1]) instanceof SpaceBlock)) {
				finished = false;
			}
		}

		return finished;
	}

	public ArrayList<PlayerBlock> getBottomBlocks(ArrayList<PlayerBlock> playerBlock) {
		ArrayList<PlayerBlock> bottomBlocks = new ArrayList<PlayerBlock>();
		double max = 0;
		for(PlayerBlock block: playerBlock) {
			if(block.getWorldCoords().getY() > max) {
				max = block.getWorldCoords().getY();
			}
		}
		for(PlayerBlock block: playerBlock) {
			if(block.getWorldCoords().getY() == max) {
				bottomBlocks.add(block);
			}
		}

		return bottomBlocks;
	}



}