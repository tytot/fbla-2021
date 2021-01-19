import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class GoalBlock extends MapBlock {

	public static final int PRESSED_SIZE = (int) (0.3 * Block.SIZE);

	boolean pressed = false;
	private Image unpressedImg;
	private Image pressedImg;

	public GoalBlock() throws IOException {
		unpressedImg = ImageIO
		.read(new File("src/res/img/sprites/buttons/button.png"));
		pressedImg = ImageIO.read(
		new File("src/res/img/sprites/buttons/buttonPressed.png"));
	}

	public boolean isPressed() {
		return pressed;
	}

	public void press() {
		pressed = true;
	}

	public void unpress() {
		pressed = false;
	}

	public Image getImage() {
		if (pressed) {
			return pressedImg;
		}
		return unpressedImg;
	}

	@Override
	public boolean isSolid() {
		return false;
	}
}
