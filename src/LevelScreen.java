
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

public class LevelScreen extends JPanel implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2278619233103992010L;

	private JFrame frame;
	private Timer timer = new Timer(25, this);
	
	private JButton[] buttons = new JButton[6];
	private JButton[] allButtons = new JButton[24];
	private JButton exit;
	private JButton lastPage, nextPage;
	private JLabel pageNum;
	private int page = 1;
	private JPanel grid;
	private GridBagConstraints gbc;
	
	private Theme[] themes = new Theme[4];

	LevelScreen(JFrame frame) {
		themes[0] = new PlainTheme();
		themes[1] = new RainyTheme();
		themes[2] = new SnowyTheme();
		themes[3] = new SandyTheme();
		
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
		JLabel levelsLabel = UIFactory.createLabel("img/ui/levelsPressed.png");
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
		lastPage = UIFactory.createButton("img/ui/lastLevel.png", "img/ui/lastLevelPressed.png");
		lastPage.addActionListener(this);
		navigation.add(lastPage);
		JPanel chip = new TransparentRoundedPanel(new Color(0, 0, 0, 25));
		chip.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		pageNum = UIFactory.createLabel(page + " / 4", 36);
		chip.add(pageNum);
		navigation.add(Box.createHorizontalStrut(5));
		navigation.add(chip);
		navigation.add(Box.createHorizontalStrut(5));
		nextPage = UIFactory.createButton("img/ui/nextLevel.png", "img/ui/nextLevelPressed.png");
		nextPage.addActionListener(this);
		navigation.add(nextPage);
		navigation.setAlignmentX(CENTER_ALIGNMENT);
		container.add(navigation);
		container.add(Box.createVerticalStrut(Block.SIZE * 2));
		add(container);
		
		timer.start();
	}
	
	public void paintComponent(Graphics g) {
		g.drawImage(themes[page - 1].getBackgroundImage(), 0, 0, null);
		themes[page - 1].drawParticles((Graphics2D) g, null, null);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == timer) {
			repaint();
		} else {
			String cmd = e.getActionCommand();
			if (cmd.startsWith("Level ")) {
				frame.setContentPane(new Level(Integer.parseInt(cmd.substring(6)), true, true, 0, frame, this));
			} else if (e.getSource() == lastPage) {
				int prevPage = page;
				if (page == 1) {
					page = 4;
				} else {
					page--;
				}
				newPage(prevPage);
			} else if (e.getSource() == nextPage) {
				int prevPage = page;
				if (page == 4) {
					page = 1;
				} else {
					page++;
				}
				newPage(prevPage);
			} else if (e.getSource() == exit) {
				frame.setContentPane(new MainScreen(frame));
				if (themes[page - 1].getBackgroundNoise() != null) {
					themes[page - 1].getBackgroundNoise().stop();
				}
			}
			SoundEffect.CLICK.play(false);
			frame.revalidate();
			frame.repaint();
		}
	}
	
	private void newPage(int prevPage) {
		if (themes[prevPage - 1].getBackgroundNoise() != null) {
			themes[prevPage - 1].getBackgroundNoise().stop();
		}
		pageNum.setText(page + " / 4");
		for (int i = 0; i < 6; i++) {
			grid.remove(buttons[i]);
			buttons[i] = allButtons[6 * (page - 1) + i];
			gbc.gridx = i % 3;
			gbc.gridy = i / 3;
			grid.add(buttons[i], gbc);
		}
		playBackgroundNoise();
	}
	
	public void playBackgroundNoise() {
		if (themes[page - 1].getBackgroundNoise() != null) {
			themes[page - 1].getBackgroundNoise().play(true);
		}
	}
}
