
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;

public class SettingScreen extends JPanel {
	private JFrame frame;
	private JPanel OptionPanel;
	private JLabel title, Volume;
	private ArrayList<JButton> buttons;
	private ArrayList<JLabel> sliderOptions;
	private GridLayout layout;
	private JSlider volumeSlider = new JSlider();
	private Hashtable<Integer, JLabel> labels = new Hashtable<>();
	private static final Font TITLE_FONT = new Font("Serif", Font.BOLD, 50);
	private static final Font OPTION_FONT = new Font("Serif", Font.BOLD, 25);
	private static final Font SLIDER_FONT = new Font("Serif", Font.BOLD, 12);

	SettingScreen(JFrame frame) {

		// Setting up Frame and Layout and adding HashTable Labels
		this.frame = frame;
		frame.setPreferredSize(new Dimension(600, 350));
		this.setBackground(Color.BLACK);
		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		sliderOptions = new ArrayList<JLabel>();
		sliderOptions.add(new JLabel("MUTE"));
		sliderOptions.add(new JLabel("LOW"));
		sliderOptions.add(new JLabel("MEDIUM"));
		sliderOptions.add(new JLabel("HIGH"));
		for (JLabel option : sliderOptions) {
			option.setFont(SLIDER_FONT);
			option.setForeground(Color.white);
		}
		int counter = 0;
		for (int i = 0; i <= 75; i += 25) {
			labels.put(i, sliderOptions.get(counter));
			counter++;
		}

		buttons = new ArrayList<JButton>();
		// Setting up the Title
		title = new JLabel("Settings", SwingConstants.CENTER);
		title.setFont(TITLE_FONT);
		title.setForeground(Color.RED);
		title.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		c.gridx = 0;
		c.gridy = 0;
		this.add(title, c);

		// Setting up Setting Options
		OptionPanel = new JPanel();
		OptionPanel.setBackground(Color.BLACK);

		// Volume
		c.gridy = 1;
		Volume = new JLabel("Volume");
		Volume.setAlignmentX(JLabel.LEFT_ALIGNMENT);
		Volume.setFont(OPTION_FONT);
		Volume.setForeground(Color.white);
		OptionPanel.add(Volume);

		// Volume Slider
		c.gridx = 1;
		volumeSlider.setBackground(Color.black);
		volumeSlider.setForeground(Color.white);
		volumeSlider.setMaximum(75);
		volumeSlider.setMajorTickSpacing(25);
		volumeSlider.setLabelTable(labels);
		volumeSlider.setSnapToTicks(true);
		volumeSlider.setPaintLabels(true);
		volumeSlider.setPaintTicks(true);
		volumeSlider.setPaintLabels(true);

		OptionPanel.add(volumeSlider);

		this.add(OptionPanel, c);

	}
}
