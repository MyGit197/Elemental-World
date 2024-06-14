/*
    PauseScreen.java

    Chứa cả Frame va Panel cho màn hình tạm dừng
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


// Tạo Frame
public class PauseScreen extends JFrame implements ActionListener {
    static public PausePanel pausemenu = new PausePanel();
    Timer timer = new Timer(1000/60, this);
    public PauseScreen() {
        super("Elemental World");
        timer.start();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(Consts.SCREEN_WIDTH, Consts.SCREEN_HEIGHT);
        add(pausemenu);
        pausemenu.timer.start();
        setIconImage(Consts.windowIcon.getImage());
        pausemenu.addMouseListener(new MenuMouseListener());
    }
    public void actionPerformed (ActionEvent e) {
        pausemenu.move();
        pausemenu.repaint();
    }
    class MenuMouseListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
        }
    }
}


// Tạo Panel
class PausePanel extends JPanel implements KeyListener, ActionListener, MouseListener, MouseMotionListener {
    Timer timer = new Timer(1000/60, this);
    boolean[] keys = new boolean[KeyEvent.KEY_LAST + 1]; // Bấm/thả phím
    public int playButtonWidth = 200; public int playButtonHeight = 200; // Kích thước nút Resume
    public int buttonWidth = 175; public int buttonHeight = 175; // Kích thước các nút còn lại

    // Hitbox các nút
    public Rectangle playButtonHitbox = new Rectangle((Consts.SCREEN_WIDTH/2) - (playButtonWidth/2), 400, playButtonWidth, playButtonHeight);
    public Rectangle menuButtonHitbox = new Rectangle((Consts.SCREEN_WIDTH / 2) + (playButtonWidth / 2) + 175 - (buttonWidth / 2), 400 + 12, buttonWidth, buttonHeight);
    public Rectangle practiceButtonHitbox = new Rectangle((Consts.SCREEN_WIDTH / 2) - (playButtonWidth / 2) - 175 - (buttonWidth / 2), 400 + 12, buttonWidth, buttonHeight);

    // Ảnh các nút
    public BufferedImage playButtonImg = Util.resize(Util.loadBuffImage("assets/buttons/play.png"), playButtonWidth, playButtonHeight);
    public BufferedImage menuButtonImg = Util.resize(Util.loadBuffImage("assets/buttons/menu.png"), buttonWidth, buttonHeight);
    public BufferedImage playButtonHoverImg = Util.resize(Util.loadBuffImage("assets/buttons/play.png"), playButtonWidth + 60, playButtonHeight + 60);
    public BufferedImage menuButtonHoverImg = Util.resize(Util.loadBuffImage("assets/buttons/menu.png"), buttonWidth + 30, buttonHeight + 30);
    public BufferedImage practiceButtonImg = Util.resize(Util.loadBuffImage("assets/buttons/practice.png"), buttonWidth, buttonHeight);
    public BufferedImage practiceButtonHoverImg = Util.resize(Util.loadBuffImage("assets/buttons/practice.png"), buttonWidth + 30, buttonHeight + 30);
    public BufferedImage unpracticeButtonImg = Util.resize(Util.loadBuffImage("assets/buttons/unpractice.png"), buttonWidth, buttonHeight);
    public BufferedImage unpracticeButtonHoverImg = Util.resize(Util.loadBuffImage("assets/buttons/unpractice.png"), buttonWidth + 30, buttonHeight + 30);


    // Cho biết con trỏ có đang ở trên nút hay không
    public boolean playButtonHover = false;
    public boolean menuButtonHover = false;
    public boolean practiceButtonHover = false;

    // Cho viết chuột có đang giữ hay không
    boolean mousePressed = false;

    // Font
    Font fontScores, lvlNameFont;


    // Constructor
    public PausePanel() {
        super();
        setLayout(null);
        setPreferredSize(new Dimension(Consts.SCREEN_WIDTH, Consts.SCREEN_HEIGHT));
        setFocusable(true);
        requestFocus();
        addKeyListener(this);
        addMouseListener(this);
        addMouseMotionListener(this);
        setFocusable(true);
        addMouseListener(this);
        addMouseMotionListener(this);
        addMouseMotionListener(this);
        timer.start();
        // Load font
        try{
            File fntFile = new File("assets/Fonts/PUSAB.otf");
            fontScores = Font.createFont(Font.TRUETYPE_FONT, fntFile).deriveFont(30f);
            lvlNameFont = Font.createFont(Font.TRUETYPE_FONT, fntFile).deriveFont(50f);
        }
        catch(IOException ex){
            System.out.println(ex);
        }
        catch(FontFormatException ex){
            System.out.println(ex);
        }
    }



    public void paint(Graphics g) {
        super.paint(g);


        // background
        g.setColor(new Color(0, 0, 0, 228));
        g.fillRect(0, 0, Consts.SCREEN_WIDTH, Consts.SCREEN_HEIGHT);

        // Tạo hiệu ứng hover cho các nút
        if (!playButtonHover) {
            g.drawImage(playButtonImg, (Consts.SCREEN_WIDTH / 2) - (playButtonWidth / 2), 400,  null);
        }
        else {
            g.drawImage(playButtonHoverImg, (Consts.SCREEN_WIDTH / 2) - (playButtonWidth / 2) - 15, 400 - 15, null);
        }
        if (!menuButtonHover) {
            g.drawImage(menuButtonImg, (Consts.SCREEN_WIDTH / 2) + (playButtonWidth / 2) + 175 - (buttonWidth / 2), 400 + 12, null);
        }
        else {
            g.drawImage(menuButtonHoverImg, (Consts.SCREEN_WIDTH / 2) + (playButtonWidth / 2) + 175 - (buttonWidth / 2) - 15, 400 + 12 - 15, null);
        }
        if (practiceButtonHover) {
            if (Player.practiceMode) {
                g.drawImage(unpracticeButtonHoverImg, (Consts.SCREEN_WIDTH / 2) - (playButtonWidth / 2) - 175 - (buttonWidth / 2) - 15, 400 + 12 - 15, null);
            }
            else {
                g.drawImage(practiceButtonHoverImg, (Consts.SCREEN_WIDTH / 2) - (playButtonWidth / 2) - 175 - (buttonWidth / 2) - 15, 400 + 12 - 15, null);
            }
        }
        else {
            if (Player.practiceMode) {
                g.drawImage(unpracticeButtonImg, (Consts.SCREEN_WIDTH / 2) - (playButtonWidth / 2) - 175 - (buttonWidth / 2), 400 + 12, null);
            }
            else {
                g.drawImage(practiceButtonImg, (Consts.SCREEN_WIDTH / 2) - (playButtonWidth / 2) - 175 - (buttonWidth / 2), 400 + 12, null);
            }
        }


        // Viết tên level và tiến độ hoàn thành
        g.setColor(Color.WHITE);
        Util.drawCenteredString(g, MenuPanel.lvlNames[MenuPanel.targetLevel], new Rectangle(0, 0, Consts.SCREEN_WIDTH, 80), lvlNameFont);
        int percent = Math.min(((Integer.parseInt(Util.readFile(Consts.scoreFile, MenuPanel.targetLevel)) * 100)/ (Map.mapWidth * 75)), 100);
        g.setColor(new Color(0, 0, 0,  100));
        g.fillRoundRect((Consts.SCREEN_WIDTH/2) - 300, 100, 600, 40, 32, 50);
        g.setColor(Color.GREEN);
        g.fillRoundRect((Consts.SCREEN_WIDTH/2) - 300,100, (600/100) * percent, 40, 32, 50);
        Util.drawCenteredString(g, percent + "%", new Rectangle(0, 82, Consts.SCREEN_WIDTH, 80), fontScores);
    }



    public void move() {
    }

    public void resume() { // quay lại màn chơi
        Main.resumeGame();
    }


    public void exitToMenu() { // trở về màn hình chọn level
        Main.toMainMenu();
    }

    public void restartLevel() { // chơi lại level
        Main.enterGame(MenuPanel.targetLevel);
    }

    public void actionPerformed(ActionEvent e) {
        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }


    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        // bấm Space để quay lại màn chơi, bấm Esc để thoát ra màn hình chọn level
        if (code == KeyEvent.VK_SPACE) {
            resume();
        }
        else if (code == KeyEvent.VK_ESCAPE) {
            Main.toMainMenu();
        }
        keys[code] = true;
    }


    public void keyReleased(KeyEvent e) { // Kiểm tra xem nút được thả chưa
        int code = e.getKeyCode();
        keys[code] = false;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // vị trí con trỏ chuột
        int mouseX = e.getX();
        int mouseY = e.getY();

        if (!mousePressed) {
            mousePressed = true;
            if (playButtonHitbox.contains(mouseX, mouseY)) {
                System.out.println("PLAYBUTTONPRESSED");
                resume();
            }
            else if (menuButtonHitbox.contains(mouseX, mouseY)) {
                exitToMenu();
            }
            else if (practiceButtonHitbox.contains(mouseX, mouseY)) { // nút bật/tắt chế độ luyện tập
                System.out.println("PRESSED");
                if (Player.practiceMode == true) {
                    System.out.println("unpracticing");
                    restartLevel();
                    Player.practiceMode = false;
                } else {
                    Player.practiceMode = true;
                    resume();
                }
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        mousePressed = false;
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }
    @Override
    public void mouseExited(MouseEvent e) {
    }
    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {


        // kiểm tra sự kiện chuột di chuyển, dùng cho Hover
        if (playButtonHitbox.contains(e.getX(), e.getY())) {
            playButtonHover = true;
        }
        else {
            playButtonHover = false;
        }
        if (menuButtonHitbox.contains(e.getX(), e.getY())) {
            menuButtonHover = true;
        }
        else {
            menuButtonHover = false;
        }
        if (practiceButtonHitbox.contains(e.getX(), e.getY())) {
            practiceButtonHover = true;
        }
        else {
            practiceButtonHover = false;
        }
    }


}