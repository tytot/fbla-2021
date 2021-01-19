
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.io.File;

public class FontLoader {
	public static Font loadFont(String path, float size) {
		try {
			Font customFont = Font
			.createFont(Font.TRUETYPE_FONT, new File(path))
			.deriveFont(size);
			GraphicsEnvironment ge = GraphicsEnvironment
			.getLocalGraphicsEnvironment();
			ge.registerFont(customFont);
			return customFont;
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		return null;
	}
}
