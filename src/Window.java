
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

class Window {
	private JFrame frame;
	
	public static final Dimension DIMENSIONS = new Dimension(33 * Block.SIZE, 24 * Block.SIZE);

	Window() {
		frame = new JFrame("The Puzzled Cube");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setPreferredSize(DIMENSIONS);
		frame.setMinimumSize(DIMENSIONS);
		frame.setMaximumSize(DIMENSIONS);
		frame.setContentPane(new MainScreen(frame));
		frame.pack();
		frame.setVisible(true);
		System.out.println(DIMENSIONS.getHeight() + ", " + DIMENSIONS.getWidth());
		
		SoundEffect.MUSIC.play(true);
	}

	private static void runGUI() {
		new Window();
	}

	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				runGUI();
			}
		});
	}
}