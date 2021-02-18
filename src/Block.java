import java.awt.Image;

public abstract class Block {
	
	public static final int SIZE = 40;
	
	public static final int ALONE = 0;
	public static final int BOTTOM = 1;
	public static final int BOTTOM_LEFT = 2;
	public static final int BOTTOM_RIGHT = 3;
	public static final int CENTER = 4;
	public static final int LEFT = 5;
	public static final int MIDDLE = 6;
	public static final int RIGHT = 7;
	public static final int TOP = 8;
	public static final int TOP_LEFT = 9;
	public static final int TOP_RIGHT = 10;
	public static final int LEFT_AND_BOTTOM_RIGHT = 11;
	public static final int RIGHT_AND_BOTTOM_LEFT = 12;
	
	public final static String[] ENDS = { "", "Bottom", "BottomLeft", "BottomRight", "Center", "Left", "Mid", "Right", "Top", "TopLeft", "TopRight", "LeftAndBottomRight", "RightAndBottomLeft" };
	
	public abstract Image getImage();
}
