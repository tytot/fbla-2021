
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class LevelScreen extends JPanel {
	private JFrame frame;
	private JPanel titlePanel, level1, level2, level3, level4;
	private JLabel title;
	private ArrayList<JButton[]> levels = new ArrayList<JButton[]>();
	private int currentCard = 1;
	private CardLayout cl;
	private GridLayout layout, buttonLayout;
	private static final Font TITLE_FONT = new Font("Serif", Font.BOLD, 50);

	LevelScreen(JFrame frame) {

		// Setting up Frame and layout
		this.frame = frame;
		this.setBackground(Color.BLACK);
		frame.setPreferredSize(new Dimension(600, 400));
		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		// Setting up the Title
		titlePanel = new JPanel();
		titlePanel.setBackground(Color.BLACK);
		title = new JLabel("Levels", SwingConstants.CENTER);
		title.setFont(TITLE_FONT);
		title.setForeground(Color.RED);
		titlePanel.add(title);
		c.gridx = 0;
		c.gridy = 0;
		c.fill = GridBagConstraints.VERTICAL;
		this.add(titlePanel, c);

		// Setting up different levels
		final JPanel cardPanel = new JPanel();
		c.gridy = 1;
		c.anchor = GridBagConstraints.CENTER;
		cl = new CardLayout();
		cardPanel.setLayout(cl);
		layout = new GridLayout(0, 3);

		// Setting up First Level Set
		level1 = new JPanel();
		level1.setBackground(Color.black);
		level1.setLayout(layout);
		level1.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));
		JButton[] level1Buttons = new JButton[6];
		for (int i = 0; i < level1Buttons.length; i++) {
			level1Buttons[i] = new JButton(String.valueOf(i + 1));
		}
		levels.add(level1Buttons);
		for (JButton level : levels.get(0)) {
			level.setPreferredSize(new Dimension(60, 35));
			level.setBackground(Color.BLACK);
			level.setFont(new Font("Serif", Font.BOLD, 25));
			level.setForeground(Color.WHITE);
			level.setFocusPainted(false);
			level.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.red));
			level1.add(level);
		}
		layout.setHgap(100);
		layout.setVgap(50);
		level1.add(Box.createVerticalGlue());
		cardPanel.add(level1, "1");

		// Setting up Second Level Set
		level2 = new JPanel();
		level2.setBackground(Color.black);
		level2.setLayout(layout);
		level2.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));
		JButton[] level2Buttons = new JButton[6];
		for (int i = 0; i < level2Buttons.length; i++) {
			level2Buttons[i] = new JButton(String.valueOf(i + 7));
		}
		levels.add(level2Buttons);
		for (JButton level : levels.get(1)) {
			level.setPreferredSize(new Dimension(60, 35));
			level.setBackground(Color.BLACK);
			level.setFont(new Font("Serif", Font.BOLD, 25));
			level.setForeground(Color.WHITE);
			level.setFocusPainted(false);
			level.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.red));
			level2.add(level);
		}
		layout.setHgap(100);
		layout.setVgap(50);
		level2.add(Box.createVerticalGlue());
		cardPanel.add(level2, "2");

		// Setting up Third Level Set
		level3 = new JPanel();
		level3.setBackground(Color.black);
		level3.setLayout(layout);
		level3.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));
		JButton[] level3Buttons = new JButton[6];
		for (int i = 0; i < level1Buttons.length; i++) {
			level1Buttons[i] = new JButton(String.valueOf(i + 13));
		}
		levels.add(level3Buttons);
		for (JButton level : levels.get(0)) {
			level.setPreferredSize(new Dimension(60, 35));
			level.setBackground(Color.BLACK);
			level.setFont(new Font("Serif", Font.BOLD, 25));
			level.setForeground(Color.WHITE);
			level.setFocusPainted(false);
			level.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.red));
			level3.add(level);
		}
		layout.setHgap(100);
		layout.setVgap(50);
		level3.add(Box.createVerticalGlue());
		cardPanel.add(level3, "3");

		// Setting up Fourth Level Set
		level4 = new JPanel();
		level4.setBackground(Color.black);
		level4.setLayout(layout);
		level4.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));
		JButton[] level4Buttons = new JButton[6];
		for (int i = 0; i < level4Buttons.length; i++) {
			level1Buttons[i] = new JButton(String.valueOf(i + 19));
		}
		levels.add(level4Buttons);
		for (JButton level : levels.get(0)) {
			level.setPreferredSize(new Dimension(60, 35));
			level.setBackground(Color.BLACK);
			level.setFont(new Font("Serif", Font.BOLD, 25));
			level.setForeground(Color.WHITE);
			level.setFocusPainted(false);
			level.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.red));
			level4.add(level);
		}
		layout.setHgap(100);
		layout.setVgap(50);
		level4.add(Box.createVerticalGlue());
		cardPanel.add(level4, "4");


		this.add(cardPanel, c);

		JPanel buttonPanel = new JPanel();
		buttonLayout = new GridLayout(1, 0);
		buttonLayout.setHgap(50);
		buttonPanel.setLayout(buttonLayout);
		buttonPanel.setBackground(Color.black);
		// Add Back Button
		JButton back = new JButton("Previous");
		back.setBackground(Color.black);
		back.setFont(new Font("Serif", Font.BOLD, 25));
		back.setForeground(Color.white);
		back.setFocusPainted(false);

		buttonPanel.add(back);

		// Add Next Button
		JButton next = new JButton("Next");
		next.setBackground(Color.black);
		next.setFont(new Font("Serif", Font.BOLD, 25));
		next.setForeground(Color.white);
		next.setFocusPainted(false);
		buttonPanel.add(next);

		next.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (currentCard < 4) {
					currentCard += 1;
					cl.show(cardPanel, "" + (currentCard));
				}
			}
		});

		back.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (currentCard > 1) {
					currentCard -= 1;
					cl.show(cardPanel, "" + (currentCard));
				}
			}
		});

		c.gridy = 2;
		this.add(buttonPanel, c);
	}

}
