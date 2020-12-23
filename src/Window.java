
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;

import javax.swing.*;

class Window {
	private JFrame frame;
	// private MenuBar menuBar;
	private JPanel currentScreenPanel, testLevelPanel, testSettingPanel;
	boolean isMainScreen;
	boolean isEditActionItemScreen;

	Window() {
		frame = new JFrame("The Puzzled Cube");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		currentScreenPanel = new MainScreen(frame);
		testLevelPanel = new LevelScreen(frame);
		testSettingPanel = new SettingScreen(frame);
		// menuBar = new MenuBar(currentScreenPanel);
		// frame.setJMenuBar(menuBar);
		frame.setContentPane(currentScreenPanel);
		frame.pack();
		frame.setVisible(true);
	}

	private static void runGUI() {
		JFrame.setDefaultLookAndFeelDecorated(true);
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