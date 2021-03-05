import java.awt.Graphics2D;
import java.awt.Point;
import java.util.List;

public class SandyTheme extends Theme {

	@Override
	public String getBackgroundImagePath() {
		return "img/backgrounds/desert.png";
	}

	@Override
	public String getBlockImagePathStub() {
		return "sand";
	}

	@Override
	public SoundEffect getBackgroundNoise() {
		return SoundEffect.WIND;
	}

	@Override
	public void drawParticles(Graphics2D g2, Player player, Map map) {
		List<Point> particles = this.getParticles();
		
		g2.setColor(WeatherConstants.SAND_COLOR);
		int randX = (int) (Math.random() * Window.DIMENSIONS.width);
		particles.add(new Point(randX, 0));
		for (int i = 0; i < WeatherConstants.SAND_SPEED_X / WeatherConstants.SAND_SPEED_Y; i++) {
			int randY = (int) (Math.random() * Window.DIMENSIONS.height);
			particles.add(new Point(0, randY));
		}
		for (int i = 0; i < particles.size(); i++) {
			Point p = particles.get(i);
			g2.fillRect(p.x - WeatherConstants.SAND_RADIUS, p.y - WeatherConstants.SAND_RADIUS, 2 * WeatherConstants.SAND_RADIUS, 2 * WeatherConstants.SAND_RADIUS);
			p.translate(WeatherConstants.SAND_SPEED_X, WeatherConstants.SAND_SPEED_Y);
			if (p.y >= Window.DIMENSIONS.height || p.x >= Window.DIMENSIONS.width) {
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
