import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class LeaderboardScreen extends JPanel {
    private JFrame frame;
    private JPanel titlePanel, leaderboard;
    private JLabel title;
    private ArrayList<JLabel> Names = new ArrayList<JLabel>();
    private ArrayList<Integer> Times = new ArrayList<Integer>();
    private int currentCard = 1;
    private CardLayout cl;
    private GridLayout layout, buttonLayout;
    private static final Font TITLE_FONT = new Font("Courier New", Font.BOLD, 100);

    LeaderboardScreen(JFrame frame) {

        // Setting up Frame and layout
        this.frame = frame;
        this.setBackground(Color.BLACK);
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        title = new JLabel("Leaderboard", SwingConstants.CENTER);
        title.setFont(TITLE_FONT);
        title.setForeground(Color.RED);
        title.setAlignmentX(CENTER_ALIGNMENT);
        add(Box.createVerticalStrut(100));
        this.add(title);
        
        add(Box.createVerticalStrut(100));
        Times.add(100);
        Names.add(new JLabel("#1: Alex   - 09:31.323"));
        Names.add(new JLabel("#2: Tyler  - 11:45.020"));
        Names.add(new JLabel("#3: Aaron  - 13:10.434"));
        Names.add(new JLabel("#4: Aditya - 25:01:919"));
        for (JLabel label : Names) {
        	label.setFont(new Font("Courier New", Font.BOLD, 50));
			label.setForeground(Color.WHITE);
			label.setAlignmentX(CENTER_ALIGNMENT);
			add(label);
        }
    }

}
