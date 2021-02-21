
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
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
import javax.swing.border.LineBorder;

public class LevelScreen extends JPanel implements ActionListener {
	private JFrame frame;
	
	private JButton[] buttons = new JButton[6];
	private JButton[] allButtons = new JButton[24];
	private JButton exit;
	private JButton lastPage, nextPage;
	private JLabel pageNum;
	private int page = 1;
	private JPanel grid;
	private GridBagConstraints gbc;

	LevelScreen(JFrame frame) {
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
		JLabel levelsLabel = new JLabel(new ImageIcon("img/ui/levelsPressed.png"));
		levelsLabel.setAlignmentX(CENTER_ALIGNMENT);
		container.add(levelsLabel);
		grid = new JPanel();
		grid.setOpaque(false);
		grid.setLayout(new GridBagLayout());
		gbc = new GridBagConstraints();
		gbc.insets = new Insets(50, 50, 50, 50);
		for (int i = 0; i < 24; i++) {
			allButtons[i] = new LevelButton(i + 1);
			allButtons[i].setActionCommand("Level " + (i + 1));
			allButtons[i].addActionListener(this);
		}
		for (int i = 0; i < 6; i++) {
			buttons[i] = allButtons[i];
			gbc.gridx = i % 3;
			gbc.gridy = i / 3;
			grid.add(buttons[i], gbc);
		}
		grid.setAlignmentX(CENTER_ALIGNMENT);
		container.add(grid);
		JPanel navigation = new JPanel();
		navigation.setOpaque(false);
		lastPage = UIFactory.createButton(new ImageIcon("img/ui/lastLevel.png"), new ImageIcon("img/ui/lastLevelPressed.png"));
		lastPage.addActionListener(this);
		navigation.add(lastPage);
		JPanel chip = new TransparentRoundedPanel(new Color(0, 0, 0, 25));
		chip.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		pageNum = UIFactory.createLabel(page + " / 4", 36);
		chip.add(pageNum);
		navigation.add(Box.createHorizontalStrut(5));
		navigation.add(chip);
		navigation.add(Box.createHorizontalStrut(5));
		nextPage = UIFactory.createButton(new ImageIcon("img/ui/nextLevel.png"), new ImageIcon("img/ui/nextLevelPressed.png"));
		nextPage.addActionListener(this);
		navigation.add(nextPage);
		navigation.setAlignmentX(CENTER_ALIGNMENT);
		container.add(navigation);
		container.add(Box.createVerticalStrut(Block.SIZE * 2));
		add(container);
	}
	
	public void paintComponent(Graphics g) {
		g.drawImage(new PlainTheme().getBackgroundImage(), 0, 0, null);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if (cmd.startsWith("Level ")) {
			frame.setContentPane(new Level(Integer.parseInt(cmd.substring(6)), true, true, 0, frame, this));
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
}
