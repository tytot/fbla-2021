import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;

public class LevelButton extends JButton {

	private int levelNumber;
	private Image normal, pressed;
	private Font font = FontLoader.loadFont("src/res/font.ttf", 72);

	LevelButton(int levelNumber) {
		this.levelNumber = levelNumber;
		try {
			this.normal = ImageIO
			.read(new File("src/res/img/ui/levelBlank.png"));
			this.pressed = ImageIO
			.read(new File("src/res/img/ui/levelBlankPressed.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		setContentAreaFilled(false);
		setBorderPainted(false);
		setFocusPainted(false);
	}

	public void paintComponent(Graphics g) {
		if (!getModel().isArmed()) {
			g.drawImage(normal, 0, 0, null);
		} else {
			g.drawImage(pressed, 0, 0, null);
		}
		g.setColor(Color.WHITE);
		g.setFont(font);
		FontMetrics metrics = g.getFontMetrics(font);
		RenderingHints rh = new RenderingHints(
		RenderingHints.KEY_TEXT_ANTIALIASING,
		RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHints(rh);
		if (!getModel().isArmed()) {
			g.drawString(levelNumber + "",
			((140 - metrics.stringWidth(levelNumber + "")) / 2), 94);
		} else {
			g.drawString(levelNumber + "",
			((140 - metrics.stringWidth(levelNumber + "")) / 2), 100);
		}
	}

	public Dimension getPreferredSize() {
		return new Dimension(140, 148);
	}
}
