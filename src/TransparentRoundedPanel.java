import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.JPanel;

public class TransparentRoundedPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2147641524455782201L;
	private Color color;

	TransparentRoundedPanel(Color color) {
		this.color = color;
		setOpaque(false);
	}

	public void paintComponent(Graphics g) {
		g.setColor(color);
		Rectangle r = g.getClipBounds();
		g.fillRoundRect(r.x, r.y, r.width, r.height, 16, 16);
		super.paintComponent(g);
	}
}