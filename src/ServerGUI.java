import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class ServerGUI extends JFrame {

    private static final long serialVersionUID = 1L;

    private JPanel contentPanel, pPanel;

    public ServerGUI(PaintListener paintListener) {
        setTitle("Server");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 600);
        contentPanel = new JPanel();
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPanel);
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.PAGE_AXIS));

        // paint panel
        pPanel = new PaintPanel(paintListener);
        contentPanel.add(pPanel);
    }

    public void drawLine(int x, int y, int nx, int ny) {
        ((PaintPanel) pPanel).setUp();
        ((PaintPanel) pPanel).drawLine(x, y, nx, ny);
        ((PaintPanel) pPanel).tearDown();
    }

}