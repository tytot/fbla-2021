import java.awt.Image;
import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Map {

	private char[][] mapChars;
	private MapBlock[][] map;
	private ArrayList<Point> startingPositions = new ArrayList<Point>();
	private ArrayList<Point> goalBlocks = new ArrayList<Point>();

	Map(List<String> lines) throws IOException {
		String firstLine = "";
		for (int i = 0; i < lines.get(0).length(); i++) {
			firstLine += ".";
		}
		lines.add(0, firstLine);
		String[] lineArray = lines.toArray(new String[0]);
		mapChars = new char[lines.size()][];
		for (int i = 0; i < lineArray.length; i++) {
			String line = lineArray[i];
			mapChars[i] = line.toCharArray();
		}
		map = new MapBlock[lines.size()][lines.get(0).length()];
		for (int i = 0; i < lines.size(); i++) {
			char[] row = lines.get(i).toCharArray();
			for (int j = 0; j < row.length; j++) {
				char block = row[j];
				if (block == '.') {
					map[i][j] = new SpaceBlock();
				} else if (block == 'B') {
					map[i][j] = new SolidBlock(
					relativePosition(i, j));
				} else if (block == 'C') {
					map[i][j] = new CryingPlayerBlock(
					relativePosition(i, j));
				} else if (block == 'G') {
					map[i][j] = new GoalBlock();
					goalBlocks.add(new Point(j, i));
				} else if (block == 'R') {
					map[i][j] = new GrowPowerUp();
				} else if (block == 'S') {
					map[i][j] = new SplitPowerUp();
				} else if (block == 'M') {
					map[i][j] = new MergePowerUp();
				} else if (block == 'Q') {
					map[i][j] = new QuadPowerUp();
				} else if (block == 'P') {
					map[i][j] = new SpaceBlock();
					startingPositions.add(new Point(j, i));
				}
			}
		}
	}

	Map(Map other) {
		mapChars = new char[other
		.getChars().length][other.getChars()[0].length];
		for (int i = 0; i < other.getChars().length; i++) {
			for (int j = 0; j < other.getChars()[0].length; j++) {
				mapChars[i][j] = other.getChars()[i][j];
			}
		}
		map = new MapBlock[other
		.getBlocks().length][other.getBlocks()[0].length];
		for (int i = 0; i < other.getBlocks().length; i++) {
			for (int j = 0; j < other.getBlocks()[0].length; j++) {
				map[i][j] = other.getBlocks()[i][j];
			}
		}
		startingPositions = other.getStartingPositions();
		goalBlocks = other.getGoalBlocks();
	}

	public int relativePosition(int i, int j) {
		char c = mapChars[i][j];
		boolean left = !isValidBlock(j - 1, i)
		|| mapChars[i][j - 1] == c;
		boolean right = !isValidBlock(j + 1, i)
		|| mapChars[i][j + 1] == c;
		boolean top = !isValidBlock(j, i - 1)
		|| mapChars[i - 1][j] == c;
		boolean bottom = !isValidBlock(j, i + 1)
		|| mapChars[i + 1][j] == c;
		if (left) {
			// block to left
			if (right) {
				// block to right
				if (top) {
					// block on top
					return Block.CENTER;
				} else {
					// no block on top
					return Block.MIDDLE;
				}
			} else {
				// no block to right
				if (top) {
					// block on top
					if (bottom) {
						// block below
						return Block.CENTER;
					} else {
						// no block below
						return Block.BOTTOM_RIGHT;
					}
				} else {
					// no block on top
					if (bottom) {
						// block below
						return Block.TOP_RIGHT;
					} else {
						// no block below
						return Block.RIGHT;
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
						return Block.CENTER;
					} else {
						// no block below
						return Block.BOTTOM_LEFT;
					}
				} else {
					// no block on top
					if (bottom) {
						// block below
						return Block.TOP_LEFT;
					} else {
						// no block below
						return Block.LEFT;
					}
				}
			} else {
				// no block to right
				if (top) {
					// block on top
					if (bottom) {
						// block below
						return Block.CENTER;
					} else {
						// no block below
						return Block.BOTTOM;
					}
				} else {
					// no block on top
					if (bottom) {
						// block below
						return Block.TOP;
					} else {
						// no block below
						return Block.ALONE;
					}
				}
			}
		}
	}

	public boolean isValidBlock(int x, int y) {
		return y >= 0 && y < map.length && x >= 0
		&& x < map[0].length;
	}

	public char[][] getChars() {
		return mapChars;
	}

	public MapBlock[][] getBlocks() {
		return map;
	}

	public ArrayList<Point> getStartingPositions() {
		return startingPositions;
	}

	public ArrayList<Point> getGoalBlocks() {
		return goalBlocks;
	}
}
