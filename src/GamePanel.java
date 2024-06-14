//GamePanel.java
// Lop nay tao ra GamePanel, dieu khien hanh dong va hien thi cac doi tuong game

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import java.awt.image.BufferedImage;


class GamePanel extends JPanel implements KeyListener, ActionListener, MouseListener, MouseMotionListener {
    Timer timer = new Timer(1000/60, this);
    static Player player;
    Background bg = new Background(Util.loadBuffImage("assets/background/stereoBG.png"), Util.loadBuffImage("assets/ground/ground1.png"));
    static String lvl1map;
    static Map lvl;


    //ArrayList cua tung loai doi tuong va hieu ung hinh anh
    ArrayList<Solid> lvlSolids = new ArrayList<Solid>();
    ArrayList <Spike> lvlSpikes = new ArrayList<Spike>();
    ArrayList <Portal> lvlPortals = new ArrayList<Portal>();
    ArrayList <Orb> lvlOrbs = new ArrayList<Orb>();
    ArrayList <Checkpoint> checkPoints = new ArrayList<Checkpoint>();
    ArrayList <Pad> lvlPads = new ArrayList<Pad>();
    ArrayList <Effect> playerEffects = new ArrayList<Effect>();
    ArrayList<ArrayList<Effect>> padParticles = new ArrayList<ArrayList<Effect>>();
    ArrayList<ArrayList<Effect>> portalParticles = new ArrayList<ArrayList<Effect>>();
    ArrayList <Effect> shipEffects = new ArrayList<Effect>();

    // Cac gia tri danh cho viec hien thi cac doi tuong
    public double stationaryX = 300;
    private static int offsetX = 0;
    private static int offsetY = 0;
    static boolean mouseDown = false; // Cho biet chuot co duoc nhan khong
    boolean[] keys = new boolean[KeyEvent.KEY_LAST + 1]; // Mang luu tru cac phim duoc nhan

    // Phuong thuc khoi tao cua GamePanel
    public GamePanel( String mapString, String soundTrack) {
        setFocusable(true);
        addKeyListener(this);
        addMouseListener(this);
        requestFocus();
        mapReload(mapString, soundTrack);
    }

    // Cai dat phuong thuc actionPerformed tu giao dien ActionListener
    public void actionPerformed(ActionEvent e) {
        move();
        create();
        destroy();
        repaint();

    }

    // Phuong thuc nay de tai lai map khi vao game hoac chuyen man choi
    public void mapReload(String mapString, String soundTrack) {

        lvl = new Map(mapString);

        double stationaryX = 300;
        player = new Player(stationaryX, Consts.floor- Consts.solidHeight, 75, 75);
        lvlSolids = new ArrayList<Solid>();
        lvlSpikes = new ArrayList<Spike>();
        lvlPortals = new ArrayList<Portal>();
        lvlOrbs = new ArrayList<Orb>();
        checkPoints = new ArrayList<Checkpoint>();
        lvlPads = new ArrayList<Pad>();
        playerEffects = new ArrayList<Effect>();
        padParticles = new ArrayList<ArrayList<Effect>>();
        portalParticles = new ArrayList<ArrayList<Effect>>();
        shipEffects = new ArrayList<Effect>();



        lvl1map = mapString;
        lvlSolids = lvl.getSolids();
        lvlSpikes = lvl.getSpikes();
        lvlPortals = lvl.getPortals();
        lvlPads = lvl.getPads();
        lvlOrbs = lvl.getOrbs();


        if (!lvlPads.isEmpty()) {
            for (Pad p: lvlPads) {
                padParticles.add(new ArrayList<Effect>());
            }
        }

        if (!lvlPortals.isEmpty()) {
            for (Portal p: lvlPortals) {
                portalParticles.add(new ArrayList<Effect>());
            }
        }

    }

    // Phuong thuc giup Player di chuyen
    public void move() {

        if (player.deathTimeCounter > 0) {
            player.deathTimeCounter -- ;
            if(player.deathTimeCounter == 0) {
                player.restart();
            }
            return;
        }

        // Di chuyen background va cac chuong ngai vat
        bg.move();

        // Di chuyen Player
        player.move(lvlSolids, lvlSpikes, lvlPortals, lvlPads, lvlOrbs);

        // Chuyen dong nhay cua Player
        if(mouseDown) {
            player.cubeJump();
        }

        // Di chuyen hieu ung chay cua Player va Ship
        if (!playerEffects.isEmpty()) {
            for (Effect s: playerEffects) {
                s.move();
            }
        }

        if (! shipEffects.isEmpty()) {
            for (Effect s: shipEffects) {
                s.move();
            }
        }

        for (int i = 0; i < padParticles.size(); i++) {
            ArrayList<Effect> lis = padParticles.get(i);
            for (int j = lis.size()-1; j>=0; j--) {
                Effect s = lis.get(j);
                s.move();
            }
        }

        for (int i = 0; i < portalParticles.size(); i++) {
            ArrayList<Effect> lis = portalParticles.get(i);
            for (int j = lis.size()-1; j>=0; j--) {
                Effect s = lis.get(j);
                s.move();
            }
        }

    }

