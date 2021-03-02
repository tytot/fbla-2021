
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class MainScreen extends JPanel implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5053519158223165280L;
	private JFrame frame;
	private JButton play, levels, leaders, tutorial;

	MainScreen(JFrame frame) {
		this.frame = frame;
		setPreferredSize(new Dimension(33 * Block.SIZE, 24 * Block.SIZE));
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		add(Box.createVerticalGlue());
		JLabel title = UIFactory.createLabel("img/ui/title.png");
		title.setAlignmentX(CENTER_ALIGNMENT);
		add(title);
		add(Box.createVerticalGlue());
		play = UIFactory.createButton("img/ui/play.png", "img/ui/playPressed.png");
		play.setAlignmentX(CENTER_ALIGNMENT);
		play.addActionListener(this);
		add(play);
		add(Box.createVerticalStrut(Block.SIZE));
		levels = UIFactory.createButton("img/ui/levels.png", "img/ui/levelsPressed.png");
		levels.setAlignmentX(CENTER_ALIGNMENT);
		levels.addActionListener(this);
		add(levels);
		add(Box.createVerticalStrut(Block.SIZE));
		leaders = UIFactory.createButton("img/ui/leaderboard.png", "img/ui/leaderboardPressed.png");
		leaders.setAlignmentX(CENTER_ALIGNMENT);
		leaders.addActionListener(this);
		add(leaders);
		add(Box.createVerticalStrut(Block.SIZE));
		tutorial = UIFactory.createButton(new ImageIcon("img/ui/instructions.png"), new ImageIcon("img/ui/instructionsPressed.png"));
		tutorial.setAlignmentX(CENTER_ALIGNMENT);
		tutorial.addActionListener(this);
		add(tutorial);
		add(Box.createVerticalGlue());
	}

	public void paintComponent(Graphics g) {
		g.drawImage(new PlainTheme().getBackgroundImage(), 0, 0, null);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == play) {
			Level level = new Level(1, true, false, 0, frame, this);
			frame.setContentPane(level);
		} else if (e.getSource() == levels) {
			frame.setContentPane(new LevelScreen(frame));
		} else if (e.getSource() == leaders) {
			frame.setContentPane(new LeaderboardScreen(frame));
		} else if (e.getSource() == tutorial) {
			frame.setContentPane(new TutorialScreen(frame));
		}

		SoundEffect.CLICK.play(false);
		frame.revalidate();
		frame.repaint();
	}
}