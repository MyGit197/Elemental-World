/*
 * MenuPanel.java
 * Giao diện màn hình chính và màn hình chọn Level
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.swing.Timer;
import java.io.*;

public class MenuPanel extends JPanel implements KeyListener, ActionListener, MouseListener, MouseMotionListener {

    public static String screen = "menu"; // Theo dõi dao diện hiện tại của người dùng
    public static int targetLevel = 1; // vẽ và load level

    // Ảnh
    BufferedImage title;
    BufferedImage credit;
    BufferedImage background;
    BufferedImage startButton;
    BufferedImage creditButton;
    BufferedImage guideButton;

    BufferedImage backgroundImg = Util.loadBuffImage("assets/background/stereoBG.png");
    BufferedImage groundImg = Util.loadBuffImage("assets/ground/ground1.png");
    BufferedImage nextButtonImg = Util.loadBuffImage("assets/buttons/rightArrow.png");
    BufferedImage prevButtonImg = Util.loadBuffImage("assets/buttons/leftArrow.png");

    BufferedImage [] levelBackground = new BufferedImage[4];
    BufferedImage backGroundLevel1 = Util.resize(Util.loadBuffImage("assets/background/forest.png"), Consts.SCREEN_WIDTH, Consts.SCREEN_HEIGHT);
    BufferedImage backGroundLevel2 = Util.resize(Util.loadBuffImage("assets/background/water.png"), Consts.SCREEN_WIDTH, Consts.SCREEN_HEIGHT);
    BufferedImage backGroundLevel3 = Util.resize(Util.loadBuffImage("assets/background/fire.png"), Consts.SCREEN_WIDTH, Consts.SCREEN_HEIGHT);

    BufferedImage [] difficultyFaces = new BufferedImage[4];
    BufferedImage easyFace = Util.resize(Util.loadBuffImage("assets/difficultyFaces/easy.png"), 50, 50);
    BufferedImage hardFace = Util.resize(Util.loadBuffImage("assets/difficultyFaces/hard.png"), 50, 50);
    BufferedImage harderFace = Util.resize(Util.loadBuffImage("assets/difficultyFaces/harder.png"), 50, 50);



    boolean[] keys = new boolean[KeyEvent.KEY_LAST + 1]; // Mảng để theo dõi những nút được bấm
    public Font fontLocal, fontSys, fontScores, lvlNameFont; // font chử dùng trong menu

    Background bg1 = new Background(backgroundImg, groundImg); // Dùng để tạo Background
    Timer timer = new Timer(1000/60, this); // timer


    int buttonWidth = 250; int buttonHeight = 250; // Chiều dài/rộng của các nút, nút bé bằng nửa nút to
    int titleWidth = 1150; int titleHeight = 180; // Kích thước của Title
    int switchButtonWidth = 75; int switchButtonHeight = 100; // Kích thước nút phải/trái trong dao diện chọn lv
    int loadButtonWidth = 600; int loadButtonHeight = 300; // Kích thước nút vào màn chơi
    // hitbox của các nút trong giao diện chọn màn chơi
    Rectangle nextButtonHitbox = new Rectangle(Consts.SCREEN_WIDTH - 20 - switchButtonWidth, (Consts.SCREEN_HEIGHT/2) - (switchButtonHeight/2), switchButtonWidth, switchButtonHeight);
    Rectangle prevButtonHitbox = new Rectangle(20, (Consts.SCREEN_HEIGHT/2) - (switchButtonHeight/2), switchButtonWidth, switchButtonHeight);
    Rectangle loadLevelHitbox = new Rectangle((Consts.SCREEN_WIDTH/2) - (loadButtonWidth/2), 100, loadButtonWidth, loadButtonHeight);
    // Cho biết con trỏ có đang ở trên nút hay không
    boolean nextButtonHover = false;
    boolean prevButtonHover = false;
    boolean loadButtonHover = false;
    boolean guideButtonHover = false;
    boolean menuButtonHover = false;
    boolean creditButtonHover = false;
    boolean mouseDown = false; // cho biết chuột có đang được giữ hay không

    public static String [] lvlNames = new String[4]; // Mảng tên level

    // Constructor
    public MenuPanel() {
        super();
        setLayout(null);
        setPreferredSize(new Dimension(Consts.SCREEN_WIDTH, Consts.SCREEN_HEIGHT));
        setFocusable(true);
        requestFocus();

        // Load và chỉnh kích thước ảnh
        title = Util.loadBuffImage("assets/logos/title.png");
        credit = Util.loadBuffImage("assets/logos/credit.png");
        background = Util.loadBuffImage("assets/background/background1.png");
        startButton = Util.resize(Util.loadBuffImage("assets/logos/playButton.png"), buttonWidth, buttonHeight);
        creditButton = Util.resize(Util.loadBuffImage("assets/logos/creditButton.png"), buttonWidth/2, buttonHeight/2);
        guideButton = Util.resize(Util.loadBuffImage("assets/buttons/guide.png"), buttonWidth/2, buttonHeight/2);
        title = Util.resize(title, titleWidth, titleHeight);

        // FONT
        fontSys = new Font("Montserat", Font.PLAIN, 32);
        try{
            File fntFile = new File("assets/Fonts/PUSAB.otf");
            fontLocal = Font.createFont(Font.TRUETYPE_FONT, fntFile).deriveFont(32f);
            fontScores = Font.createFont(Font.TRUETYPE_FONT, fntFile).deriveFont(30f);
            lvlNameFont = Font.createFont(Font.TRUETYPE_FONT, fntFile).deriveFont(50f);
        }
        catch(IOException ex){
            System.out.println(ex);
        }
        catch(FontFormatException ex){
            System.out.println(ex);
        }


        lvlNames[1] = "Forest Frontier"; lvlNames[2] = "Ocean Depth"; lvlNames[3] = "Lava Peak"; // Đặt tên level
        difficultyFaces[1] = easyFace; difficultyFaces[2] = hardFace; difficultyFaces[3] = harderFace; // Đặt biểu tượng độ khó
        levelBackground[1] = backGroundLevel1; levelBackground[2] = backGroundLevel2; levelBackground[3] = backGroundLevel3;
        // Thêm listener
        addKeyListener(this);
        addMouseListener(this);
        addMouseMotionListener(this);
    }
    // Trả về hitbox của các nút
    public Rectangle getButtonHitbox() {
        return new Rectangle(Consts.SCREEN_WIDTH / 2 - (buttonWidth/2), Consts.SCREEN_HEIGHT/2 - (buttonHeight/2), buttonWidth, buttonHeight);
    }
    public Rectangle creditButtonHitbox() {
        return new Rectangle(Consts.SCREEN_WIDTH / 4 - (buttonWidth/4) , Consts.SCREEN_HEIGHT/2 - (buttonHeight/4), buttonWidth/2, buttonHeight/2);
    }
    public Rectangle guideButtonHitbox() {
        return new Rectangle(Consts.SCREEN_WIDTH / 4 * 3 - (buttonWidth/4), Consts.SCREEN_HEIGHT/2 - (buttonHeight/4), buttonWidth/2, buttonHeight/2);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (screen == "menu") { // Nếu người dùng ở giao diện Menu
            mainMenuDraw(g);
        }
        else if (screen == "levelSelect") { // Nếu người dùng ở giao diện chọn màn chơi
            levelSelectDraw(g);
        }
        else if (screen == "guide") {
            guideDraw(g);
        }
        else if (screen == "credit"){
            creditDraw(g);
        }
    }

    // Vẽ màn hình chính
    public void mainMenuDraw(Graphics g) {
        bg1.mainMenuDraw(g); // Tạo background
        g.drawImage(title, (Consts.SCREEN_WIDTH - titleWidth) / 2, 75, null); // draw title

        // Hiệu ứng Hover cho các nút - hiệu ứng khi trỏ chuột vào nút
        if (!menuButtonHover) {
            g.drawImage(startButton, Consts.SCREEN_WIDTH / 2 - (buttonWidth / 2), Consts.SCREEN_HEIGHT / 2 - (buttonHeight / 2), null);
        }
        else {
            g.drawImage(startButton, Consts.SCREEN_WIDTH / 2 - (buttonWidth / 2) - 15, Consts.SCREEN_HEIGHT / 2 - (buttonHeight / 2) - 15, buttonWidth + 30, buttonHeight + 30,null);
        }
        if (!creditButtonHover) {
            g.drawImage(creditButton, Consts.SCREEN_WIDTH / 4 - (buttonWidth / 4 ), Consts.SCREEN_HEIGHT / 2 - (buttonHeight / 4), null);
        }
        else {
            g.drawImage(creditButton, Consts.SCREEN_WIDTH / 4- (buttonWidth / 4 ) - 7, Consts.SCREEN_HEIGHT / 2 - (buttonHeight / 4) - 7, buttonWidth/2+15, buttonHeight/2 + 15,null);
        }
        if (!guideButtonHover) {
            g.drawImage(guideButton, Consts.SCREEN_WIDTH / 4 * 3 - (buttonWidth / 4), Consts.SCREEN_HEIGHT / 2 - (buttonHeight / 4), null);
        }
        else {
            g.drawImage(guideButton, Consts.SCREEN_WIDTH / 4 * 3 - (buttonWidth / 4) - 7, Consts.SCREEN_HEIGHT / 2 - (buttonHeight / 4) - 7, buttonWidth/2+15, buttonHeight/2 + 15,null);
        }
    }
    // Vẽ màn hình credit
    public void creditDraw(Graphics g){
        bg1.mainMenuDraw(g);
        g.drawImage(credit, (Consts.SCREEN_WIDTH - credit.getWidth()) / 2, 60, null);
        int creditButtonWidth = Consts.SCREEN_WIDTH;
        int creditButtonHeight = 70;
        Util.drawCenteredString(g,"Vi Hung Duc - 20224836", new Rectangle(0, 160, creditButtonWidth, creditButtonHeight), lvlNameFont);
        Util.drawCenteredString(g,"Ha Huy Hoang - 20224", new Rectangle(0, 240, creditButtonWidth, creditButtonHeight), lvlNameFont);
        Util.drawCenteredString(g,"Nguyen Thi Thanh Huyen - 20225017", new Rectangle(0, 320, creditButtonWidth, creditButtonHeight), lvlNameFont);
        Util.drawCenteredString(g, "Nguyen Thi Tra My - 20225049", new Rectangle( 0, 400, creditButtonWidth, creditButtonHeight), lvlNameFont);
        Util.drawCenteredString(g,"Nguyen Hoang Phuc - 20225067", new Rectangle(0, 480, creditButtonWidth, creditButtonHeight), lvlNameFont);
    }
    // Vẽ màn hướng dẫn
    public void guideDraw(Graphics g) {
        GuidePanel guide = new GuidePanel();
        guide.guideDraw(g);
    }
    // Vẽ màn hình chọn màn chơi
    public void levelSelectDraw(Graphics g) {
        g.drawImage(levelBackground[targetLevel], 0, 0, null); // Tạo background

        // Hiệu ứng Hover cho các nút
        if (nextButtonHover) {
            g.drawImage(Util.resize(nextButtonImg, switchButtonWidth + 20, switchButtonHeight + 20), nextButtonHitbox.x - 10, nextButtonHitbox.y - 10, null);
        }
        else {
            g.drawImage(Util.resize(nextButtonImg, switchButtonWidth, switchButtonHeight), nextButtonHitbox.x, nextButtonHitbox.y, null);
        }
        if (prevButtonHover) {
            g.drawImage(Util.resize(prevButtonImg, switchButtonWidth + 20, switchButtonHeight + 20), prevButtonHitbox.x - 10, prevButtonHitbox.y - 10, null);
        }
        else {
            g.drawImage(Util.resize(prevButtonImg, switchButtonWidth, switchButtonHeight), prevButtonHitbox.x, prevButtonHitbox.y, null);
        }
        if (loadButtonHover) {
            g.setColor(new Color(0, 0, 0,  100));
            g.fillRoundRect(loadLevelHitbox.x, loadLevelHitbox.y, loadLevelHitbox.width, loadLevelHitbox.height, 50, 30);
        }
        else {
            g.setColor(new Color(0, 0, 0,  50));
            g.fillRoundRect(loadLevelHitbox.x, loadLevelHitbox.y, loadLevelHitbox.width, loadLevelHitbox.height, 50, 30);
        }


        // Tạo tên và biểu tượng độ khó
        Util.drawCenteredString(g, lvlNames[targetLevel], loadLevelHitbox, lvlNameFont);
        g.drawImage(difficultyFaces[targetLevel], (Consts.SCREEN_WIDTH/2) - 25 ,loadLevelHitbox.y + 200, null);


        // Tạo thanh tiến độ màn chơi - Highscore
        int percent = Math.min(((Integer.parseInt(Util.readFile(Consts.scoreFile, targetLevel)) * 100)/ (Map.mapWidth * 75)) , 100);
        g.setColor(new Color(0, 0, 0,  100));
        g.fillRoundRect((Consts.SCREEN_WIDTH/2) - 300, Consts.SCREEN_HEIGHT - 272, 600, 40, 32, 50);
        g.setColor(Color.GREEN);
        g.fillRoundRect((Consts.SCREEN_WIDTH/2) - 300, Consts.SCREEN_HEIGHT - 272, (600/100) * percent, 40, 32, 50);
        Util.drawCenteredString(g, percent + "%", new Rectangle(0, Consts.SCREEN_HEIGHT - 325, Consts.SCREEN_WIDTH, 150), fontScores);
    }


    public void move() {
        bg1.move();
    }

    // Các cài đặt để lắng nghe và xử lý keyevent
    public void keyPressed(KeyEvent e) {
        // Xử lí sự kiện bấm phím
        int code = e.getKeyCode();
        if (screen == "menu") { // Khi đang ở Menu
            if (code == KeyEvent.VK_SPACE || code == KeyEvent.VK_ENTER) { // Bấm Space hoặc Enter để vào dao diện chọn level
                screen = "levelSelect";
            }
        }
        else if (screen == "levelSelect") { // Khi đang chọn màn chơi
            if (code == KeyEvent.VK_ESCAPE) { // Bấm Esc để quay về menu
                screen = "menu";
            }
            else if (code == KeyEvent.VK_SPACE || code == KeyEvent.VK_ENTER) { // Bấm Space hoặc Enter để vào level
                Main.enterGame(targetLevel);
            }
        }
        else if (screen == "guide") {
            if (code == KeyEvent.VK_ESCAPE) { // Bấm Esc để quay về menu
                screen = "menu";
            }
            else if (code == KeyEvent.VK_ENTER) { // Bấm Enter để vào level
                screen = "levelSelect";
            }
        }
        else if (screen == "credit") { // Khi đăng ở Credit
            if (code == KeyEvent.VK_ESCAPE) { // Bấm Esc để quay về menu
                screen = "menu";
            }
        }

        keys[code] = true;
    }

    public void keyReleased(KeyEvent e) {
        // Xử lí sự kiện thả chuột

        int code = e.getKeyCode();
        keys[code] = false;
    }

    public void keyTyped(KeyEvent e) {
    }

    public void actionPerformed(ActionEvent e) {
    }

    //Các cài đặt để lắng nghe và xử lý mouseevent
    public void mouseClicked(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
        // Xử lí sự kiện bấm chuột
        int mouseX = e.getX();
        int mouseY = e.getY();

        // Nếu bấm chuột trong hitbox của nút
        if (!mouseDown) {
            if (screen == "menu") {
                if (getButtonHitbox().contains(mouseX, mouseY)) {
                    screen = "levelSelect";
                }
                if (creditButtonHitbox().contains(mouseX, mouseY)) {
                    screen = "credit";
                }
                if (guideButtonHitbox().contains(mouseX, mouseY)) {
                    screen = "guide";
                }
            }

            else if (screen == "levelSelect") {
                // Hiện các level
                if (loadLevelHitbox.contains(mouseX, mouseY)) {
                    Main.enterGame(targetLevel);
                }
                else if (nextButtonHitbox.contains(mouseX, mouseY)) {
                    targetLevel++;
                }
                else if (prevButtonHitbox.contains(mouseX, mouseY)) {
                    targetLevel--;
                }

                targetLevel = (targetLevel +2 ) % 3 + 1;
            }
        }

        mouseDown = true;
    }

    public void mouseMoved(MouseEvent e) {
        // Xử lí sự kiện chuột di chuyển
        int mouseX = e.getX();
        int mouseY = e.getY();

        // Nếu con trỏ chuột đang ở trên nút
        if (screen == "menu") {
            if (getButtonHitbox().contains(mouseX, mouseY)) {
                menuButtonHover = true;
            } else {
                menuButtonHover = false;
            }
            if (creditButtonHitbox().contains(mouseX, mouseY)) {
                creditButtonHover = true;
            } else {
                creditButtonHover = false;
            }
            if (guideButtonHitbox().contains(mouseX, mouseY)) {
                guideButtonHover = true;
            } else {
                guideButtonHover = false;
            }
        }
        else if (screen == "levelSelect") {
            if (nextButtonHitbox.contains(mouseX, mouseY)) {
                nextButtonHover = true;
            }
            else {
                nextButtonHover = false;
            }
            if (prevButtonHitbox.contains(mouseX, mouseY)) {
                prevButtonHover = true;
            }
            else {
                prevButtonHover = false;
            }
            if (loadLevelHitbox.contains(mouseX, mouseY)) {
                loadButtonHover = true;
            }
            else {
                loadButtonHover = false;
            }
        }
    }

    public void mouseReleased(MouseEvent e) {
        //xử lí sự kiện thả chuột
        mouseDown = false;
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseDragged(MouseEvent e) {
    }

}