    // Phuong thuc de tao ra hieu ung hinh anh
    public void create() {
        Random rand = new Random();
        double min = 0;
        double max = 3 *Math.PI /2;
        if (playerEffects.size() < 100) {
            if( player.getGamemode().equals("cube") && player.onSurface == true) {
                int l = rand.nextInt(4) + 4;
                if ( !player.reverse) {
                    playerEffects.add(new Effect(player.getX(), player.getY() + player.getHeight() - 5 + rand.nextInt(5), Math.random() * (-Math.PI), l, l, 0.2 * player.getVX(), 20));
                }
                else if ( player.reverse) {
                    playerEffects.add(new Effect(player.getX(), player.getY() - 5 + rand.nextInt(5), Math.random() * (Math.PI), l, l, 0.2 * player.getVX(), 20));
                }
            }

            if (player.getGamemode().equals("ship")) {
                int l = rand.nextInt(3) + 4;
                playerEffects.add ( new Effect( player.getX(), rand.nextInt(player.getWidth()) + player.getY(), min + Math.random() * (max - min) , l, l,-2, 50));
            }
        }

        if ( player.getGamemode().equals("ship")) {  // the particles that float around the screen when player is in shiop mode
            if (shipEffects.size() < 800) {
                int l= rand.nextInt(7) + 4;
                shipEffects.add(new Effect(rand.nextInt(1000) + player.getX() - 200, rand.nextInt(1000) + player.getY() - 400, min + Math.random() * (max - min) ,l,l,-2, 100 ));
            }
        }

        for (int i = 0; i<padParticles.size(); i++) {
            ArrayList lis = padParticles.get(i);
            if(lis.size() < 700){
                int l = rand.nextInt(7) + 4;
                lis.add(new Effect(lvlPads.get(i).getX()+rand.nextInt(Consts.solidWidth), lvlPads.get(i).getY() + lvlPads.get(i).getHeight(), Math.PI/2, l, l,-10, rand.nextInt(50) + 70 ));
            }
        }


        // Dinh huong va tao cac phan cua Portal bang song sin
        for (int i = 0; i<portalParticles.size(); i++) {
            ArrayList lis = portalParticles.get(i);
            if(lis.size() < 700){
                int l = rand.nextInt(10) + 4;
                int portalY = lvlPortals.get(i).getY();
                int portalH = lvlPortals.get(i).getHeight();
                int portalX = lvlPortals.get(i).getX();
                int py = portalY - 20 + rand.nextInt(portalH + 20);
                int midy = (2* portalY + portalH ) /2;
                lis.add(new Effect(portalX-rand.nextInt(100), py,  Math.asin((double) 2* ((midy) - py) / portalH ) , l, l,5, rand.nextInt(50) + 60 ));

            }
        }


    }

    //Xoa hieu ung khi da di chuyen den khoang cach cuc dai
    public void destroy() {
        for (int i = playerEffects.size() - 1; i>=0; i--) {
            Effect s = playerEffects.get(i);
            if (Math.pow(s.x - s.startX, 2) + Math.pow(s.y-s.startY, 2) > Math.pow(s.maxdist, 2)) {
                playerEffects.remove(i);
            }
        }
        for (int i = shipEffects.size() - 1; i>=0; i--) {
            Effect s = shipEffects.get(i);
            if (Math.pow(s.x - s.startX, 2) + Math.pow(s.y-s.startY, 2) > Math.pow(s.maxdist, 2)) {
                shipEffects.remove(i);
            }
        }

        for (int i = 0; i<padParticles.size(); i++) {
            ArrayList<Effect> lis = padParticles.get(i);
            for (int j = lis.size()-1; j>=0; j--) {
                Effect s = lis.get(j);
                if (Math.pow(s.x - s.startX, 2) + Math.pow(s.y-s.startY, 2) > Math.pow(s.maxdist, 2)) {
                    lis.remove(j);
                }
            }
        }

        for (int i = 0; i<portalParticles.size(); i++) {
            ArrayList<Effect> lis = portalParticles.get(i);
            for (int j = lis.size()-1; j>=0; j--) {
                Effect s = lis.get(j);
                if (Math.pow(s.x - s.startX, 2) + Math.pow(s.y-s.startY, 2) > Math.pow(s.maxdist, 2)) {
                    lis.remove(j);
                }
            }
        }

    }



    // Reset va dua Player ve vi tri bat dau
    public static void resetPlayer() {
        player.setGamemode("cube");
        player.upright();
        player.initY = -41.55;
        player.setY(Consts.floor - player.getHeight());
        player.setVY(0);
        player.reverse = false;
        player.setX((int) player.constantX);
        player.onSurface = true;
        Player.practiceMode = false;
        Map.checkpoints.clear();
    }

    // Set Player ve che do practice
    public static void setPlayerPracticemode(boolean b) {
        player.practiceMode = b;
    }

