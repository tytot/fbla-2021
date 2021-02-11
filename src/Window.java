
import java.awt.Dimension;

import javax.swing.*;

class Window {
	private JFrame frame;
	private final Dimension d = new Dimension(33, 24);
	private SoundEffect music = new SoundEffect(SoundEffect.MUSIC, -10f);

	Window() {
		frame = new JFrame("The Puzzled Cube");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setPreferredSize(new Dimension(d.width * Block.SIZE, d.height * Block.SIZE));
		frame.setMinimumSize(new Dimension(d.width * Block.SIZE, d.height * Block.SIZE));
		frame.setMaximumSize(new Dimension(d.width * Block.SIZE, d.height * Block.SIZE));
		frame.setContentPane(new MainScreen(frame));
		frame.pack();
		frame.setVisible(true);
		
		music.play(true);
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