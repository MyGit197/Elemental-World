/*
 * MenuFrame.java
 * Tạo JFrame chứa menuPanel
 */
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MenuFrame extends JFrame implements ActionListener {
    static public MenuPanel menu = new MenuPanel();
    Timer timer = new Timer(1000/60, this);

    // constructor
    public MenuFrame() {
        super("Elemental World");
        timer = new Timer(1000/60, this);
        timer.start();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(Consts.SCREEN_WIDTH, Consts.SCREEN_HEIGHT);

        add(menu); // thêm Panel

        // Thêm mouse listener vào MenuPanel
        menu.addMouseListener(new MenuMouseListener());

        setIconImage(Consts.windowIcon.getImage()); // Biểu tượng cho cửa sổ Game
    }

    public void actionPerformed(ActionEvent e) {
        // Vẽ lại menu panel trong sự kiện timers
        menu.move();
        menu.repaint();
    }

    public void startMenuSound() { // Chạy nhạc Menu
        Util.startSound( Consts.MenuMusic);
    }

    public void stopMenuSound() { // Dừng nhạc Menu
        Util.stopSound(Consts.MenuMusic);
    }

    class MenuMouseListener extends MouseAdapter {
        public void mouseClicked(MouseEvent e) {
        }
    }
}