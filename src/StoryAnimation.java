import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

public class StoryAnimation extends JPanel implements ActionListener {
    private JFrame frame;
    private final int INITIAL_X = 0;
    private final int INITIAL_Y = 150;
    private final int DELAY = 20;
    private boolean drawRect = true;
    private boolean question = false;

    private Timer timer;
    private int x, y;

    StoryAnimation(JFrame frame) {
        this.frame = frame;
        frame.setPreferredSize(new Dimension(600, 400));
        this.setBackground(Color.black);
        x = INITIAL_X;
        y = INITIAL_Y;

        timer = new Timer(DELAY, this);
        timer.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        try {
            draw(g);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private void draw(Graphics g) throws MalformedURLException {
        g.setColor(Color.white);
        g.drawLine(INITIAL_X, INITIAL_Y+ 50 , frame.getWidth(), INITIAL_Y + 50);
        g.setColor(Color.red);
        g.fillRect(x, y, 50, 50);
        Toolkit.getDefaultToolkit().sync();
        if(question) {
            g.drawImage(load("res/img/arrow.gif"), 100, 100, this);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(drawRect) {
            x+= 5;
            if(x == frame.getWidth()/2) {
                drawRect = false;
                question = true;
            }
        } else if(question) {

        }


        repaint();
    }

    private Image load(final String url) {
        try {
            final Toolkit tk = Toolkit.getDefaultToolkit();
            final File path = new File(url); // Any URL would work here
            final Image img = tk.createImage(path.toURI().toURL());
            tk.prepareImage(img, -1, -1, null);
            return img;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
