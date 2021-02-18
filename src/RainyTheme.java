import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.List;

public class RainyTheme extends Theme {

	@Override
	public String getBackgroundImagePath() {
		return "img/backgrounds/castles.png";
	}

	@Override
	public String getBlockImagePathStub() {
		return "stone";
	}

	@Override
	public SoundEffect getBackgroundNoise() {
		return SoundEffect.RAIN;
	}

	@Override
	public void drawParticles(Graphics2D g2, Player player, Map map) {
		List<Point> particles = this.getParticles();
		
		g2.setColor(WeatherConstants.RAIN_COLOR);
		g2.setStroke(WeatherConstants.RAIN_STROKE);
		particles.add(new Point((int) (Math.random() * Window.DIMENSIONS.width), 0));
		particles.add(new Point((int) (Math.random() * Window.DIMENSIONS.width), 0));
		particles.add(new Point((int) (Math.random() * Window.DIMENSIONS.width), 0));
		for (int i = 0; i < particles.size(); i++) {
			Point p = particles.get(i);
			g2.drawLine(p.x, p.y - WeatherConstants.RAIN_LENGTH, p.x, p.y);
			p.translate(0, WeatherConstants.RAIN_SPEED);
			if (p.y - WeatherConstants.RAIN_LENGTH >= Window.DIMENSIONS.height) {
				particles.remove(i);
				i--;
			} else if ((map.isValidBlock(p.x / Block.SIZE, p.y / Block.SIZE)
				&& map.getBlocks()[p.y / Block.SIZE][p.x / Block.SIZE].isSolid())
				|| player.intersectsPoint(p)) {
				p.setLocation(p.x, p.y / Block.SIZE * Block.SIZE);
				g2.drawLine(p.x - 6, p.y - 6, p.x - 4, p.y - 4);
				g2.drawLine(p.x + 4, p.y - 4, p.x + 6, p.y - 6);
				particles.remove(i);
				i--;
			}
		}
	}

}
