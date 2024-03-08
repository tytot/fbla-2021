
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;

public class Window {
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
		
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter() {
		    @Override
		    public void windowClosing(WindowEvent event) {
		    	if (frame.getContentPane() instanceof VictoryScreen) {
		    		VictoryScreen vs = (VictoryScreen) frame.getContentPane();
					class LeaderboardPoster extends SwingWorker<String, Object> {
						@Override
						public String doInBackground() {
							return Connection.postToLeaderboard(vs.getName(), vs.getTimeDecis());
						}
						@Override
						protected void done() {
							try {
								System.out.println(get());
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
					(new LeaderboardPoster()).execute();
		    	}
				System.exit(0);
		    }
		});
	}

	public static void runGUI() {
		new Window();
	}

	public static void main(String[] args) {
		System.setProperty("sun.java2d.uiScale.enabled", "false");

		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				runGUI();
			}
		});
	}
}