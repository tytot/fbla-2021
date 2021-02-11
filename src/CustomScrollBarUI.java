import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.plaf.basic.BasicScrollBarUI;

public class CustomScrollBarUI extends BasicScrollBarUI {
	
	@Override
	protected JButton createIncreaseButton(int orientation) {
		ImageIcon icon = new ImageIcon("img/ui/blue_sliderDown.png");
		JButton button = UIFactory.createButton(icon, icon);
		return button;
	}
	
	@Override
	protected JButton createDecreaseButton(int orientation) {
		ImageIcon icon = new ImageIcon("img/ui/blue_sliderUp.png");
		JButton button = UIFactory.createButton(icon, icon);
		return button;
	}
	
    @Override
    protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
    	Graphics2D g2 = (Graphics2D) g;
    	g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    	g.setColor(new Color(159, 186, 188));
        g.fillRoundRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height, 8, 8);
    }

    @Override
    protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
    	Graphics2D g2 = (Graphics2D) g;
    	g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    	g.setColor(new Color(25, 137, 184));
        g.fillRoundRect(thumbBounds.x, thumbBounds.y, thumbBounds.width, thumbBounds.height, 8, 8);
        g.setColor(new Color(53, 186, 243));
        g.fillRoundRect(thumbBounds.x + 2, thumbBounds.y + 2, thumbBounds.width - 4, thumbBounds.height - 4, 8, 8);
        g.setColor(new Color(30, 167, 225));
        g.fillRect(thumbBounds.x + 4, thumbBounds.y + 4, thumbBounds.width - 8, thumbBounds.height - 8);
     }
	
    
    @Override
    public Dimension getPreferredSize(JComponent c) {
    	return new Dimension(28, 0);
    }
}
