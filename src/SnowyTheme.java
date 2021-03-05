import java.awt.Graphics2D;
import java.awt.Point;
import java.util.List;

public class SnowyTheme extends Theme {

	@Override
	public String getBackgroundImagePath() {
		return "img/backgrounds/forest.png";
	}

	@Override
	public String getBlockImagePathStub() {
		return "tundra";
	}

	@Override
	public SoundEffect getBackgroundNoise() {
		return SoundEffect.SNOW;
	}

	@Override
	public void drawParticles(Graphics2D g2, Player player, Map map) {
		List<Point> particles = this.getParticles();
		
		g2.setColor(WeatherConstants.SNOW_COLOR);
		int randX = (int) (Math.random() * Window.DIMENSIONS.width);
		particles.add(new Point(randX, 0));
		particles.add(new Point(randX, 0));
		for (int i = 0; i < particles.size(); i++) {
			Point p = particles.get(i);
			g2.fillOval(p.x - WeatherConstants.SNOW_RADIUS, p.y - WeatherConstants.SNOW_RADIUS, 2 * WeatherConstants.SNOW_RADIUS, 2 * WeatherConstants.SNOW_RADIUS);
			p.translate(0, WeatherConstants.SNOW_SPEED);
			if (p.y >= Window.DIMENSIONS.height) {
				particles.remove(i);
				i--;
			} else if (player != null && map != null) {
				if (map.getBlocks()[p.y / Block.SIZE][p.x / Block.SIZE].isSolid()
				|| player.intersectsPoint(p)) {
					particles.remove(i);
					i--;
				}
			}
		}
	}

}
