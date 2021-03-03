import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class TutorialScreen extends JPanel implements ActionListener {
    /**
	 * 
	 */
	private static final long serialVersionUID = -930112817244138244L;
	private JFrame frame;
    private JButton exit;

    TutorialScreen(JFrame frame) {
        this.frame = frame;
        setPreferredSize(new Dimension(33 * Block.SIZE, 24 * Block.SIZE));
        setLayout(null);

        exit = UIFactory.createButton("img/ui/exit.png", "img/ui/exitPressed.png", 25, 15);
        exit.addActionListener(this);
        add(exit);

        ImageIcon instructionsIcon = new ImageIcon(TutorialScreen.class.getResource("img/ui/puzzledCube.png"));
        add(UIFactory.createLabel(instructionsIcon, (Window.DIMENSIONS.width - instructionsIcon.getIconWidth()) / 2, (Window.DIMENSIONS.height - instructionsIcon.getIconHeight()) / 2));
    }

    public void paintComponent(Graphics g) {
        g.drawImage(new PlainTheme().getBackgroundImage(), 0, 0, null);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == exit) {
            frame.setContentPane(new MainScreen(frame));
            SoundEffect.CLICK.play(false);
            frame.repaint();
            frame.revalidate();
        }
    }

//    public static void main(String[] args) {
//        JFrame frame = new JFrame("Tutorial");
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setResizable(false);
//        Dimension d = new Dimension(33, 24);
//        frame.setPreferredSize(new Dimension(d.width * Block.SIZE, d.height * Block.SIZE));
//        frame.setMinimumSize(new Dimension(d.width * Block.SIZE, d.height * Block.SIZE));
//        frame.setMaximumSize(new Dimension(d.width * Block.SIZE, d.height * Block.SIZE));
//        frame.setContentPane(new TutorialScreen(frame));
//        frame.pack();
//        frame.setVisible(true);
//    }
}