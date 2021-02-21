import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;

public class VictoryScreen extends JPanel implements ActionListener {
	
	private JFrame frame;
	
	private Timer animTimer = new Timer(25, this);
	private long openTime;
	
	private Theme theme;
	
	private int startMarginTop;
	private int time;
	
	private JButton menu;
	private JButton leaders;
	
	VictoryScreen(int time, JFrame frame) {
		this.frame = frame;
		this.theme = new PlainTheme();
		this.time = time;
		
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		ImageIcon victoryIcon = new ImageIcon("img/ui/victory.png");
		startMarginTop = (Window.DIMENSIONS.height - victoryIcon.getIconHeight()) / 2;
		this.add(Box.createVerticalStrut(startMarginTop));
		JLabel victoryLabel = UIFactory.createLabel(victoryIcon);
		victoryLabel.setAlignmentX(CENTER_ALIGNMENT);
		this.add(victoryLabel);
		
		openTime = System.currentTimeMillis();
		animTimer.start();
	}
	
	VictoryScreen() {
		this.theme = new PlainTheme();
		this.time = 0;
		
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		ImageIcon victoryIcon = new ImageIcon("img/ui/victory.png");
		startMarginTop = Block.SIZE;
		this.add(Box.createVerticalStrut(startMarginTop));
		JLabel victoryLabel = UIFactory.createLabel(victoryIcon);
		victoryLabel.setAlignmentX(CENTER_ALIGNMENT);
		this.add(victoryLabel);
		
		addDetails();
	}
	
	public void addDetails() {
		this.add(Box.createVerticalStrut(Block.SIZE / 2));
		
		JPanel details = new TransparentRoundedPanel(new Color(0, 0, 0, 25));
		details.setAlignmentX(CENTER_ALIGNMENT);
		details.setPreferredSize(new Dimension(480, 80));
		details.setMaximumSize(new Dimension(480, 80));
		details.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
		JLabel inLabel = UIFactory.createLabel("Your Time:", 36);
		inLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 5));
		details.add(inLabel, gbc);
		int minutes = time / 600, seconds = (time % 600) / 10, decis = (time % 600) % 10;
		String timeString = minutes + ":" + String.format("%02d", seconds) + "." + decis;
		JLabel timeLabel = UIFactory.createOutlinedLabel(timeString);
		details.add(timeLabel, gbc);
		this.add(details);
		this.add(Box.createVerticalStrut(Block.SIZE));
		
		JPanel leaderboard = new TransparentRoundedPanel(new Color(0, 0, 0, 25));
		leaderboard.setMaximumSize(new Dimension(800, 270));
		leaderboard.add(Box.createVerticalStrut(Block.SIZE));
		leaderboard.setAlignmentX(CENTER_ALIGNMENT);
		leaderboard.setLayout(new BoxLayout(leaderboard, BoxLayout.Y_AXIS));
		JLabel congratsLabel = UIFactory.createLabel("You made the leaderboard!", 32);
		congratsLabel.setAlignmentX(CENTER_ALIGNMENT);
		leaderboard.add(congratsLabel);
		leaderboard.add(Box.createVerticalStrut(Block.SIZE / 2));
		JLabel leaderboardHeader = UIFactory.createLabel(new ImageIcon("img/ui/leaderboardHeader.png"));
		leaderboardHeader.setAlignmentX(CENTER_ALIGNMENT);
		leaderboard.add(leaderboardHeader);
		leaderboard.add(Box.createVerticalStrut(Block.SIZE / 4));
		
		JPanel entry = new JPanel();
		entry.setLayout(null);
		entry.setPreferredSize(new Dimension(720, 70));
		entry.setMinimumSize(new Dimension(720, 70));
		entry.setMaximumSize(new Dimension(720, 70));
		entry.setOpaque(false);
		
		int rank = 10;
		entry.add(UIFactory.createOutlinedLabel(rank + "", 5, 10));
		if (rank == 1) {
			entry.add(UIFactory.createLabel(new ImageIcon("img/ui/gold_medal.png"), 60, 0));
		} else if (rank == 2) {
			entry.add(UIFactory.createLabel(new ImageIcon("img/ui/silver_medal.png"), 60, 0));
		} else if (rank == 3) {
			entry.add(UIFactory.createLabel(new ImageIcon("img/ui/bronze_medal.png"), 60, 0));
		}
		JTextField nameField = new JTextField("Player");
		nameField.setBounds(new Rectangle(140, 5, 275, 50));
		nameField.setFont(UIFactory.getFont(32));
		nameField.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.GRAY, 3), BorderFactory.createEmptyBorder(5, 5, 0, 5)));
		nameField.setForeground(Color.GRAY);
		nameField.setCaretColor(Color.GRAY);
		entry.add(nameField);
		JLabel timeLabel2 = UIFactory.createOutlinedLabel(timeString);
		timeLabel2.setBounds(600 - timeLabel2.getPreferredSize().width, 10, timeLabel2.getPreferredSize().width, timeLabel2.getPreferredSize().height);
		entry.add(timeLabel2);
		leaderboard.add(entry);
		this.add(leaderboard);
		
		this.add(Box.createVerticalGlue());
		leaders = UIFactory.createButton(new ImageIcon("img/ui/leaderboard.png"), new ImageIcon("img/ui/leaderboardPressed.png"));
		leaders.setAlignmentX(CENTER_ALIGNMENT);
		leaders.addActionListener(this);
		this.add(leaders);
//		menu = UIFactory.createButton(new ImageIcon("img/ui/menu.png"), new ImageIcon("img/ui/menuPressed.png"));
//		menu.setAlignmentX(CENTER_ALIGNMENT);
//		menu.addActionListener(this);
//		this.add(menu);
		this.add(Box.createVerticalGlue());
		
		this.revalidate();
	}
	
	public void paintComponent(Graphics g) {
		g.drawImage(theme.getBackgroundImage(), 0, 0, null);
		
		long elapsed = System.currentTimeMillis() - openTime;
		if (elapsed < 4000) {
			float opacity = (4000f - elapsed) / 4000f;
			
			Graphics2D g2 = (Graphics2D) g;
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
			g2.setColor(Color.WHITE);
			g2.fillRect(0, 0, Window.DIMENSIONS.width, Window.DIMENSIONS.height);
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f - opacity));
		} else if (elapsed >= 5500 && elapsed < 6000) {
			this.remove(0);
			this.add(Box.createVerticalStrut((int) (((6000 - elapsed) / 500.0) * (startMarginTop - Block.SIZE) + Block.SIZE)), 0);
			this.revalidate();
		} else if (elapsed >= 6000 && animTimer.isRunning()) {
			addDetails();
			animTimer.stop();
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (arg0.getSource() == animTimer) {
			this.repaint();
		} else if (arg0.getSource() == leaders) {
			frame.setContentPane(new LeaderboardScreen(frame));
			SoundEffect.CLICK.play(false);
			frame.repaint();
			frame.revalidate();
		}
	}
	
	public static void main(String[] args) {
		JFrame frame = new JFrame("Victory");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		Dimension d = new Dimension(33, 24);
		frame.setPreferredSize(new Dimension(d.width * Block.SIZE, d.height * Block.SIZE));
		frame.setMinimumSize(new Dimension(d.width * Block.SIZE, d.height * Block.SIZE));
		frame.setMaximumSize(new Dimension(d.width * Block.SIZE, d.height * Block.SIZE));
		frame.setContentPane(new VictoryScreen(10000, frame));
		frame.pack();
		frame.setVisible(true);
	}
}
