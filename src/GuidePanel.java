// Lop nay de tao giao dien huong dan choi game
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class GuidePanel extends JPanel implements KeyListener, ActionListener, MouseListener, MouseMotionListener {

    BufferedImage backgroundImg = Util.loadBuffImage("assets/background/stereoBG.png");
    BufferedImage groundImg = Util.loadBuffImage("assets/ground/ground1.png");
    Image guideImg = new ImageIcon("assets/background/guide.gif").getImage();

    // Phuong thuc khoi tao cua GuidePanel
    public GuidePanel() {
        super();
        setLayout(null);
        setPreferredSize(new Dimension(Consts.SCREEN_WIDTH, Consts.SCREEN_HEIGHT));
        setFocusable(true);
        requestFocus();

        addKeyListener(this);
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    boolean[] keys = new boolean[KeyEvent.KEY_LAST + 1]; // Doi tuong de luu tru cac phim duoc nhan

    Background bg1 = new Background(backgroundImg, groundImg); // Doi tuong Background de ve background

    // Phuong thuc de ve man hinh huong dan choi game
    public void guideDraw(Graphics g) {
        bg1.mainMenuDraw(g);
        g.drawImage(guideImg, 10, 10, Consts.SCREEN_WIDTH - 35, Consts.SCREEN_WIDTH - 600, null);
    }

    // Cac cai dat de lang nghe va xu ly KeyEvent
    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_ESCAPE) {
            Main.toMainMenu();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    // Cac cai dat de lang nghe va xu ly MouseEvent
    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    @Override
    public void mouseDragged(MouseEvent e) {}

    @Override
    public void mouseMoved(MouseEvent e) {}

    // Cai dat phuong thuc actionPerformed tu giao dien ActionListener
    @Override
    public void actionPerformed(ActionEvent e) {}
}
