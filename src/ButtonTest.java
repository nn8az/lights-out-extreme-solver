import java.awt.Color;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class ButtonTest extends JPanel implements ActionListener {

    private static final int N = 4;
    private static final Random rnd = new Random();
    private final Timer timer = new Timer(1000, this);
    private final List<ButtonPanel> panels = new ArrayList<ButtonPanel>();

    public ButtonTest() {
        this.setLayout(new GridLayout(N, N, N, N));
        for (int i = 0; i < N * N; i++) {
            ButtonPanel bp = new ButtonPanel(i);
            bp.setOpaque(true);
            bp.setBackground(Color.WHITE);
            panels.add(bp);
            this.add(bp);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        for (JPanel p : panels) {
            p.setBackground(new Color(rnd.nextInt()));
        }
    }

    private static class ButtonPanel extends JPanel {

        public ButtonPanel(int i) {
            this.setBackground(new Color(rnd.nextInt()));
            this.add(new JButton("Button " + String.valueOf(i)));
        }
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                JFrame f = new JFrame("ButtonTest");
                f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                ButtonTest bt = new ButtonTest();
                f.add(bt);
                f.pack();
                f.setLocationRelativeTo(null);
                f.setVisible(true);
                bt.timer.start();
            }
        });
    }
}