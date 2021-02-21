import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Rectangle;
import java.io.File;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.*;

public class UIFactory {
	
	private static final String FONT_PATH = "font.ttf";
	
	private static Font font;
	private static final HashMap<Integer, Font> FONT_MAP = new HashMap<Integer, Font>();
	
	public static final HashMap<String, Image> OUTLINED_MAP = new HashMap<String, Image>();
	
	static {
		try {
			font = Font.createFont(Font.TRUETYPE_FONT, new File(FONT_PATH));
			GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(font);
			
			for (int i = 0; i < 10; i++) {
				OUTLINED_MAP.put(i + "", ImageIO.read(new File("img/ui/hud_" + i + ".png")));
			}
			OUTLINED_MAP.put(":", ImageIO.read(new File("img/ui/hud_colon.png")));
			OUTLINED_MAP.put(".", ImageIO.read(new File("img/ui/hud_dot.png")));
			OUTLINED_MAP.put("x", ImageIO.read(new File("img/ui/hud_x.png")));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static Font getFont(int fontSize) {
		if (!FONT_MAP.containsKey(fontSize)) {
			FONT_MAP.put(fontSize, font.deriveFont((float) fontSize));
		}
		return FONT_MAP.get(fontSize);
	}
	
	public static JLabel createLabel(String text, int fontSize) {
		JLabel label = new JLabel(text, SwingConstants.CENTER);
		label.setFont(getFont(fontSize));
		label.setForeground(Color.WHITE);
		return label;
	}
	
	public static JLabel createLabel(String text, int fontSize, Rectangle bounds) {
		JLabel label = createLabel(text, fontSize);
		label.setBounds(bounds);
		return label;
	}
	
	public static JLabel createLabel(ImageIcon icon) {
		JLabel label = new JLabel(icon);
		return label;
	}
	
	public static JLabel createLabel(ImageIcon icon, int x, int y) {
		JLabel label = createLabel(icon);
		label.setBounds(x, y, icon.getIconWidth(), icon.getIconHeight());
		return label;
	}
	
	public static JLabel createOutlinedLabel(String text) {
		JLabel label = new OutlinedLabel(text);
		return label;
	}

	public static JTextArea createTextArea(String text, int row, int column) {
		JTextArea textArea = new JTextArea(text, row, column);
		textArea.setWrapStyleWord(true);
		textArea.setLineWrap(true);
		textArea.setOpaque(false);
		textArea.setEditable(false);
		textArea.setFocusable(false);
		textArea.setBackground(UIManager.getColor("Label.background"));
		textArea.setBorder(UIManager.getBorder("Label.border"));
		return textArea;
	}
	
	public static JLabel createOutlinedLabel(String text, int x, int y) {
		JLabel label = createOutlinedLabel(text);
		label.setBounds(x, y, label.getPreferredSize().width, label.getPreferredSize().height);
		return label;
	}
	
	public static JButton createButton(ImageIcon icon, ImageIcon pressedIcon) {
		JButton b = new JButton(icon);
		b.setPressedIcon(pressedIcon);
		b.setContentAreaFilled(false);
		b.setBorderPainted(false);
		b.setFocusPainted(false);
		b.setBorder(null);
		return b;
	}
	
	public static JButton createButton(ImageIcon icon, ImageIcon pressedIcon, int x, int y) {
		JButton b = createButton(icon, pressedIcon);
		b.setBounds(x, y, icon.getIconWidth(), icon.getIconHeight());
		return b;
	}
	
	public static JPanel createHelpPanel(String text, int x, int y) {
		JPanel panel = new JPanel();
		panel.setOpaque(false);
		panel.setLayout(null);
		panel.setBounds(x, y, 300, 150);
		JLabel helpText = UIFactory.createLabel("<html>" + text + "</html>", 20);
		helpText.setBounds(20, 20, 260, 110);
		panel.add(helpText);
		JLabel helpBG = new JLabel(new ImageIcon("img/ui/help.png"));
		helpBG.setBounds(0, 0, 300, 150);
		panel.add(helpBG);
		return panel;
	}
	
	public static JPanel createHelpPanel(String text, ImageIcon icon, int x, int y) {
		JPanel panel = new JPanel();
		panel.setOpaque(false);
		panel.setLayout(null);
		panel.setBounds(x, y, 300, 150);
		Image image = icon.getImage();
		image = image.getScaledInstance(50, 50, Image.SCALE_DEFAULT);
		JLabel powerUp = new JLabel(new ImageIcon(image));
		powerUp.setBounds(8, 50, 50, 50);
		panel.add(powerUp);
		JLabel helpText = UIFactory.createLabel("<html>" + text + "</html>", 20);
		helpText.setBounds(60, 20, 220, 110);
		panel.add(helpText);
		JLabel helpBG = new JLabel(new ImageIcon("img/ui/help.png"));
		helpBG.setBounds(0, 0, 300, 150);
		panel.add(helpBG);
		return panel;
	}
}
