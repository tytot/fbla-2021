import java.awt.BasicStroke;
import java.awt.Color;

public class WeatherConstants {
	
	public static final int RAIN_SPEED = 20;
	public static final BasicStroke RAIN_STROKE = new BasicStroke(3);
	public static final int RAIN_LENGTH = 25;
	public static final Color RAIN_COLOR = new Color(161, 198, 204, 150);
	
	public static final int SNOW_SPEED = 5;
	public static final int SNOW_RADIUS = 4;
	public static final Color SNOW_COLOR = new Color(255, 255, 255, 150);
	
	public static final int SAND_SPEED = 10;
	public static final int SAND_ANGLE = 15;
	public static final int SAND_SPEED_X = (int) Math.round(Math.cos(Math.toRadians(SAND_ANGLE)) * SAND_SPEED);
	public static final int SAND_SPEED_Y = (int) Math.round(Math.sin(Math.toRadians(SAND_ANGLE)) * SAND_SPEED);
	public static final int SAND_RADIUS = 2;
	public static final Color SAND_COLOR = new Color(229, 214, 160);
}
