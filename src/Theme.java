import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public abstract class Theme {
	
	private ArrayList<Point> particles = new ArrayList<Point>();
	
	public abstract String getBackgroundImagePath();
	
	public Image getBackgroundImage() {
		return ImageFactory.fetchBackgroundImage(getBackgroundImagePath());
	}
	
	public abstract String getBlockImagePathStub();
	
	public abstract SoundEffect getBackgroundNoise();
	
	public abstract void drawParticles(Graphics2D g2, Player player, Map map);
	
	public List<Point> getParticles() {
		return particles;
	}
}
