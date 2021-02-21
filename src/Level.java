import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Level extends JPanel implements MouseListener, MouseMotionListener, ActionListener {
	
	private JFrame frame;
	private JPanel last;
	private int levelNumber;
	private Theme theme;
	private boolean timeTrial;
	
	private Timer timer = new Timer(25, this);
	private int xOffset;
	private Timer flashTimer = new Timer(500, this);
	private int numFlashes = 0;
	private Timer changeTimer = new Timer(500, this);

	private Map map, startMap;
	private int[] storedPowerUps = { 0, 0, 0 };
	private double powerUpAngle = 0;
	
	private Player player = new Player();
	private int numHearts = 3;
	private boolean firstMovement = true;
	private boolean complete = false;
	
	private long openTime, startTime, endTime, changeTime, fadeTime;
	private int cumulativeTime;
	
	private Rectangle beamSpawnArea;
	private List<int[]> beams = new ArrayList<int[]>();
	
	private JButton lastLevel, nextLevel, exit, reset, menu;
	private JLabel timerLabel;
	private JLabel growCount, splitCount, mergeCount;
	private JLabel[] hearts = new JLabel[3];
	private List<JPanel> helpPanels = new ArrayList<JPanel>();
	
	private String sekrit = "";
	
	Level(int levelNumber, boolean enterRight, boolean timeTrial, int cumulativeTime, JFrame frame, JPanel last) {
		this.frame = frame;
		this.last = last;
		this.levelNumber = levelNumber;
		this.xOffset = enterRight ? Window.DIMENSIONS.width : -Window.DIMENSIONS.width;
		if (levelNumber <= 6) {
			this.theme = new PlainTheme();
		} else if (levelNumber <= 12) {
			this.theme = new RainyTheme();
		} else if (levelNumber <= 18) {
			this.theme = new SnowyTheme();
		} else {
			this.theme = new SandyTheme();
		}
		this.timeTrial = timeTrial;
		this.cumulativeTime = cumulativeTime;
		
		setLayout(null);
		addMouseListener(this);
		addMouseMotionListener(this);
		setFocusable(true);
		requestFocus();
		requestFocusInWindow();
		try {
			List<String> lines = Files.readAllLines(Paths.get("levels/level" + levelNumber + ".txt"));
			map = new Map(theme, lines);
			startMap = new Map(map);
			if (!timeTrial && levelNumber == 24) {
				Point goalBlock = map.getGoalBlocks().get(0);
				Point goalPoint = new Point(Block.SIZE * goalBlock.x + Block.SIZE / 2, Block.SIZE * goalBlock.y + (Block.SIZE - GoalBlock.PRESSED_SIZE) / 2);
				int radius = (int) Math.ceil(Math.sqrt(Math.pow(Window.DIMENSIONS.width, 2) + Math.pow(Window.DIMENSIONS.height, 2)));
				beamSpawnArea = new Rectangle(goalPoint.x - radius, goalPoint.y - radius, 2 * radius, 2 * radius);
			}
			for (Point startPos : map.getStartingPositions()) {
				player.addBlock(startPos.x, startPos.y);
			}
			player.calculateRelativePositions(player.getBlocks());
			initializeUI();
		} catch (IOException e) {
			e.printStackTrace();
		}
		openTime = System.currentTimeMillis();
		timer.start();
	}
	
	private void initializeUI() throws IOException {
		exit = UIFactory.createButton(new ImageIcon("img/ui/exit.png"), new ImageIcon("img/ui/exitPressed.png"), 25, 15);
		exit.addActionListener(this);
		add(exit);
		lastLevel = UIFactory.createButton(new ImageIcon("img/ui/lastLevel.png"), new ImageIcon("img/ui/lastLevelPressed.png"), 100, 15);
		lastLevel.addActionListener(this);
		if (!timeTrial || levelNumber == 1) {
			lastLevel.setEnabled(false);
		}
		add(lastLevel);
		add(UIFactory.createLabel("Level " + levelNumber, 28, new Rectangle(155, 20, 190, 49)));
		add(UIFactory.createLabel(new ImageIcon("img/ui/levelBar.png"), 155, 15));
		nextLevel = UIFactory.createButton(new ImageIcon("img/ui/nextLevel.png"), new ImageIcon("img/ui/nextLevelPressed.png"), 355, 15);
		if (!timeTrial || levelNumber == 24) {
			nextLevel.setEnabled(false);
		}
		nextLevel.addActionListener(this);
		add(nextLevel);
		reset = UIFactory.createButton(new ImageIcon("img/ui/reset.png"), new ImageIcon("img/ui/resetPressed.png"), 425, 15);
		reset.addActionListener(this);
		add(reset);
		
		int width = getPreferredSize().width;
		
		timerLabel = UIFactory.createOutlinedLabel("0:00.0", width / 2 - 77, 20);
		add(timerLabel);
		
		add(UIFactory.createLabel(new ImageIcon("img/sprites/powerups/grow.png"), width - 480, 0));
		growCount = UIFactory.createOutlinedLabel("x0", width - 410, 20);
		add(growCount);
		add(UIFactory.createLabel(new ImageIcon("img/sprites/powerups/split.png"), width - 320, 0));
		splitCount = UIFactory.createOutlinedLabel("x0", width - 250, 20);
		add(splitCount);
		add(UIFactory.createLabel(new ImageIcon("img/sprites/powerups/merge.png"), width - 160, 0));
		mergeCount = UIFactory.createOutlinedLabel("x0", width - 90, 20);
		add(mergeCount);
		
		ImageIcon heartIcon = new ImageIcon("img/ui/hud_heartFull.png");
		ImageIcon emptyHeartIcon = new ImageIcon("img/ui/hud_heartEmpty.png");
		for (int i = 0; i < 3; i++) {
			add(UIFactory.createLabel(emptyHeartIcon, (int) (width / 2 + (-1.5 + i) * emptyHeartIcon.getIconWidth()), 70));
			hearts[i] = UIFactory.createLabel(heartIcon, (int) (width / 2 + (-1.5 + i) * heartIcon.getIconWidth()), 70);
			add(hearts[i]);
		}
		
		if (levelNumber == 1) {
			helpPanels.add(UIFactory.createHelpPanel("Press A or left arrow key to move left. Press D or right arrow key to move right.", 300, 150));
		} else if (levelNumber == 2) {
			helpPanels.add(UIFactory.createHelpPanel("Hover your cursor next to your block. Click to grow there.", new ImageIcon("img/sprites/powerups/grow.png"), 900, 150));
		} else if (levelNumber == 7) {
			helpPanels.add(UIFactory.createHelpPanel("Right click to begin to split.", new ImageIcon("img/sprites/powerups/split.png"), 250, 350));
			helpPanels.add(UIFactory.createHelpPanel("Click the line where you want to split.", new ImageIcon("img/sprites/powerups/split.png"), 250, 350));
			helpPanels.add(UIFactory.createHelpPanel("Choose a side to keep.", new ImageIcon("img/sprites/powerups/split.png"), 250, 350));
		} else if (levelNumber == 15) {
			helpPanels.add(UIFactory.createHelpPanel("Press SHIFT to merge with adjacent abandoned blocks.", new ImageIcon("img/sprites/powerups/merge.png"), 950, 300));
		} else if (levelNumber == 4) {
			helpPanels.add(UIFactory.createHelpPanel("To complete a level, all blocks must either be on a button or off the ground.", 100, 600));
		}
	}
	
	private void updateHUD() {
		int elapsed = cumulativeTime + (int) ((endTime - startTime) / 100);
		int minutes = elapsed / 600, seconds = (elapsed % 600) / 10, decis = (elapsed % 600) % 10;
		String time = minutes + ":" + String.format("%02d", seconds) + "." + decis;
		
		timerLabel.setText(time);
		Dimension timerSize = timerLabel.getPreferredSize();
		timerLabel.setBounds((getPreferredSize().width - timerSize.width) / 2, 20, timerSize.width, timerSize.height);
		
		growCount.setText("x" + storedPowerUps[0]);
		splitCount.setText("x" + storedPowerUps[1]);
		mergeCount.setText("x" + storedPowerUps[2]);
	}
    
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.drawImage(theme.getBackgroundImage(), 0, 0, null);
		
		if (!timeTrial && complete && levelNumber == 24) {
			g2.translate((int) (21 * Math.random()) - 10, (int) (21 * Math.random()) - 10);
		}
		theme.drawParticles(g2, player, map);
		
		MapBlock[][] blocks = map.getBlocks();
		for (int i = 0; i < blocks.length; i++) {
			for (int j = 0; j < blocks[0].length; j++) {
				MapBlock block = blocks[i][j];
				Image img = block.getImage();
				if (img != null) {
					if (block instanceof PowerUp) {
						AffineTransform old = g2.getTransform();
						g2.rotate(powerUpAngle, (j + 0.5) * Block.SIZE + xOffset, (i + 0.5) * Block.SIZE);
						g2.drawImage(img, j * Block.SIZE + xOffset, i * Block.SIZE, null);
						g2.setTransform(old);
					} else {
						g2.drawImage(img, j * Block.SIZE + xOffset, i * Block.SIZE, null);
					}
				}
			}
		}
		ArrayList<PlayerBlock> pBlocks = player.getBlocks();
		for (PlayerBlock pBlock : pBlocks) {
			Point pixelCoords = pBlock.getPixelCoords();
			if (!timeTrial && complete && levelNumber == 24) {
				g2.setColor(Color.WHITE);
				g2.fillRoundRect(pixelCoords.x, pixelCoords.y, Block.SIZE, Block.SIZE - GoalBlock.PRESSED_SIZE, 8, 8);
			} else {
				g2.drawImage(pBlock.getImage(), pixelCoords.x + xOffset, pixelCoords.y, null);
			}
		}
		int state = player.getState();
		if (state == Player.NORMAL) {
			PlayerBlock highlightedBlock = player.getHighlightedBlock();
			if (highlightedBlock != null) {
				Point highlightedPoint = highlightedBlock.getPixelCoords();
				g2.drawImage(highlightedBlock.getImage(), highlightedPoint.x + xOffset, highlightedPoint.y, null);
			}
		} else if (state == Player.SPLITTING) {
			Point[] splitLine = player.getSplitLine();
			if (splitLine != null) {
				g2.setColor(Color.WHITE);
				g2.setStroke(new BasicStroke(4, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER));
				g2.drawLine(splitLine[0].x * Block.SIZE + xOffset, splitLine[0].y * Block.SIZE,
						splitLine[1].x * Block.SIZE + xOffset, splitLine[1].y * Block.SIZE);
			}
		} else if (state == Player.CHOOSING) {
			int chosenSide = player.getChosenSide();
			if (chosenSide != -1) {
				AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f);
				g2.setComposite(ac);
				for (PlayerBlock pBlock : player.getSplitBlocks(chosenSide == 0 ? 1 : 0)) {
					Point pixelCoords = pBlock.getPixelCoords();
					g2.drawImage(pBlock.getImage(), pixelCoords.x + xOffset, pixelCoords.y, null);
				}
				g2.setComposite(AlphaComposite.SrcOver);
			}
		}
		if (numHearts == 0) {
			drawGameOver(g2);
		} else if (!timeTrial && complete && levelNumber == 24) {
			drawGameComplete(g2);
		}
	}
	
	private void drawGameOver(Graphics2D g2) {
		long elapsed = Math.min(1000, System.currentTimeMillis() - fadeTime);
		
		double scale = 9.0 * ((1000 - elapsed) / 1000.0) + 1;
		double angle = elapsed * (4 * 360 / 1000.0);
		float opacity = elapsed / 1000f;
		
		AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity);
		g2.setComposite(ac);
		
		g2.setColor(Color.BLACK);
		g2.fillRect(0, 0, Window.DIMENSIONS.width, Window.DIMENSIONS.height);
		
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.rotate(Math.toRadians(angle), Window.DIMENSIONS.width / 2, Window.DIMENSIONS.height / 2);
		Font font = UIFactory.getFont((int) (scale * 160));
		FontMetrics fm = g2.getFontMetrics(font);
		Rectangle2D bounds = fm.getStringBounds("Game Over", g2);
		g2.setFont(font);
		g2.setColor(Color.WHITE);
		g2.drawString("Game Over", (Window.DIMENSIONS.width - (int) bounds.getWidth()) / 2, (Window.DIMENSIONS.height - (int) bounds.getHeight()) / 2 + fm.getAscent());
		
		if (System.currentTimeMillis() - fadeTime >= 7000 && menu == null) {
			ImageIcon menuIcon = new ImageIcon("img/ui/menu.png");
			menu = UIFactory.createButton(menuIcon, new ImageIcon("img/ui/menuPressed.png"));
			menu.setBounds((Window.DIMENSIONS.width - menuIcon.getIconWidth()) / 2, Window.DIMENSIONS.height - 200, menuIcon.getIconWidth(), menuIcon.getIconHeight());
			menu.addActionListener(this);
			add(menu);
			SoundEffect.PICKUP.play(false);
		}
	}
	
	private void drawGameComplete(Graphics2D g2) {
		g2.setColor(Color.WHITE);
		
		long elapsed = System.currentTimeMillis() - fadeTime;
		if (elapsed <= 4000) {
			int beamAngle = (int) (360 * Math.random());
			int beamArc = (int) (10 * Math.random()) + 6;
			int beamOpacity = (int) (255 * Math.random()) + 1;
			
			beams.add(new int[] { beamAngle, beamArc, beamOpacity });
			for (int[] beam : beams) {
				AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, beam[2] / 255f);
				g2.setComposite(ac);
				g2.fillArc(beamSpawnArea.x, beamSpawnArea.y, beamSpawnArea.width, beamSpawnArea.height, beam[0], beam[1]);
			}
			if (elapsed >= 2000) {
				AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (elapsed - 2000) / 2000f);
				g2.setComposite(ac);
				g2.fillRect(-10, -10, Window.DIMENSIONS.width + 20, Window.DIMENSIONS.height + 20);
			}
		} else {
			g2.fillRect(-10, -10, Window.DIMENSIONS.width + 20, Window.DIMENSIONS.height + 20);
			changeScreen(new VictoryScreen(cumulativeTime + (int) ((endTime - startTime) / 100), frame));
		}
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		if (player.getMovement() == Movement.STILL && !player.isFalling()) {
			int state = player.getState();
			if (state == Player.NORMAL) {
				if (SwingUtilities.isRightMouseButton(arg0) && storedPowerUps[1] > 0 && player.getBlocks().size() > 1) {
					player.startSplitting();
					if (levelNumber == 7) {
						remove(helpPanels.get(0));
						add(helpPanels.get(1));
						revalidate();
					}
				} else if (SwingUtilities.isLeftMouseButton(arg0) && player.getHighlightedBlock() != null) {
					player.confirmBuild(map);
					--storedPowerUps[0];
					SoundEffect.GROW.play(false);
					if (levelNumber == 2) {
						remove(helpPanels.get(0));
						revalidate();
					}
				}
			} else if (state == Player.SPLITTING) {
				if (SwingUtilities.isLeftMouseButton(arg0) && player.getSplitLine() != null) {
					player.splitIntoSides();
					--storedPowerUps[1];
					SoundEffect.SPLIT.play(false);
					if (levelNumber == 7) {
						remove(helpPanels.get(1));
						add(helpPanels.get(2));
						revalidate();
					}
				}
			} else if (state == Player.CHOOSING) {
				int chosenSide = player.getChosenSide();
				if (SwingUtilities.isLeftMouseButton(arg0) && chosenSide != -1) {
					ArrayList<PlayerBlock> abandonedBlocks = player.chooseSide(chosenSide);
					for (PlayerBlock pBlock : abandonedBlocks) {
						Point worldCoords = pBlock.getWorldCoords();
						map.getChars()[worldCoords.y][worldCoords.x] = 'C';
					}
					for (int i = 0; i < map.getChars().length; i++) {
						for (int j = 0; j < map.getChars()[0].length; j++) {
							if (map.getChars()[i][j] == 'C') {
								map.getBlocks()[i][j] = new CryingPlayerBlock(map.relativePosition(i, j));
							}
						}
					}
				}
				SoundEffect.SAD.play(false);
				if (levelNumber == 7) {
					remove(helpPanels.get(2));
					revalidate();
				}
			}
			mouseAction(arg0);
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
		if (state == Player.NORMAL) {
			if (storedPowerUps[0] > 0)
				player.highlightBlock(x, y);
		} else if (state == Player.SPLITTING) {
			player.highlightSplitLine(map, x, y);
		} else if (state == Player.CHOOSING) {
			player.setChosenSide(x, y);
		}
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (arg0.getSource() == lastLevel) {
			changeLevel(levelNumber - 1);
			SoundEffect.CLICK.play(false);
		} else if (arg0.getSource() == nextLevel) {
			changeLevel(levelNumber + 1);
			SoundEffect.CLICK.play(false);
		} else if (arg0.getSource() == reset) {
			loseHeart();
			SoundEffect.CLICK.play(false);
		} else if (arg0.getSource() == exit) {
			if (theme.getBackgroundNoise() != null) {
				theme.getBackgroundNoise().stop();
			}
			changeScreen(last);
			SoundEffect.CLICK.play(false);
		} else if (arg0.getSource() == menu) {
			changeScreen(new MainScreen(frame));
			SoundEffect.CLICK.play(false);
		} else if (arg0.getSource() == timer) {
			if (startTime == 0) {
				xOffset = (int) Math.signum(xOffset) * Math.max(0, (int) (Window.DIMENSIONS.width * (500.0 - System.currentTimeMillis() + openTime) / 500));
				if (xOffset == 0) {
					bindKeys();
					if (theme.getBackgroundNoise() != null) {
						theme.getBackgroundNoise().play(true);
					}
					if (levelNumber == 1 || levelNumber == 4) {
						add(helpPanels.get(0));
					}
					startTime = System.currentTimeMillis();
				}
			} else if (xOffset != 0) {
				xOffset = (int) (Math.signum(xOffset) * (Window.DIMENSIONS.width * (System.currentTimeMillis() - changeTime) / 500));
			}
			powerUpAngle += 0.05;
			if (powerUpAngle >= 2 * Math.PI) {
				powerUpAngle = 0;
			}
			player.move(map);
			
			if (!complete) {
				if (xOffset == 0) {
					endTime = System.currentTimeMillis();
				}
				updateHUD();
				pickUpPowerUp();
				triggerButtons();
				if (player.isOutOfBounds(map) && numHearts > 0) {
					loseHeart();
				}
				if (player.reachedGoal(map)) {
					complete = true;
					unbindKeys();
					if (!timeTrial && levelNumber == 24) {
						fadeTime = System.currentTimeMillis();
						this.removeAll();
					}
					if (player.getMovement() == Movement.LEFT)
						player.setMovement(Movement.STILL_LEFT);
					else if (player.getMovement() == Movement.RIGHT)
						player.setMovement(Movement.STILL_RIGHT);
					if (!timeTrial) {
						lastLevel.setEnabled(false);
						nextLevel.setEnabled(false);
					}
					reset.setEnabled(false);
				}
			} else if (player.getMovement() == Movement.STILL && !flashTimer.isRunning() && !changeTimer.isRunning()) {
				flashTimer.start();
				
				SoundEffect.SUCCESS.play(false);
				if (!timeTrial && levelNumber == 24) {
					SoundEffect.MUSIC.stop();
					if (theme.getBackgroundNoise() != null) {
						theme.getBackgroundNoise().stop();
					}
					SoundEffect.VICTORY.play(false);
				}
			}
			repaint();
		} else if (arg0.getSource() == flashTimer) {
			if (numFlashes == 4 && levelNumber != 24) {
				changeLevel(levelNumber + 1);
				flashTimer.stop();
			}
			timerLabel.setVisible(!timerLabel.isVisible());
			numFlashes++;
		} else if (arg0.getSource() == changeTimer) {
			if (theme.getBackgroundNoise() != null) {
				theme.getBackgroundNoise().stop();
			}
			changeTimer.stop();
			
			int newLevelNumber = Integer.parseInt(arg0.getActionCommand());
			changeScreen(new Level(newLevelNumber, newLevelNumber > levelNumber, timeTrial, timeTrial ? 0 : cumulativeTime + (int) ((endTime - startTime) / 100), frame, last));
		}
	}
	
	private void changeLevel(int newLevelNumber) {
		xOffset = (int) Math.signum(levelNumber - newLevelNumber);
		changeTime = System.currentTimeMillis();
		changeTimer.start();
		changeTimer.setActionCommand(newLevelNumber + "");
		for (int i = 0; i < helpPanels.size(); i++) {
			remove(helpPanels.get(0));
		}
	}
	
	private void changeScreen(JPanel screen) {
		timer.stop();
		frame.setContentPane(screen);
		frame.revalidate();
		frame.repaint();
		timer.stop();
	}
	
	private void resetLevel() {
		map = new Map(startMap);
		player.resetPositions(map.getStartingPositions());
		player.setState(Player.NORMAL);
		storedPowerUps = new int[] {0,0,0};
		if (levelNumber != 1 && levelNumber != 4) {
			for (int i = 0; i < helpPanels.size(); i++) {
				remove(helpPanels.get(0));
			}
		}
	}
	
	private void loseHeart() {
		numHearts--;
		remove(hearts[numHearts]);
		SoundEffect.DEATH.play(false);
		if (numHearts == 0) {
			fadeTime = System.currentTimeMillis();
			this.removeAll();
			SoundEffect.MUSIC.stop();
			if (theme.getBackgroundNoise() != null) {
				theme.getBackgroundNoise().stop();
			}
			SoundEffect.GAME_OVER.play(false);
		} else {
			resetLevel();
		}
	}

	private void pickUpPowerUp() {
		for (int i = 0; i < player.getBlocks().size(); i++) {
			PlayerBlock pBlock = player.getBlocks().get(i);
			Point check = new Point((int) (Math.round(pBlock.getPixelCoords().getX() / Block.SIZE)),
					pBlock.getWorldCoords().y);
			if (map.isValidBlock(check.x, check.y)) {
				MapBlock block = map.getBlocks()[check.y][check.x];
				if (block instanceof GrowPowerUp) {
					++storedPowerUps[0];
					if (levelNumber == 2) {
						add(helpPanels.get(0));
						revalidate();
					}
				} else if (block instanceof SplitPowerUp) {
					++storedPowerUps[1];
					if (levelNumber == 7) {
						add(helpPanels.get(0));
						revalidate();
					}
				} else if (block instanceof MergePowerUp) {
					++storedPowerUps[2];
					if (levelNumber == 15) {
						add(helpPanels.get(0));
						revalidate();
					}
				} else if (block instanceof QuadPowerUp) {
					storedPowerUps[0] += 4;
				}
				if (block instanceof PowerUp) {
					map.getBlocks()[check.y][check.x] = new SpaceBlock();
					SoundEffect.PICKUP.play(false);
				}
			}
		}
	}
	
	private void triggerButtons() {
		HashSet<PlayerBlock> blocksToSquish = new HashSet<PlayerBlock>();
		for (Point gbLoc : map.getGoalBlocks()) {
			GoalBlock g = (GoalBlock) map.getBlocks()[gbLoc.y][gbLoc.x];
			ArrayList<PlayerBlock> blocksOn = player.blocksOnGoalBlock(gbLoc);
			if (blocksOn.size() > 0 && !g.isPressed()) {
				g.press();
				SoundEffect.BUTTON_DOWN.play(false);
			} else if (blocksOn.size() == 0 && g.isPressed()){
				g.unpress();
				SoundEffect.BUTTON_UP.play(false);
			}
			blocksToSquish.addAll(blocksOn);
		}
		player.squishBlocks(blocksToSquish);
	}
	
	private void bindKeys() {
		Action leftDown = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (player.getState() == Player.NORMAL) {
					player.setMovement(Movement.LEFT);
					if (firstMovement) {
						firstMovement = false;
					}
				}
			}
		};
		Action rightDown = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (player.getState() == Player.NORMAL) {
					player.setMovement(Movement.RIGHT);
					if (firstMovement) {
						firstMovement = false;
					}
				}
			}
		};
		Action leftUp = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (player.getState() == Player.NORMAL) {
					if (player.getMovement() != Movement.RIGHT) {
						player.setMovement(Movement.STILL_LEFT);
					}
				}
			}
		};
		Action rightUp = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (player.getState() == Player.NORMAL) {
					if (player.getMovement() != Movement.LEFT) {
						player.setMovement(Movement.STILL_RIGHT);
					}
				}
			}
		};
		Action shiftDown = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (player.getState() == Player.NORMAL) {
					if (storedPowerUps[2] > 0) {
						ArrayList<PlayerBlock> mergedBlocks = player.merge(map);
						if (mergedBlocks.size() > 0) {
							for (PlayerBlock pBlock : mergedBlocks) {
								Point worldCoords = pBlock.getWorldCoords();
								map.getChars()[worldCoords.y][worldCoords.x] = '.';
								map.getBlocks()[worldCoords.y][worldCoords.x] = new SpaceBlock();
								player.addBlock(worldCoords.x, worldCoords.y);
							}
							player.calculateRelativePositions(player.getBlocks());
							storedPowerUps[2]--;
							SoundEffect.MERGE.play(false);
							if (levelNumber == 15) {
								remove(helpPanels.get(0));
								revalidate();
							}
						}
					}
				}
			}
		};
		
		InputMap im = getInputMap(WHEN_IN_FOCUSED_WINDOW);
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0, false), "left-press");
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0, false), "left-press");
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0, false), "right-press");
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0, false), "right-press");
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0, true), "left-release");
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0, true), "left-release");
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0, true), "right-release");
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0, true), "right-release");
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_SHIFT, InputEvent.SHIFT_DOWN_MASK, false), "shift-press");
	
		ActionMap am = getActionMap();
		am.put("left-press", leftDown);
		am.put("left-release", leftUp);
		am.put("right-press", rightDown);
		am.put("right-release", rightUp);
		am.put("shift-press", shiftDown);
		for (int i = 0; i <= 9; i++) {
			im.put(KeyStroke.getKeyStroke(KeyEvent.VK_0 + i, 0, false), i + "-press");
			am.put(i + "-press", new NumberDownAction(i));
		}
		
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_G, 0, false), "g-press");
		am.put("g-press", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (sekrit.length() > 0) {
					System.out.println("sekrit: " + sekrit);
					int newLevel = Integer.parseInt(sekrit);
					if (newLevel >= 1 && newLevel <= 24) {
						changeLevel(newLevel);
					}
					sekrit = "";
				}
			}
		});
	}
	
	class NumberDownAction extends AbstractAction {
		
		private int number;
		
		NumberDownAction(int number) {
			this.number = number;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			sekrit += number;
		}
	}
	
	private void unbindKeys() {
		ActionMap am = getActionMap();
		am.clear();
	}

	@Override
	public Dimension getPreferredSize() {
		return Window.DIMENSIONS;
	}

//	private static void runGame() {
//		JFrame frame = new JFrame("Level 15");
//		Level level = new Level(15, "img/backgrounds/grasslands.png", frame, null);
//		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		frame.addMouseListener(level);
//		frame.addMouseMotionListener(level);
//		frame.add(level);
//		frame.pack();
//		frame.setVisible(true);
//	}

//	public static void main(String[] args) {
//		javax.swing.SwingUtilities.invokeLater(new Runnable() {
//			public void run() {
//				runGame();
//			}
//		});
//	}
}