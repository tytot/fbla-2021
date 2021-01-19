
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

public class MainScreen extends JPanel implements ActionListener {
	private JFrame frame;
	private Image bg;
	private JButton play, levels;

	private SoundEffect click = new SoundEffect(SoundEffect.CLICK);

	MainScreen(JFrame frame) {
		this.frame = frame;
		setPreferredSize(
		new Dimension(33 * Block.SIZE, 24 * Block.SIZE));
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		try {
			bg = ImageIO
			.read(new File("src/res/img/backgrounds/grasslands.png"));
			add(Box.createVerticalGlue());
			add(Box.createVerticalGlue());
			JLabel title = new JLabel(
			new ImageIcon("src/res/img/ui/title.png"));
			title.setAlignmentX(CENTER_ALIGNMENT);
			add(title);
			add(Box.createVerticalGlue());
			add(Box.createVerticalGlue());
			play = initializeButton("src/res/img/ui/play.png",
			"src/res/img/ui/playPressed.png");
			play.setAlignmentX(CENTER_ALIGNMENT);
			add(play);
			add(Box.createVerticalStrut(Block.SIZE));
			levels = initializeButton("src/res/img/ui/levels.png",
			"src/res/img/ui/levelsPressed.png");
			levels.setAlignmentX(CENTER_ALIGNMENT);
			add(levels);
			add(Box.createVerticalGlue());
			add(Box.createVerticalGlue());
			add(Box.createVerticalGlue());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private JButton initializeButton(String imgPath,
	String pressedImgPath) {
		JButton b = new JButton(new ImageIcon(imgPath));
		b.setPressedIcon(new ImageIcon(pressedImgPath));
		b.setContentAreaFilled(false);
		b.setBorderPainted(false);
		b.setFocusPainted(false);
		b.addActionListener(this);
		return b;
	}

	public void paintComponent(Graphics g) {
		drawBackground(g);
	}

	public void drawBackground(Graphics g) {
		Dimension size = getPreferredSize();
		double ratio = size.getWidth() / size.getHeight();
		double imgRatio = (double) bg.getWidth(this)
		/ bg.getHeight(this);
		int width, height;
		if (ratio > imgRatio) {
			width = (int) size.getWidth();
			height = (int) (size.getWidth() / bg.getWidth(this)
			* bg.getHeight(this));
		} else {
			height = (int) size.getHeight();
			width = (int) (ratio * height);
		}
		g.drawImage(bg, -(width - (int) size.getWidth()) / 2,
		-(height - (int) size.getHeight()) / 2, width, height, this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == play) {
			Level level = new Level(1,
			"src/res/img/backgrounds/grasslands.png", frame, this);
			frame.setContentPane(level);
		} else if (e.getSource() == levels) {
			frame.setContentPane(new LevelScreen(frame));
		}
		click.play(false);
		frame.revalidate();
		frame.repaint();
	}
}
