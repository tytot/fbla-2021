import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Level extends JPanel implements MouseListener, MouseMotionListener, ActionListener {
	private JFrame frame;
	private JPanel last;
	private int levelNumber;
	
	private Timer timer = new Timer(10, this);
	private Timer fadeTimer = new Timer(10, this);

	private MapBlock[][] map, startMap;
	private ArrayList<Point> startingPositions = new ArrayList<Point>();
	private ArrayList<Point> goalBlocks = new ArrayList<Point>();
	private JLabel[][] powerUps;
	private int[] storedPowerUps = { 0, 0, 0 };
	private Player player = new Player();
	private boolean firstMovement = true;
	private long startTime, endTime;
	private boolean complete = false;
	
	private JLabel back, reset, growCounter, splitCounter, mergeCounter, next, retry;
	private JLabel[] hearts = new JLabel[3];
	private final Font labelFont = new Font("Courier New", Font.BOLD, MapBlock.SIZE);
	private final Color textColor = Color.WHITE;
	private Color fadeIn = null;
	
//	private final HashMap<Point, Rectangle> sides = new HashMap<Point, Rectangle>() {
//		{
//			put(new Point(-1, -1), new Rectangle(0, 0, PlayerBlock.SIZE / 5, PlayerBlock.SIZE / 5));
//			put(new Point(0, -1), new Rectangle(0, 0, PlayerBlock.SIZE, PlayerBlock.SIZE / 5));
//			put(new Point(1, -1), new Rectangle(4 * PlayerBlock.SIZE / 5, 0, PlayerBlock.SIZE / 5, PlayerBlock.SIZE / 5));
//			put(new Point(1, 0), new Rectangle(4 * PlayerBlock.SIZE / 5, 0, PlayerBlock.SIZE / 5, PlayerBlock.SIZE));
//			put(new Point(1, 1), new Rectangle(4 * PlayerBlock.SIZE / 5, 4 * PlayerBlock.SIZE / 5, PlayerBlock.SIZE / 5, PlayerBlock.SIZE / 5));
//			put(new Point(0, 1), new Rectangle(0, 4 * PlayerBlock.SIZE / 5, PlayerBlock.SIZE, PlayerBlock.SIZE / 5));
//			put(new Point(-1, 1), new Rectangle(0, 4 * PlayerBlock.SIZE / 5, PlayerBlock.SIZE / 5, PlayerBlock.SIZE / 5));
//			put(new Point(-1, 0), new Rectangle(0, 0, PlayerBlock.SIZE / 5, PlayerBlock.SIZE));
//		}
//	};

	Level(int levelNumber, JFrame frame, JPanel last) {
		this.frame = frame;
		this.last = last;
		this.levelNumber = levelNumber;
		setLayout(null);
		addMouseListener(this);
		addMouseMotionListener(this);
		setFocusable(true);
		requestFocus();
		requestFocusInWindow();
		try {
			List<String> lines = Files.readAllLines(Paths.get("levels/level" + levelNumber + ".txt"));
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
					} else if (block == 'C') {
						map[i][j] = new CryingPlayerBlock();
					} else if (block == 'G') {
						map[i][j] = new GoalBlock();
						goalBlocks.add(new Point(j, i));
					} else if (block == 'R') {
						map[i][j] = new GrowPowerUp();
					} else if (block == 'S') {
						map[i][j] = new SplitPowerUp();
					} else if (block == 'M') {
						map[i][j] = new MergePowerUp();
					} else if (block == 'P') {
						map[i][j] = new SpaceBlock();
						player.addBlock(j, i, Color.RED);
						startingPositions.add(new Point(j, i));
					}
				}
			}
			addLabels();
			resetPowerUps();
			for (int i = 0; i < map.length; i++) {
				for (int j = 0; j < map[i].length; j++) {
					startMap[i][j] = map[i][j];
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		bindKeys();
		timer.start();
	}
	
	private void addLabels() throws MalformedURLException {
		back = new JLabel("BACK");
		back.setForeground(textColor);
		back.setFont(labelFont);
		back.addMouseListener(this);
		back.setBounds(MapBlock.SIZE, (map.length - 1) * MapBlock.SIZE, 3 * MapBlock.SIZE, MapBlock.SIZE);
		add(back);
		
		reset = new JLabel("RESET");
		reset.setForeground(textColor);
		reset.setFont(labelFont);
		reset.addMouseListener(this);
		reset.setBounds(MapBlock.SIZE, 0, 3 * MapBlock.SIZE, MapBlock.SIZE);
		add(reset);
		
		ImageIcon gImgIcon = new ImageIcon(new File(new GrowPowerUp().imagePath()).toURI().toURL());
		gImgIcon.setImage(gImgIcon.getImage().getScaledInstance(MapBlock.SIZE, MapBlock.SIZE, Image.SCALE_DEFAULT));
		JLabel growIcon = new JLabel(gImgIcon);
		growIcon.setBounds(5 * MapBlock.SIZE, 0, MapBlock.SIZE, MapBlock.SIZE);
		add(growIcon);
		growCounter = new JLabel("x0");
		growCounter.setForeground(textColor);
		growCounter.setFont(labelFont);
		growCounter.addMouseListener(this);
		growCounter.setBounds(6 * MapBlock.SIZE, 0, 2 * MapBlock.SIZE, MapBlock.SIZE);
		add(growCounter);
		
		ImageIcon sImgIcon = new ImageIcon(new File(new SplitPowerUp().imagePath()).toURI().toURL());
		sImgIcon.setImage(sImgIcon.getImage().getScaledInstance(MapBlock.SIZE, MapBlock.SIZE, Image.SCALE_DEFAULT));
		JLabel splitIcon = new JLabel(sImgIcon);
		splitIcon.setBounds(8 * MapBlock.SIZE, 0, MapBlock.SIZE, MapBlock.SIZE);
		add(splitIcon);
		splitCounter = new JLabel("x0");
		splitCounter.setForeground(textColor);
		splitCounter.setFont(labelFont);
		splitCounter.addMouseListener(this);
		splitCounter.setBounds(9 * MapBlock.SIZE, 0, 2 * MapBlock.SIZE, MapBlock.SIZE);
		add(splitCounter);
		
		ImageIcon mImgIcon = new ImageIcon(new File(new MergePowerUp().imagePath()).toURI().toURL());
		mImgIcon.setImage(mImgIcon.getImage().getScaledInstance(MapBlock.SIZE, MapBlock.SIZE, Image.SCALE_DEFAULT));
		JLabel mergeIcon = new JLabel(mImgIcon);
		mergeIcon.setBounds(11 * MapBlock.SIZE, 0, MapBlock.SIZE, MapBlock.SIZE);
		add(mergeIcon);
		mergeCounter = new JLabel("x0");
		mergeCounter.setForeground(textColor);
		mergeCounter.setFont(labelFont);
		mergeCounter.addMouseListener(this);
		mergeCounter.setBounds(12 * MapBlock.SIZE, 0, 2 * MapBlock.SIZE, MapBlock.SIZE);
		add(mergeCounter);
		
		for (int i = 0; i < hearts.length; i++) {
			ImageIcon imgIcon = new ImageIcon(new File("res/img/heart.png").toURI().toURL());
			imgIcon.setImage(
					imgIcon.getImage().getScaledInstance(MapBlock.SIZE, MapBlock.SIZE, Image.SCALE_DEFAULT));
			JLabel label = new JLabel(imgIcon);
			hearts[i] = label;
			label.setBounds((map[0].length - 2 - i) * MapBlock.SIZE, 0, MapBlock.SIZE, MapBlock.SIZE);
			add(label);
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
		ArrayList<PlayerBlock> pBlocks = player.getBlocks();
		for (PlayerBlock pBlock : pBlocks) {
			g2.setColor(Color.RED);
			Point pixelCoords = pBlock.getPixelCoords();
			g2.fillRect(pixelCoords.x, pixelCoords.y, PlayerBlock.SIZE, PlayerBlock.SIZE);
//			g2.setColor(Color.RED.darker());
//			Point worldCoords = pBlock.getWorldCoords();
//			for (Point offset : sides.keySet()) {
//				if (!pBlocks.contains(new PlayerBlock(worldCoords.x + offset.x, worldCoords.y + offset.y))) {
//					Rectangle rect = sides.get(offset);
//					g2.fillRect(pixelCoords.x + rect.x, pixelCoords.y + rect.y, rect.width, rect.height);
//				}
//			}
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
		if (fadeIn != null) {
			g2.setColor(fadeIn);
			g2.fillRect(0, 0, getWidth(), getHeight());
		}
	}
	
	public void bindKeys() {
        registerKeyBinding(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0, false), "left-press", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (player.getState() == Player.NORMAL) {
					player.setMovement(Movement.LEFT);
					if (firstMovement) {
						startTime = System.currentTimeMillis();
						firstMovement = false;
					}
				}
			}
		});
        registerKeyBinding(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0, false), "right-press", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (player.getState() == Player.NORMAL) {
					player.setMovement(Movement.RIGHT);
					if (firstMovement) {
						startTime = System.currentTimeMillis();
						firstMovement = false;
					}
				}
			}
		});
        registerKeyBinding(KeyStroke.getKeyStroke(KeyEvent.VK_M, 0, false), "shift-press", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (player.getState() == Player.NORMAL) {
					ArrayList<PlayerBlock> mergedBlocks = player.merge(map);
					if (mergedBlocks.size() > 0 && storedPowerUps[2] > 0) {
						for (PlayerBlock pBlock : mergedBlocks) {
							Point worldCoords = pBlock.getWorldCoords();
							map[worldCoords.y][worldCoords.x] = new SpaceBlock();
							player.addBlock(worldCoords.x, worldCoords.y, Color.RED);
						}
						mergeCounter.setText("x" + --storedPowerUps[2]);
					}
				}
			}
		});
        registerKeyBinding(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0, true), "left-release", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (player.getState() == Player.NORMAL) {
					if (player.getMovement() != Movement.RIGHT) {
						player.setMovement(Movement.STILL_LEFT);
					}
				}
			}
		});
        registerKeyBinding(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0, true), "right-release", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (player.getState() == Player.NORMAL) {
					if (player.getMovement() != Movement.LEFT) {
						player.setMovement(Movement.STILL_RIGHT);
					}
				}
			}
		});
	}

    public void registerKeyBinding(KeyStroke keyStroke, String name, Action action) {
        InputMap im = getInputMap(WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = getActionMap();

        im.put(keyStroke, name);
        am.put(name, action);
    }

	@Override
	public void mouseClicked(MouseEvent arg0) {
		if (arg0.getComponent() == reset) {
			if (!complete) {
				resetLevel();
				resetPowerUps();
			}
		} else if (arg0.getComponent() == next) {
			frame.setContentPane(new Level(++levelNumber, frame, last));
			frame.revalidate();
			frame.repaint();
		} else if (arg0.getComponent() == retry) {
			frame.setContentPane(new Level(levelNumber, frame, last));
			frame.revalidate();
			frame.repaint();
		} else if (arg0.getComponent() == back) {
			frame.setContentPane(last);
			frame.revalidate();
			frame.repaint();
		} else if (player.getMovement() == Movement.STILL && !player.isFalling()) {
			int state = player.getState();
			if (state == Player.NORMAL) {
				if (SwingUtilities.isRightMouseButton(arg0) && storedPowerUps[0] > 0) {
					player.startBuilding(map);
				} else if (SwingUtilities.isMiddleMouseButton(arg0) && storedPowerUps[1] > 0) {
					player.startSplitting();
				}
			} else if (state == Player.BUILDING) {
				if (player.getHighlightedBlock() != null) {
					player.confirmBuild();
					growCounter.setText("x" + --storedPowerUps[0]);
				}
			} else if (state == Player.SPLITTING) {
				if (player.getSplitLine() != null) {
					player.splitIntoSides();
					splitCounter.setText("x" + --storedPowerUps[1]);
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
		SoundEffect.BG.stop();
		SoundEffect.BG.play(true);
		resetPowerUps();
	}

	private void resetPowerUps() {
		try {
			for (int i = 0; i < map.length; i++) {
				for (int j = 0; j < map[0].length; j++) {
					if (map[i][j] instanceof PowerUp && powerUps[i][j] == null) {
						ImageIcon imgIcon = new ImageIcon(new File(((PowerUp) map[i][j]).imagePath()).toURI().toURL());
						imgIcon.setImage(
								imgIcon.getImage().getScaledInstance(MapBlock.SIZE, MapBlock.SIZE, Image.SCALE_DEFAULT));
						JLabel label = new JLabel(imgIcon);
						powerUps[i][j] = label;
						label.setBounds(j * MapBlock.SIZE, i * MapBlock.SIZE, MapBlock.SIZE, MapBlock.SIZE);
						add(label);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		storedPowerUps = new int[] {0,0,0};
		growCounter.setText("x0");
		splitCounter.setText("x0");
		mergeCounter.setText("x0");
	}

	public void pickUpPowerUp() {
		for (int i = 0; i < player.getBlocks().size(); i++) {
			PlayerBlock pBlock = player.getBlocks().get(i);
			Point check = new Point((int) (Math.round(pBlock.getPixelCoords().getX() / MapBlock.SIZE)),
					pBlock.getWorldCoords().y);
			if (player.isValidBlock(map, check.x, check.y) && powerUps[check.y][check.x] != null) {
				if (map[check.y][check.x] instanceof GrowPowerUp) {
					growCounter.setText("x" + ++storedPowerUps[0]);
				} else if (map[check.y][check.x] instanceof SplitPowerUp) {
					splitCounter.setText("x" + ++storedPowerUps[1]);
				} else if (map[check.y][check.x] instanceof MergePowerUp) {
					mergeCounter.setText("x" + ++storedPowerUps[2]);
				}
				remove(powerUps[check.y][check.x]);
				powerUps[check.y][check.x] = null;
			}
		}
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
		int x = e.getX();
		int y = e.getY();
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
		if (arg0.getSource() == timer) {
			player.move(map);
			pickUpPowerUp();
			if (player.isOutOfBounds(map)) {
				resetLevel();
				if (hearts[0] != null) {
					remove(hearts[0]);
					hearts[0] = null;
				} else if (hearts[1] != null) {
					remove(hearts[1]);
					hearts[1] = null;
				} else if (hearts[2] != null) {
					remove(hearts[2]);
					hearts[2] = null;
					fadeTimer.start();
				}
			}
			if (!complete && player.reachedGoal(goalBlocks, map)) {
				complete = true;
				SoundEffect.PLATE_CLICK.play(false);
				endTime = System.currentTimeMillis();
				fadeTimer.start();
			}
			timer.start();
		} else if (arg0.getSource() == fadeTimer) {
			if (fadeIn == null) {
				fadeIn = new Color(0, 0, 0, 1);
				remove(reset);
				for (int i = 0; i < powerUps.length; i++) {
					for (int j = 0; j < powerUps.length; j++) {
						if (powerUps[i][j] != null) {
							remove(powerUps[i][j]);
						}
					}
				}
			} else if (fadeIn.getAlpha() < 255) {
                fadeIn = new Color(fadeIn.getRed(), fadeIn.getGreen(), fadeIn.getBlue(), fadeIn.getAlpha() + 2);
                fadeTimer.start();
            } else {
            	if (fadeTimer.isRunning()) {
            		fadeTimer.stop();
            	}
            	if (complete) {
	        		JLabel finishLabel = new JLabel("LEVEL COMPLETE", SwingConstants.CENTER);
	        		finishLabel.setForeground(Color.WHITE);
	        		finishLabel.setFont(labelFont.deriveFont(100f));
	        		finishLabel.setBounds(0, 4 * MapBlock.SIZE, getWidth(), 2 * MapBlock.SIZE);
	        		add(finishLabel);
	        		JLabel timeLabel = new JLabel("in " + (endTime - startTime) / 1000.0 + " s.", SwingConstants.CENTER);
	        		timeLabel.setForeground(Color.WHITE);
	        		timeLabel.setFont(labelFont);
	        		timeLabel.setBounds(0, 6 * MapBlock.SIZE, getWidth(), MapBlock.SIZE);
	        		add(timeLabel);
	        		JLabel gTimeLabel = new JLabel("Total time: " + (System.currentTimeMillis() - Window.startTime) / 1000.0 + " s.", SwingConstants.CENTER);
	        		gTimeLabel.setForeground(Color.WHITE);
	        		gTimeLabel.setFont(labelFont);
	        		gTimeLabel.setBounds(0, 8 * MapBlock.SIZE, getWidth(), MapBlock.SIZE);
	        		add(gTimeLabel);
	        		if (levelNumber == 23) {
	        			Window.endTime = System.currentTimeMillis();
	        		}
	        		next = new JLabel("NEXT LEVEL", SwingConstants.CENTER);
	        		next.setForeground(Color.WHITE);
	        		next.setFont(labelFont);
	        		next.setBounds(0, (map.length - 2) * MapBlock.SIZE, getWidth(), MapBlock.SIZE);
	        		next.addMouseListener(this);
	        		add(next);
            	} else {
	        		JLabel failLabel = new JLabel("YOU FAILED.", SwingConstants.CENTER);
	        		failLabel.setForeground(Color.WHITE);
	        		failLabel.setFont(labelFont.deriveFont(100f));
	        		failLabel.setBounds(0, 4 * MapBlock.SIZE, getWidth(), 2 * MapBlock.SIZE);
	        		add(failLabel);
	        		retry = new JLabel("RETRY", SwingConstants.CENTER);
	        		retry.setForeground(Color.WHITE);
	        		retry.setFont(labelFont);
	        		retry.setBounds(0, (map.length - 2) * MapBlock.SIZE, getWidth(), MapBlock.SIZE);
	        		retry.addMouseListener(this);
	        		add(retry);
            	}
            }
		}
		repaint();
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(map[0].length * MapBlock.SIZE, map.length * MapBlock.SIZE);
	}

	private static void runGame() {
		JFrame frame = new JFrame("Level 15");
		Level level = new Level(15, frame, null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.addMouseListener(level);
		frame.addMouseMotionListener(level);
		frame.add(level);
		frame.pack();
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				runGame();
			}
		});
	}
}