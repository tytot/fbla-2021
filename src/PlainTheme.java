import java.awt.Graphics2D;

public class PlainTheme extends Theme {

	@Override
	public String getBackgroundImagePath() {
		return "img/backgrounds/plain.png";
	}

	@Override
	public String getBlockImagePathStub() {
		return "grass";
	}

	@Override
	public SoundEffect getBackgroundNoise() {
		return null;
	}
	@Override
	public void drawParticles(Graphics2D g2, Player player, Map map) {
		// nothing for now
	}

}
