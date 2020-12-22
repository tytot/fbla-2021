import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Level extends JPanel implements KeyListener, MouseListener, MouseMotionListener, ActionListener {
	private Timer timer = new Timer(10, this);
	private static Insets insets;

	private MapBlock[][] map, startMap;
	private ArrayList<Point> startingPositions = new ArrayList<Point>();
	private ArrayList<Point> goalBlocks = new ArrayList<Point>();
	private JLabel[][] powerUps;
	private Player player = new Player();

	Level(String filePath) {
		setLayout(null);
		try {
			List<String> lines = Files.readAllLines(Paths.get(filePath));
			map = new MapBlock[lines.size()][lines.get(0).length()];
			startMap = new MapBlock[lines.size()][lines.get(0).length()];
			powerUps = new JLabel[map.length][map[0].length];
			for (int i = 0; i < lines.size(); i++) {
				char[] row = lines.get(i).toCharArray();
				for (int j = 0; j < row.length; j++) {
					char block = row[j];
					if (block == '.') {
						map[i][j] = new SpaceBlock();
					} else if (block == 'B') {
						map[i][j] = new SolidBlock();
					} else if(block == 'G') {
						map[i][j] = new GoalBlock();
						goalBlocks.add(new Point(j, i));
					}
					else if (block == 'R') {
						PowerUp p = new RedPowerUp();
						map[i][j] = p;
						ImageIcon imgIcon = new ImageIcon(new File(p.imagePath()).toURI().toURL());
						imgIcon.setImage(imgIcon.getImage().getScaledInstance(MapBlock.SIZE, MapBlock.SIZE,
								Image.SCALE_DEFAULT));
						JLabel label = new JLabel(imgIcon);
						powerUps[i][j] = label;
						label.setBounds(j * MapBlock.SIZE, i * MapBlock.SIZE, MapBlock.SIZE, MapBlock.SIZE);
						add(label);
					} else if (block == 'P') {
						map[i][j] = new SpaceBlock();
						player.addBlock(j, i, Color.RED);
						startingPositions.add(new Point(j, i));
					}
				}
			}
			for (int i = 0; i < map.length; i++) {
				for (int j = 0; j < map[i].length; j++) {
					startMap[i][j] = map[i][j];
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[0].length; j++) {
				MapBlock block = map[i][j];
				g2.setColor(block.getColor());
				g2.fillRect(j * MapBlock.SIZE, i * MapBlock.SIZE, MapBlock.SIZE, MapBlock.SIZE);
			}
		}
		g2.setColor(Color.RED);
		for (PlayerBlock pBlock : player.getBlocks()) {
			Point pixelCoords = pBlock.getPixelCoords();
			g2.fillRect(pixelCoords.x, pixelCoords.y, PlayerBlock.SIZE, PlayerBlock.SIZE);
		}
		int state = player.getState();
		if (state == Player.BUILDING) {
			PlayerBlock highlightedBlock = player.getHighlightedBlock();
			if (highlightedBlock != null) {
				Point highlightedPoint = highlightedBlock.getPixelCoords();
				g2.setColor(Color.PINK);
				g2.fillRect(highlightedPoint.x, highlightedPoint.y, PlayerBlock.SIZE, PlayerBlock.SIZE);
			}
		} else if (state == Player.SPLITTING) {
			Point[] splitLine = player.getSplitLine();
			if (splitLine != null) {
				g2.setColor(Color.WHITE);
				g2.setStroke(new BasicStroke(4, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER));
				g2.drawLine(splitLine[0].x * PlayerBlock.SIZE, splitLine[0].y * PlayerBlock.SIZE,
						splitLine[1].x * PlayerBlock.SIZE, splitLine[1].y * PlayerBlock.SIZE);
			}
		} else if (state == Player.CHOOSING) {
			int chosenSide = player.getChosenSide();
			if (chosenSide != -1) {
				g2.setColor(Color.PINK);
				for (PlayerBlock pBlock : player.getSplitBlocks(chosenSide)) {
					Point pixelCoords = pBlock.getPixelCoords();
					g2.fillRect(pixelCoords.x, pixelCoords.y, PlayerBlock.SIZE, PlayerBlock.SIZE);
				}
			}
		}
		timer.start();
	}

	public void keyPressed(KeyEvent e) {
		if (player.getState() == Player.NORMAL) {
			if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
				player.setMovement(Movement.RIGHT);
			} else if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
				player.setMovement(Movement.LEFT);
			} else if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
				ArrayList<PlayerBlock> mergedBlocks = player.merge(map);
				for (PlayerBlock pBlock : mergedBlocks) {
					Point worldCoords = pBlock.getWorldCoords();
					map[worldCoords.y][worldCoords.x] = new SpaceBlock();
				}
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (player.getState() == Player.NORMAL) {
			if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
				player.setMovement(Movement.STILL_RIGHT);
			} else if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
				player.setMovement(Movement.STILL_LEFT);
			} else if (e.getKeyCode() == KeyEvent.VK_SPACE && player.getMovement() == Movement.STILL
					&& player.isFalling()) {
				player.startBuilding(map);
			}
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		if (player.getMovement() == Movement.STILL && player.isFalling()) {
			int state = player.getState();
			if (state == Player.NORMAL) {
				if (SwingUtilities.isRightMouseButton(arg0)) {
					player.startBuilding(map);
				} else if (SwingUtilities.isMiddleMouseButton(arg0)) {
					player.startSplitting();
				}
			} else if (state == Player.BUILDING) {
				if (player.getHighlightedBlock() != null) {
					player.confirmBuild();
				}
			} else if (state == Player.SPLITTING) {
				if (player.getSplitLine() != null) {
					player.splitIntoSides();
				}
			} else if (state == Player.CHOOSING) {
				int chosenSide = player.getChosenSide();
				if (chosenSide != -1) {
					ArrayList<PlayerBlock> abandonedBlocks = player.chooseSide(chosenSide);
					for (PlayerBlock pBlock : abandonedBlocks) {
						Point worldCoords = pBlock.getWorldCoords();
						map[worldCoords.y][worldCoords.x] = new CryingPlayerBlock();
					}
				}
			}
			mouseAction(arg0);
		}
	}
	
	public void resetLevel() {
		for (int i = 0; i < startMap.length; i++) {
			for (int j = 0; j < startMap[i].length; j++) {
				map[i][j] = startMap[i][j];
			}
		}
		player.resetPositions(startingPositions);
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		mouseAction(arg0);
	}

	private void mouseAction(MouseEvent e) {
		int x = e.getX() - insets.left;
		int y = e.getY() - insets.top;
		int state = player.getState();
		if (state == Player.BUILDING) {
			player.highlightBlock(x, y);
		} else if (state == Player.SPLITTING) {
			player.highlightSplitLine(map, x, y);
		} else if (state == Player.CHOOSING) {
			player.setChosenSide(x, y);
		}
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		player.move(map);
		if (player.isOutOfBounds(map)) {
			resetLevel();
		}
		if(player.reachedGoal(goalBlocks, map)) {
			System.out.println("checkpoint");
		}
		repaint();
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(map[0].length * MapBlock.SIZE, map.length * MapBlock.SIZE);
	}

	private static void runGame() {
		Level level = new Level("levels/level1.txt");

		JFrame frame = new JFrame("Level 1");
		frame.addKeyListener(level);
		frame.addMouseListener(level);
		frame.addMouseMotionListener(level);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(level);
		frame.pack();
		frame.setVisible(true);
		insets = frame.getInsets();
	}

	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				runGame();
			}
		});
	}
}