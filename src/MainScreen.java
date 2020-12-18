
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class MainScreen extends JPanel {
	private JFrame frame;
	private JPanel buttonPanel;
	private JLabel title;
	private ArrayList<JButton> buttons = new ArrayList<JButton>();
	private GridLayout layout;
	private static final Font TITLE_FONT = new Font("Serif", Font.BOLD, 50);

	MainScreen(JFrame frame) {
		this.frame = frame;
		frame.setPreferredSize(new Dimension(600, 350));
		this.setBorder(BorderFactory.createEmptyBorder(25, 0, 0, 0));
		this.setBackground(Color.BLACK);
		title = new JLabel("The Puzzled Cube", SwingConstants.CENTER);
		title.setFont(TITLE_FONT);
		title.setForeground(Color.RED);
		this.add(title);
		buttonPanel = new JPanel();
		buttonPanel.setBackground(Color.black);
		buttonPanel.setBorder(BorderFactory.createEmptyBorder(25, 0, 0, 0));
		layout = new GridLayout(0, 2);
		buttonPanel.setLayout(layout);
		buttons.add(new JButton("Play"));
		buttons.add(new JButton("Levels"));
		buttons.add(new JButton("Settings"));
		buttons.add(new JButton("Leaderboard"));
		for (JButton button : buttons) {
			button.setBackground(Color.black);
			button.setFont(new Font("Serif", Font.BOLD, 25));
			button.setForeground(Color.WHITE);
			button.setFocusPainted(false);
			button.setBorderPainted(false);
			buttonPanel.add(button);
		}
		layout.setHgap(50);
		layout.setVgap(50);
		this.add(buttonPanel);

	}

}
