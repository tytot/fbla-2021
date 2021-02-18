import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;

public class ImageFactory {
	
	private static final HashMap<String, Image> CACHE = new HashMap<String, Image>();
	
	public static Image fetchImage(String path) {
		if (!CACHE.containsKey(path)) {
			try {
				Image image = ImageFactory.scaleToBlock(ImageIO.read(new File(path)));
				CACHE.put(path, image);
				return image;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return CACHE.get(path);
	}
	
	public static Image fetchImageBilinear(String path) {
		if (!CACHE.containsKey(path)) {
			try {
				Image image = ImageFactory.scaleToBlockBilinear(ImageIO.read(new File(path)));
				CACHE.put(path, image);
				return image;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return CACHE.get(path);
	}
	
	public static Image fetchBackgroundImage(String path) {
		if (!CACHE.containsKey(path)) {
			try {
				Image image = ImageFactory.scaleToWindow(ImageIO.read(new File(path)));
				CACHE.put(path, image);
				return image;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return CACHE.get(path);
	}
	
	private static BufferedImage scaleToBlock(BufferedImage original) {
		BufferedImage scaled = new BufferedImage(Block.SIZE, Block.SIZE, BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D g2 = scaled.createGraphics();
		g2.drawImage(original, 0, 0, Block.SIZE, Block.SIZE, null);
		g2.dispose();
		return scaled;
	}
	
	private static BufferedImage scaleToBlockBilinear(BufferedImage original) {
		BufferedImage scaled = new BufferedImage(Block.SIZE, Block.SIZE, BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D g2 = scaled.createGraphics();
		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2.drawImage(original, 0, 0, Block.SIZE, Block.SIZE, null);
		g2.dispose();
		return scaled;
	}
	
	private static BufferedImage scaleToWindow(BufferedImage original) {
		double ratio = Window.DIMENSIONS.getWidth() / Window.DIMENSIONS.getHeight();
		double imgRatio = (double) original.getWidth(null) / original.getHeight(null);
		int width, height;
		if (ratio > imgRatio) {
			width = Window.DIMENSIONS.width;
			height = (int) (Window.DIMENSIONS.getWidth() / original.getWidth(null) * original.getHeight(null));
		} else {
			height = Window.DIMENSIONS.height;
			width = (int) (imgRatio * height);
		}
		BufferedImage scaled = new BufferedImage(Window.DIMENSIONS.width, Window.DIMENSIONS.height, BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D g2 = scaled.createGraphics();
		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2.drawImage(original, -(width - Window.DIMENSIONS.width) / 2, -(height - Window.DIMENSIONS.height) / 2, width, height, null);
		g2.dispose();
		return scaled;
	}
}
