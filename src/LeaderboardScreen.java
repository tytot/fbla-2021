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
        frame.setPreferredSize(new Dimension(600, 400));
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        // Setting up the Title
        titlePanel = new JPanel();
        titlePanel.setBackground(Color.BLACK);
        title = new JLabel("Leaderboard", SwingConstants.CENTER);
        title.setFont(TITLE_FONT);
        title.setForeground(Color.RED);
        titlePanel.add(title);
        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.VERTICAL;
        this.add(titlePanel, c);

        //Setting up Leaderboard
        leaderboard = new JPanel();
        leaderboard.setBackground(Color.black);
        leaderboard.setLayout(new BoxLayout(leaderboard, BoxLayout.PAGE_AXIS));
        Times.add(100);
        Names.add(new JLabel("Aditya" + "Time: " + Times.get(0)));
        leaderboard.add(Names.get(0));
        leaderboard.add(Names.get(1));
        leaderboard.add(Names.get(2));
    }

}