    // Cho biet Player co dang o che do practice khong
    public static boolean getPlayerPracticemode() {
        return player.practiceMode;
    }

    // Phuong thuc de ve va hien thi
    @Override
    public void paint(Graphics g) {
        super.paint(g);

        Graphics2D g2d = (Graphics2D) (g);


        Graphics ground = (Graphics2D) (g);
        ground.setColor(Color.WHITE);

        Graphics debug = (Graphics2D) (g);
        debug.setColor(Color.RED);
        debug.fillRect((int) 300, Consts.floor, 1, 100);

        offsetX = (int) (stationaryX - player.getX());
        int adj = 250;

        bg.draw(g2d, offsetY);
        if (player.reverse) {
            adj += 200;
        }

        if (player.getOffsetY() > offsetY + adj) {
            offsetY += 5;
        }
        if (player.getOffsetY() < offsetY + adj) {
            offsetY -= 5;
        }

        int playerOSY = offsetY;

        player.draw(g2d, playerOSY);

        for (Solid s : lvlSolids) {
            s.draw(g2d, offsetX, offsetY, player);
        }
        for (Spike s : lvlSpikes) {
            s.draw(g2d, offsetX, offsetY, player);
        }
        for (Portal p : lvlPortals) {
            p.draw(g2d, offsetX, offsetY);
        }

        if (!lvlPads.isEmpty()) {
            for (Pad p : lvlPads) {
                p.draw(g, offsetX, offsetY);
            }
        }

        if (!lvlOrbs.isEmpty()) {
            for (Orb o : lvlOrbs) {
                o.draw(g, offsetX, offsetY);
            }
        }

        for (Checkpoint c : Map.checkpoints) {
            c.draw(g2d, offsetX, offsetY);
        }

        for (int i = 0; i < playerEffects.size(); i++) {
            Effect s = playerEffects.get(i);
            s.draw(g2d, offsetX, offsetY);
        }
        for (int i = 0; i < shipEffects.size(); i++) {
            Effect s = shipEffects.get(i);
            s.draw(g2d, offsetX, offsetY);
        }

        for (int i = 0; i < padParticles.size(); i++) {
            if (!Util.onScreen(player, lvlPads.get(i).getX())) {
                continue;
            }
            ArrayList<Effect> lis = padParticles.get(i);
            for (int j = lis.size() - 1; j >= 0; j--) {
                Effect s = lis.get(j);
                s.draw(g2d, offsetX, offsetY);
            }
        }

        for (int i = 0; i < portalParticles.size(); i++) {
            if (!Util.onScreen(player, lvlPortals.get(i).getX())) {
                continue;
            }
            ArrayList<Effect> lis = portalParticles.get(i);
            for (int j = lis.size() - 1; j >= 0; j--) {
                Effect s = lis.get(j);
                s.draw(g2d, offsetX, offsetY);
            }
        }
    }

    // Cac cai dat de lang nghe va xu ly MouseEvent
    public void mousePressed(MouseEvent e) {
        mouseDown = true;
        Point mouse = MouseInfo.getPointerInfo().getLocation();
        Point offset = getLocationOnScreen();
    }

    public void mouseReleased(MouseEvent e) {
        mouseDown = false;
    }
    public void mouseMoved(MouseEvent e) {}
    public void mouseDragged(MouseEvent e) {}
    public void mouseClicked(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}

    // Cac cai dat de lang nghe va xu ly KeyEvent
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        if (code == KeyEvent.VK_SPACE || code == KeyEvent.VK_UP) { // Nhan Space hoac Up tuong duong voi nhay chuot
            mouseDown = true;
        }

        if (code == KeyEvent.VK_Z) { // Nhan Z de dat check point trong che do practice
            if (keys[code] == false) {
                if (player.practiceMode) {
                    boolean b = false;
                    if(player.reverse) {
                        b = true;
                    }
                    Map.checkpoints.add(new Checkpoint(player.getX(), player.getY(), player.getVX(), player.getVY(),player.g, player.initY, player.shipG, player.shipLift, player.getGamemode(), b));
                }
            }
        }

        if (code == KeyEvent.VK_X) {         // Nhan X de xoa check point
            if (keys[code] == false){
                if (player.practiceMode) {
                    if (!Map.checkpoints.isEmpty()) {
                        Map.checkpoints.remove(Map.checkpoints.size() - 1);
                    }
                }
            }
        }

        if (code == KeyEvent.VK_ESCAPE) { // Nhan Escape
            if (keys[code] == false) {
                if (player.win) { // Neu da chien thang thi quay ve MainMenu
                    Main.toMainMenu();
                }
                else {
                    Main.pauseGame(); // Neu khong thi chuyen sang giao dien dung game
                }
            }
        }

        keys[code] = true;
    }

    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();

        if (code == KeyEvent.VK_SPACE || code == KeyEvent.VK_UP) {
            mouseDown = false;
        }
        keys[code] = false;

    }


    public void keyTyped(KeyEvent e) {}


}