
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.Image;
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
	
	private JButton[] buttons = new JButton[6];
	private JButton[] allButtons = new JButton[24];
	private JButton exit;
	private JButton lastPage, nextPage;
	private JLabel pageNum;
	private int page = 1;
	private JPanel grid;
	private GridBagConstraints gbc;

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
		for (int i = 0; i < 100; i++) {
			JPanel entry = initializeEntry(i + 1);
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
	
	private JPanel initializeEntry(int rank) {
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
		
		JLabel name = new JLabel("Balasubramanian");
		name.setFont(UIFactory.getFont(36));
		name.setForeground(Color.WHITE);
		name.setBounds(140, 10, 275, 50);
		entry.add(name);
		
		JLabel time = UIFactory.createOutlinedLabel("0:00.0");
		time.setBounds(600 - time.getPreferredSize().width, 10, time.getPreferredSize().width, time.getPreferredSize().height);
		entry.add(time);
		
		entry.setAlignmentX(CENTER_ALIGNMENT);
		return entry;
	}
	
	public void paintComponent(Graphics g) {
		g.drawImage(new PlainTheme().getBackgroundImage(), 0, 0, null);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if (cmd.startsWith("Level ")) {
			frame.setContentPane(new Level(Integer.parseInt(cmd.substring(6)), true, frame, this));
		} else if (e.getSource() == lastPage) {
			if (page == 1) {
				page = 4;
			} else {
				page--;
			}
			newPage();
		} else if (e.getSource() == nextPage) {
			if (page == 4) {
				page = 1;
			} else {
				page++;
			}
			newPage();
		} else if (e.getSource() == exit) {
			frame.setContentPane(new MainScreen(frame));
		}
		SoundEffect.CLICK.play(false);
		frame.repaint();
		frame.revalidate();
	}
	
	private void newPage() {
		pageNum.setText(page + " / 4");
		for (int i = 0; i < 6; i++) {
			grid.remove(buttons[i]);
			buttons[i] = allButtons[6 * (page - 1) + i];
			gbc.gridx = i % 3;
			gbc.gridy = i / 3;
			grid.add(buttons[i], gbc);
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
