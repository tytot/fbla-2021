
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;

import javax.swing.*;

class Window {
	private JFrame frame;
	// private MenuBar menuBar;
	boolean isMainScreen;
	boolean isEditActionItemScreen;
	
	private final Dimension d = new Dimension(33, 23);
	
	public static long startTime, endTime;

	Window() {
		frame = new JFrame("The Puzzled Cube");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel currentScreenPanel = new MainScreen(frame);
		frame.setContentPane(currentScreenPanel);
		frame.pack();
		frame.setVisible(true);
		frame.setPreferredSize(new Dimension(d.width * MapBlock.SIZE, d.height * MapBlock.SIZE));
		frame.setMinimumSize(new Dimension(d.width * MapBlock.SIZE, d.height * MapBlock.SIZE));
		frame.setMaximumSize(new Dimension(d.width * MapBlock.SIZE, d.height * MapBlock.SIZE));
	}

	private static void runGUI() {
		startTime = System.currentTimeMillis();
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