import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Rectangle;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class UIFactory {
	
	private static final String FONT_PATH = "font.ttf";
	
	private static Font font;
	private static final HashMap<Integer, Font> FONT_MAP = new HashMap<Integer, Font>();
	
	public static final HashMap<String, Image> OUTLINED_MAP = new HashMap<String, Image>();
	
	static {
		try {
			font = Font.createFont(Font.TRUETYPE_FONT, UIFactory.class.getResourceAsStream(FONT_PATH));
			GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(font);
			
			for (int i = 0; i < 10; i++) {
				OUTLINED_MAP.put(i + "", ImageIO.read(UIFactory.class.getResource("img/ui/hud_" + i + ".png")));
			}
			OUTLINED_MAP.put(":", ImageIO.read(UIFactory.class.getResource("img/ui/hud_colon.png")));
			OUTLINED_MAP.put(".", ImageIO.read(UIFactory.class.getResource("img/ui/hud_dot.png")));
			OUTLINED_MAP.put("x", ImageIO.read(UIFactory.class.getResource("img/ui/hud_x.png")));
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
	
	public static JLabel createLabel(String iconPath) {
		ImageIcon icon = new ImageIcon(UIFactory.class.getResource(iconPath));
		JLabel label = new JLabel(icon);
		return label;
	}
	
	public static JLabel createLabel(String iconPath, int x, int y) {
		ImageIcon icon = new ImageIcon(UIFactory.class.getResource(iconPath));
		return createLabel(icon, x, y);
	}
	
	public static JLabel createOutlinedLabel(String text) {
		JLabel label = new OutlinedLabel(text);
		return label;
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
	
	public static JButton createButton(String iconPath, String pressedIconPath) {
		ImageIcon icon = new ImageIcon(UIFactory.class.getResource(iconPath));
		ImageIcon pressedIcon = new ImageIcon(UIFactory.class.getResource(pressedIconPath));
		return createButton(icon, pressedIcon);
	}
	
	public static JButton createButton(String iconPath, String pressedIconPath, int x, int y) {
		ImageIcon icon = new ImageIcon(UIFactory.class.getResource(iconPath));
		ImageIcon pressedIcon = new ImageIcon(UIFactory.class.getResource(pressedIconPath));
		return createButton(icon, pressedIcon, x, y);
	}
	
	public static JPanel createHelpPanel(String text, int x, int y) {
		JPanel panel = new JPanel();
		panel.setOpaque(false);
		panel.setLayout(null);
		panel.setBounds(x, y, 300, 150);
		JLabel helpText = UIFactory.createLabel("<html>" + text + "</html>", 20);
		helpText.setBounds(20, 20, 260, 110);
		panel.add(helpText);
		JLabel helpBG = new JLabel(new ImageIcon(UIFactory.class.getResource("img/ui/help.png")));
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
		JLabel helpBG = new JLabel(new ImageIcon(UIFactory.class.getResource("img/ui/help.png")));
		helpBG.setBounds(0, 0, 300, 150);
		panel.add(helpBG);
		return panel;
	}
	
	public static JPanel createHelpPanel(String text, String iconPath, int x, int y) {
		ImageIcon icon = new ImageIcon(UIFactory.class.getResource(iconPath));
		return createHelpPanel(text, icon, x, y);
	}
}
