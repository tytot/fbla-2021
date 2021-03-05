import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
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
import javax.swing.SwingWorker;
import javax.swing.Timer;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;

public class VictoryScreen extends JPanel implements ActionListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -763037788114609809L;

	private JFrame frame;
	
	private Timer animTimer = new Timer(25, this);
	private long openTime;
	
	private Theme theme;
	
	private int startMarginTop;
	private int rank = -1;
	private int timeDecis;
	private boolean leaderboardAcquired = false;
	
	private JLabel loading;
	private JPanel container;
	private JTextField nameField;
	private JButton menu;
	private JButton leaders;
	
	VictoryScreen(int timeDecis, JFrame frame) {
		this.frame = frame;
		this.theme = new PlainTheme();
		this.timeDecis = timeDecis;
		
		this.setLayout(null);
		ImageIcon loadingIcon = new ImageIcon(LeaderboardScreen.class.getResource("img/ui/loading.gif"));
		loading = UIFactory.createLabel(loadingIcon, (Window.DIMENSIONS.width - loadingIcon.getIconWidth()) / 2, (Window.DIMENSIONS.height - loadingIcon.getIconHeight()) / 2);
		add(loading);
		loading.setVisible(false);
		
		container = new JPanel();
		container.setOpaque(false);
		container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
		container.setBounds(0, 0, frame.getWidth(), frame.getHeight());
		add(container);
		
		ImageIcon victoryIcon = new ImageIcon(VictoryScreen.class.getResource("img/ui/victory.png"));
		startMarginTop = (Window.DIMENSIONS.height - victoryIcon.getIconHeight()) / 2;
		container.add(Box.createVerticalStrut(startMarginTop));
		JLabel victoryLabel = UIFactory.createLabel(victoryIcon);
		victoryLabel.setAlignmentX(CENTER_ALIGNMENT);
		container.add(victoryLabel);
		
		nameField = new JTextField("Player");
		nameField.setBounds(new Rectangle(155, 5, 610, 50));
		nameField.setFont(UIFactory.getFont(32));
		nameField.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.GRAY, 3), BorderFactory.createEmptyBorder(5, 5, 0, 5)));
		nameField.setForeground(Color.GRAY);
		nameField.setCaretColor(Color.GRAY);
		
		openTime = System.currentTimeMillis();
		animTimer.start();
		
		class LeaderboardFetcher extends SwingWorker<String, Object> {
			@Override
			public String doInBackground() {
				return Connection.fetchLeaderboard();
			}

			@Override
			protected void done() {
				try {
					JsonArray array = Json.parse(get()).asArray();
					int numEntries = array.size();
					for (int i = 0; i < Math.min(numEntries, LeaderboardScreen.MAX_ENTRIES); i++) {
						if (timeDecis < array.get(i).asObject().getInt("time", 59999)) {
							rank = i + 1;
							break;
						}
					}
					if (rank == -1 && numEntries < LeaderboardScreen.MAX_ENTRIES) {
						rank = numEntries + 1;
					}
					if (loading.isVisible()) {
						loading.setVisible(false);
						addDetails();
					}
					leaderboardAcquired = true;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		(new LeaderboardFetcher()).execute();
	}
	
	public void addDetails() {
		JPanel details = new TransparentRoundedPanel(new Color(0, 0, 0, 25));
		details.setAlignmentX(CENTER_ALIGNMENT);
		details.setPreferredSize(new Dimension(480, 80));
		details.setMaximumSize(new Dimension(480, 80));
		details.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
		JLabel inLabel = UIFactory.createLabel("Your Time:", 36);
		inLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 5));
		details.add(inLabel, gbc);
		
		int minutes = timeDecis / 600, seconds = (timeDecis % 600) / 10, decis = (timeDecis % 600) % 10;
		String time = minutes + ":" + String.format("%02d", seconds) + "." + decis;
		JLabel timeLabel = UIFactory.createOutlinedLabel(time);
		
		details.add(timeLabel, gbc);
		container.add(details);
		container.add(Box.createVerticalStrut(Block.SIZE));
		
		if (rank != -1) {
			JPanel leaderboard = new TransparentRoundedPanel(new Color(0, 0, 0, 25));
			leaderboard.setMaximumSize(new Dimension(1170, 270));
			leaderboard.add(Box.createVerticalStrut(Block.SIZE));
			leaderboard.setAlignmentX(CENTER_ALIGNMENT);
			leaderboard.setLayout(new BoxLayout(leaderboard, BoxLayout.Y_AXIS));
			JLabel congratsLabel = UIFactory.createLabel("You made the leaderboard!", 32);
			congratsLabel.setAlignmentX(CENTER_ALIGNMENT);
			leaderboard.add(congratsLabel);
			leaderboard.add(Box.createVerticalStrut(Block.SIZE / 2));
			JLabel leaderboardHeader = UIFactory.createLabel("img/ui/leaderboardHeader.png");
			leaderboardHeader.setAlignmentX(CENTER_ALIGNMENT);
			leaderboard.add(leaderboardHeader);
			leaderboard.add(Box.createVerticalStrut(Block.SIZE / 4));
			
			JPanel entry = new JPanel();
			entry.setLayout(null);
			entry.setPreferredSize(new Dimension(1080, 70));
			entry.setMinimumSize(new Dimension(1080, 70));
			entry.setMaximumSize(new Dimension(1080, 70));
			entry.setOpaque(false);
			entry.setAlignmentX(CENTER_ALIGNMENT);
			
			entry.add(UIFactory.createOutlinedLabel(rank + "", 15, 10));
			if (rank == 1) {
				entry.add(UIFactory.createLabel("img/ui/gold_medal.png", 75, 0));
			} else if (rank == 2) {
				entry.add(UIFactory.createLabel("img/ui/silver_medal.png", 75, 0));
			} else if (rank == 3) {
				entry.add(UIFactory.createLabel("img/ui/bronze_medal.png", 75, 0));
			}
			entry.add(nameField);
			JLabel timeLabel2 = UIFactory.createOutlinedLabel(time);
			timeLabel2.setBounds(975 - timeLabel2.getPreferredSize().width, 10, timeLabel2.getPreferredSize().width, timeLabel2.getPreferredSize().height);
			entry.add(timeLabel2);
			leaderboard.add(entry);
			container.add(leaderboard);
			
			container.add(Box.createVerticalGlue());
			leaders = UIFactory.createButton("img/ui/leaderboard.png", "img/ui/leaderboardPressed.png");
			leaders.setAlignmentX(CENTER_ALIGNMENT);
			leaders.addActionListener(this);
			container.add(leaders);
			
			container.add(Box.createVerticalStrut(Block.SIZE / 2));
		} 
		menu = UIFactory.createButton(new ImageIcon("img/ui/menu.png"), new ImageIcon("img/ui/menuPressed.png"));
		menu.setAlignmentX(CENTER_ALIGNMENT);
		menu.addActionListener(this);
		container.add(menu);
		container.add(Box.createVerticalGlue());
		
		this.revalidate();
		this.repaint();
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
			container.remove(0);
			container.add(Box.createVerticalStrut((int) (((6000 - elapsed) / 500.0) * (startMarginTop - Block.SIZE) + Block.SIZE)), 0);
			this.revalidate();
		} else if (elapsed >= 6000 && animTimer.isRunning()) {
			if (leaderboardAcquired) {
				addDetails();
			} else {
				loading.setVisible(true);
			}
			animTimer.stop();
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (arg0.getSource() == animTimer) {
			this.repaint();
		} else {
			SoundEffect.CLICK.play(false);
			if (rank != 1) {
				System.out.println("Posting to leaderboard...");
				loading.setVisible(true);
				container.removeAll();
				revalidate();
				repaint();
				
				class LeaderboardPoster extends SwingWorker<String, Object> {
					@Override
					public String doInBackground() {
						return Connection.postToLeaderboard(nameField.getText(), timeDecis);
					}
					
					@Override
					protected void done() {
						try {
							System.out.println(get());
						} catch (Exception e) {
							e.printStackTrace();
						}
						switchScreen((JButton) arg0.getSource());
					}
				}
				(new LeaderboardPoster()).execute();
			} else {
		        switchScreen((JButton) arg0.getSource());
			}
		}
	}
	
	private void switchScreen(JButton source) {
        if (source == leaders) {
			frame.setContentPane(new LeaderboardScreen(frame));
		} else if (source == menu) {
			frame.setContentPane(new MainScreen(frame));
		}
		SoundEffect.MUSIC.play(true);
		frame.revalidate();
		frame.repaint();
	}
	
	public String getName() {
		return nameField.getText();
	}
	
	public int getTimeDecis() {
		return timeDecis;
	}
	
//	public static void main(String[] args) {
//		System.setProperty("sun.java2d.uiScale", "1");
//		JFrame frame = new JFrame("Victory");
//		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		frame.setResizable(false);
//		Dimension d = new Dimension(33, 24);
//		frame.setPreferredSize(new Dimension(d.width * Block.SIZE, d.height * Block.SIZE));
//		frame.setMinimumSize(new Dimension(d.width * Block.SIZE, d.height * Block.SIZE));
//		frame.setMaximumSize(new Dimension(d.width * Block.SIZE, d.height * Block.SIZE));
//		frame.setContentPane(new VictoryScreen(54343, frame));
//		frame.pack();
//		frame.setVisible(true);
//	}
}
