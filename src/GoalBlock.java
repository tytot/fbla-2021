import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class GoalBlock extends MapBlock {
	
	public static final String PATH = "img/sprites/buttons/button";
	public static final int PRESSED_SIZE = (int) (0.3 * Block.SIZE);
	
	private Image unpressedImg, pressedImg;
	
	private boolean isPressed;
	
	GoalBlock() {
		unpressedImg = ImageFactory.fetchImage("img/sprites/buttons/button.png");
		pressedImg = ImageFactory.fetchImage("img/sprites/buttons/buttonPressed.png");
	}
	
	public boolean isPressed() {
		return isPressed;
	}
	
	public void press() {
		isPressed = true;
	}
	
	public void unpress() {
		isPressed = false;
	}
	
	@Override
	public Image getImage() {
		return isPressed ? pressedImg : unpressedImg;
	}

    @Override
    public boolean isSolid() {
        return false;
    }
}
