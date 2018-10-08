import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;

public class PaintPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    int x, y;

    Graphics graphics;

    PaintListener paintListener = null;

    public PaintPanel() {
        this(null);
    }

    public PaintPanel(PaintListener pListener) {
        setBackground(Color.WHITE);

        addMouseListener(mouseListener);
        addMouseMotionListener(mouseMotionListener);

        paintListener = pListener;
    }

    MouseListener mouseListener = new MouseListener() {

        @Override
        public void mouseClicked(MouseEvent e) {
        }

        @Override
        public void mousePressed(MouseEvent e) {
            x = e.getX();
            y = e.getY();
            setUp();
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            tearDown();
        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }

    };

    MouseMotionListener mouseMotionListener = new MouseMotionListener() {

        @Override
        public void mouseDragged(MouseEvent e) {
            drawLine(x, y, x = e.getX(), y = e.getY());
        }

        @Override
        public void mouseMoved(MouseEvent e) {
        }

    };

    void drawLine(int x, int y, int nx, int ny) {
        graphics.drawLine(x, y, nx, ny);

        if (paintListener != null)
            paintListener.drawLine(x, y, nx, ny);
    }

    void setUp() {
        graphics = getGraphics();
        graphics.setColor(Color.BLACK);

        if (graphics instanceof Graphics2D) {
            Graphics2D g2 = (Graphics2D) graphics;
            g2.setStroke(new BasicStroke(5));
        }
    }

    void tearDown() {
        graphics.dispose();
    }

}

interface PaintListener {
    void drawLine(int x, int y, int nx, int ny);
}