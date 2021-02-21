
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class LeaderboardScreen extends JPanel implements ActionListener {
	private JFrame frame;
	private JButton exit;

	LeaderboardScreen(JFrame frame) {
		this.frame = frame;
		setPreferredSize(new Dimension(33 * Block.SIZE, 24 * Block.SIZE));
		setLayout(null);
		
		exit = UIFactory.createButton(new ImageIcon("img/ui/exit.png"), new ImageIcon("img/ui/exitPressed.png"), 25, 15);
		exit.addActionListener(this);
		add(exit);
		
		JPanel container = new JPanel();
		container.setOpaque(false);
		container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
		container.setBounds(0, 0, frame.getWidth(), frame.getHeight());
		container.add(Box.createVerticalStrut(Block.SIZE * 2));
		JLabel levelsLabel = UIFactory.createLabel(new ImageIcon("img/ui/leaderboardPressed.png"));
		levelsLabel.setAlignmentX(CENTER_ALIGNMENT);
		container.add(levelsLabel);
		container.add(Box.createVerticalStrut(Block.SIZE));
		JLabel leaderboardHeader = UIFactory.createLabel(new ImageIcon("img/ui/leaderboardHeader.png"));
		leaderboardHeader.setAlignmentX(CENTER_ALIGNMENT);
		container.add(leaderboardHeader);
		container.add(Box.createVerticalStrut(Block.SIZE / 2));
		
		JPanel list = new JPanel();
		list.setLayout(new BoxLayout(list, BoxLayout.Y_AXIS));
		list.setOpaque(false);
		

		// Hardcode Leaders
		JPanel juicer = createLeaderboardEntry(1, "Adit", "15:46.67");
		juicer.setAlignmentX(CENTER_ALIGNMENT);
		list.add(juicer);
		JPanel juicer2 = createLeaderboardEntry(2, "Tytot", "18:37.03");
		juicer2.setAlignmentX(CENTER_ALIGNMENT);
		list.add(juicer2);
		JPanel juicer3 = createLeaderboardEntry(3, "Chen", "28:38.04");
		juicer3.setAlignmentX(CENTER_ALIGNMENT);
		list.add(juicer3);
		//Placeholders
		for (int i = 3; i < 100; i++) {
			JPanel entry = createLeaderboardEntry(i + 1, "SAMPLE", "00:00.00");
			entry.setAlignmentX(CENTER_ALIGNMENT);
			list.add(entry);
		}
		
		JScrollPane scroller = new JScrollPane(list, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroller.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0, 25), 16, true));
		scroller.getVerticalScrollBar().setUI(new CustomScrollBarUI());
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
			entry.add(UIFactory.createLabel(new ImageIcon("img/ui/gold_medal.png"), 60, 0));
		} else if (rank == 2) {
			entry.add(UIFactory.createLabel(new ImageIcon("img/ui/silver_medal.png"), 60, 0));
		} else if (rank == 3) {
			entry.add(UIFactory.createLabel(new ImageIcon("img/ui/bronze_medal.png"), 60, 0));
		}
		entry.add(UIFactory.createLabel(name, 36, new Rectangle(140, 10, 275, 50)));
		
		JLabel timeLabel = UIFactory.createOutlinedLabel(time);
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
	
	public static void main(String[] args) {
		JFrame frame = new JFrame("Leaderboard");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		Dimension d = new Dimension(33, 24);
		frame.setPreferredSize(new Dimension(d.width * Block.SIZE, d.height * Block.SIZE));
		frame.setMinimumSize(new Dimension(d.width * Block.SIZE, d.height * Block.SIZE));
		frame.setMaximumSize(new Dimension(d.width * Block.SIZE, d.height * Block.SIZE));
		frame.setContentPane(new LeaderboardScreen(frame));
		frame.pack();
		frame.setVisible(true);
	}
}
