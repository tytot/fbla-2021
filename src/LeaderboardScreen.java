
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class LeaderboardScreen extends JPanel implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 861795915282271507L;
	private JFrame frame;
	private JButton exit;

	LeaderboardScreen(JFrame frame) {
		this.frame = frame;
		setPreferredSize(new Dimension(33 * Block.SIZE, 24 * Block.SIZE));
		setLayout(null);
		
		exit = UIFactory.createButton("img/ui/exit.png", "img/ui/exitPressed.png", 25, 15);
		exit.addActionListener(this);
		add(exit);
		
		JPanel container = new JPanel();
		container.setOpaque(false);
		container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
		container.setBounds(0, 0, frame.getWidth(), frame.getHeight());
		container.add(Box.createVerticalStrut(Block.SIZE * 2));
		JLabel levelsLabel = UIFactory.createLabel("img/ui/leaderboardPressed.png");
		levelsLabel.setAlignmentX(CENTER_ALIGNMENT);
		container.add(levelsLabel);
		container.add(Box.createVerticalStrut(Block.SIZE));
		JLabel leaderboardHeader = UIFactory.createLabel("img/ui/leaderboardHeader.png");
		leaderboardHeader.setAlignmentX(CENTER_ALIGNMENT);
		container.add(leaderboardHeader);
		container.add(Box.createVerticalStrut(Block.SIZE / 2));
		
		JPanel list = new JPanel();
		list.setLayout(new BoxLayout(list, BoxLayout.Y_AXIS));
		list.setOpaque(false);
		
		// CHANGE
		for (int i = 0; i < 100; i++) {
			JPanel entry = createLeaderboardEntry(i + 1, "Balasubramanian", "00:00.00");
			entry.setAlignmentX(CENTER_ALIGNMENT);
			list.add(entry);
		}
		
		JScrollPane scroller = new JScrollPane(list, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroller.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0, 25), 16, true));
		scroller.getVerticalScrollBar().setUI(new KenneyScrollBarUI());
		scroller.getVerticalScrollBar().setBackground(new Color(0, 0, 0, 25));
		scroller.getVerticalScrollBar().setUnitIncrement(20);
		scroller.setAlignmentX(CENTER_ALIGNMENT);
		scroller.setOpaque(false);
		scroller.getViewport().setOpaque(false);
		scroller.setMaximumSize(new Dimension(720, scroller.getPreferredSize().height));
		container.add(scroller);
		container.add(Box.createVerticalStrut(Block.SIZE * 2));
		add(container);
	}
	
	private JPanel createLeaderboardEntry(int rank, String name, String time) {
		JPanel entry = new JPanel();
		entry.setLayout(null);
		entry.setPreferredSize(new Dimension(720, 70));
		entry.setMinimumSize(new Dimension(720, 70));
		entry.setMaximumSize(new Dimension(720, 70));
		entry.setBackground(new Color(0, 0, 0, 25));
		
		entry.add(UIFactory.createOutlinedLabel(rank + "", 5, 10));
		if (rank == 1) {
			entry.add(UIFactory.createLabel("img/ui/gold_medal.png", 60, 0));
		} else if (rank == 2) {
			entry.add(UIFactory.createLabel("img/ui/silver_medal.png", 60, 0));
		} else if (rank == 3) {
			entry.add(UIFactory.createLabel("img/ui/bronze_medal.png", 60, 0));
		}
		entry.add(UIFactory.createLabel("Balasubramanian", 36, new Rectangle(140, 10, 275, 50)));
		
		JLabel timeLabel = UIFactory.createOutlinedLabel("0:00.0");
		timeLabel.setBounds(600 - timeLabel.getPreferredSize().width, 10, timeLabel.getPreferredSize().width, timeLabel.getPreferredSize().height);
		entry.add(timeLabel);
		
		return entry;
	}
	
	public void paintComponent(Graphics g) {
		g.drawImage(new PlainTheme().getBackgroundImage(), 0, 0, null);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == exit) {
			frame.setContentPane(new MainScreen(frame));
			SoundEffect.CLICK.play(false);
			frame.repaint();
			frame.revalidate();
		}
	}
	
//	public static void main(String[] args) {
//		JFrame frame = new JFrame("Leaderboard");
//		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		frame.setResizable(false);
//		Dimension d = new Dimension(33, 24);
//		frame.setPreferredSize(new Dimension(d.width * Block.SIZE, d.height * Block.SIZE));
//		frame.setMinimumSize(new Dimension(d.width * Block.SIZE, d.height * Block.SIZE));
//		frame.setMaximumSize(new Dimension(d.width * Block.SIZE, d.height * Block.SIZE));
//		frame.setContentPane(new LeaderboardScreen(frame));
//		frame.pack();
//		frame.setVisible(true);
//	}
}
