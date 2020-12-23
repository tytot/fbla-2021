
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.BorderFactory;
import javax.swing.Box;
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
	private static final Font TITLE_FONT = new Font("Courier New", Font.BOLD, 100);
	private static final Font OPTION_FONT = new Font("Courier New", Font.BOLD, 25);
	private static final Font SLIDER_FONT = new Font("Courier New", Font.BOLD, 12);

	SettingScreen(JFrame frame) {

		// Setting up Frame and Layout and adding HashTable Labels
		this.frame = frame;
		this.setBackground(Color.BLACK);
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
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
		this.add(Box.createVerticalStrut(100));
		this.add(title);

		// Setting up Setting Options
		OptionPanel = new JPanel();
		OptionPanel.setBackground(Color.BLACK);

		// Volume
		Volume = new JLabel("Volume");
		Volume.setAlignmentX(JLabel.LEFT_ALIGNMENT);
		Volume.setFont(OPTION_FONT);
		Volume.setForeground(Color.white);
		OptionPanel.add(Volume);
		OptionPanel.add(Box.createHorizontalStrut(20));

		// Volume Slider
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
		this.add(Box.createVerticalStrut(200));
		this.add(OptionPanel);

	}
}
