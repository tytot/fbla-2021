
import java.awt.Dimension;

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

	public static void runGUI() {
		new Window();
	}

	public static void main(String[] args) {
		System.setProperty("sun.java2d.uiScale", "1");
		
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				runGUI();
			}
		});
	}
}