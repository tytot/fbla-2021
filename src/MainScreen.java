
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.*;

public class MainScreen extends JPanel implements ActionListener {
	private JFrame frame;
	private JPanel buttonPanel;
	private JLabel title;
	private ArrayList<JButton> buttons = new ArrayList<JButton>();
	private GridLayout layout;
	private final int INITIAL_X = 270;
	private final int INITIAL_Y = 250;
	private final int DELAY = 25;
	private Timer timer;
	private int x, y;
	private boolean anim = true;
	private static final Font TITLE_FONT = new Font("Serif", Font.BOLD, 50);

	MainScreen(JFrame frame) {
		this.frame = frame;
		this.setBorder(BorderFactory.createEmptyBorder(400, 0, 0, 0));
		this.setBackground(Color.BLACK);
		buttonPanel = new JPanel();
		buttonPanel.setBackground(Color.black);
		layout = new GridLayout(0, 2);
		buttonPanel.setLayout(layout);
		buttons.add(new JButton("Play"));
		buttons.add(new JButton("Levels"));
		buttons.add(new JButton("Settings"));
		buttons.add(new JButton("Leaderboard"));
		for (int i = 0; i < buttons.size(); i++) {
			JButton button = buttons.get(i);
			button.setBackground(Color.black);
			button.setFont(new Font("Courier New", Font.BOLD, 60));
			button.setForeground(Color.WHITE);
			button.setFocusPainted(false);
			button.setBorderPainted(false);
			buttonPanel.add(button);
			button.addActionListener(this);
			button.setActionCommand(button.getText());
		}
		layout.setHgap(50);
		layout.setVgap(100);
		this.add(buttonPanel);

		x = INITIAL_X;
		y = INITIAL_Y;

		timer = new Timer(DELAY, this);
		timer.start();


	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		moveTitle(g);
	}

	private void moveTitle(Graphics g) {
		g.setFont(new Font("Serif", Font.BOLD, 100));
		g.setColor(Color.blue);
		g.drawString("The Puzzled Cube", x, y);
		g.setColor(Color.green);
		g.drawString("The Puzzled Cube", x-4, y-4);
		g.setColor(Color.red);
		g.drawString("The Puzzled Cube", x-7, y-7);
		Toolkit.getDefaultToolkit().sync();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(anim) {
			x += 1;
			y += 1;
		} else {
			x-= 1;
			y -= 1;
		}

		if (y > 250) {
			anim = false;
		} else if (y < 200) {
			anim = true;
		}
		String c = e.getActionCommand();
		if (c != null) {
			if (c.equals("Play")) {
				Level level = new Level(1, frame, this);
				frame.setContentPane(level);
			} else if (c.equals("Levels"))
				frame.setContentPane(new LevelScreen(frame));
			else if (c.equals("Settings"))
				frame.setContentPane(new SettingScreen(frame));
			else if (c.equals("Leaderboard"))
				frame.setContentPane(new LeaderboardScreen(frame));
			frame.revalidate();
			frame.repaint();
		}
		repaint();
	}
}
