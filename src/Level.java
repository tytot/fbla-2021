
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.List;

public class Level extends JPanel implements KeyListener, MouseListener, MouseMotionListener, ActionListener {
	private Timer timer = new Timer(10, this);
	private static Insets insets;

	private MapBlock[][] map;
	private JLabel[][] powerUps;
	private Player player = new Player();

	Level(String filePath) {
		setLayout(null);
		try {
			List<String> lines = Files.readAllLines(Paths.get(filePath));
			map = new MapBlock[lines.size()][lines.get(0).length()];
			powerUps = new JLabel[map.length][map[0].length];
			for (int i = 0; i < lines.size(); i++) {
				char[] row = lines.get(i).toCharArray();
				for (int j = 0; j < row.length; j++) {
					char block = row[j];
					if (block == '.') {
						map[i][j] = new SpaceBlock();
					} else if (block == 'B') {
						map[i][j] = new SolidBlock();
					} else if (block == 'R') {
						PowerUp p = new RedPowerUp();
						map[i][j] = p;
						ImageIcon imgIcon = new ImageIcon(new File(p.imagePath()).toURI().toURL());
						imgIcon.setImage(imgIcon.getImage().getScaledInstance(MapBlock.SIZE, MapBlock.SIZE, Image.SCALE_DEFAULT));
						JLabel label = new JLabel(imgIcon);
						powerUps[i][j] = label;
						label.setBounds(j * MapBlock.SIZE, i * MapBlock.SIZE, MapBlock.SIZE, MapBlock.SIZE);
						add(label);
					} else if (block == 'P') {
						map[i][j] = new SpaceBlock();
						player.addBlock(j, i, Color.RED);
					}
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
		PlayerBlock highlightedBlock = player.getHighlightedBlock();
		if (highlightedBlock != null) {
			Point highlightedPoint = highlightedBlock.getPixelCoords();
			g2.setColor(Color.PINK);
			g2.fillRect(highlightedPoint.x, highlightedPoint.y, PlayerBlock.SIZE, PlayerBlock.SIZE);
		}
		timer.start();
	}
	
	private Image resizeImage(Image srcImg, int w, int h){
	    BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
	    Graphics2D g2 = resizedImg.createGraphics();

	    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	    g2.drawImage(srcImg, 0, 0, w, h, null);
	    g2.dispose();

	    return resizedImg;
	}

	public void keyPressed(KeyEvent e) {
		if (!player.isBuilding()) {
			if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
				player.setMovement(Movement.RIGHT);
			} else if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
				player.setMovement(Movement.LEFT);
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (!player.isBuilding()) {
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
		if (!player.isBuilding()) {
			if (SwingUtilities.isRightMouseButton(arg0)) {
				player.startBuilding(map);
			}
		} else if (SwingUtilities.isLeftMouseButton(arg0)) {
			if (player.getHighlightedBlock() != null) {
				player.confirmBuild();
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
		if (player.isBuilding()) {
			int x = (arg0.getX() - insets.left) / PlayerBlock.SIZE;
			int y = (arg0.getY() - insets.top) / PlayerBlock.SIZE;
			player.highlightBlock(x, y);
		}
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		player.move(map);
		
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