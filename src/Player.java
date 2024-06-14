// Player.java
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/*
Class xử lí các sự kiện khí khi nhân vật tương tác với các đối tượng trên map
 */

public class Player{

    //hiệu ứng
    Image deathAnimation = new ImageIcon("assets/deathEffects/deathEffect.gif").getImage();
    private final BufferedImage shipIcon;
    private final BufferedImage ufoIcon;
    private final BufferedImage icon;


    private double x, y;
    public double constantX;
    private double px, py;
    private int width, height;
    private int groundLevel;
    private int offsetY = 0;
    public int deathTimeCounter = 0;


    // vector chuyển động của nhân vật
    public double g = 5.19; //trọng luc
    private double vy = 0;
    private double vx = 22;
    public double initY = -41.55;
    public double shipG = 1.2;
    public double shipLift = -2.008 * shipG;


    // chuyển động xoay tròn
    private double angle = 0;
    private double jumpRotate = (double) ( -Math.PI * g ) / ( 2 * initY ); // add to angle when jump
    public boolean reverse = false;


    //trạng thái nhanh vật : gamemode, trang thai chuyen dong
    private String gamemode;
    public boolean changeYdirection = false;
    public boolean onSurface = true;
    public boolean prevOnSurface = true;
    public boolean onCeiling = false;
    public boolean orbActivate = false;


    //Gamemode/Practice
    public static boolean practiceMode;
    private boolean debugDead = false; // for debugging.
    public boolean win = false;//win

    // Font
    Font titleFont;

    //Khởi tạo Player
    public Player(double x, double y, int width, int height) {
        this.gamemode = "cube";

        this.y = y;
        this.x = x;
        this.constantX = x;

        this.width  = width;
        this.height = height;

        this.groundLevel = (int) y + width;
        practiceMode = false;
        this.icon = Util.resize( Util.loadBuffImage("assets/icons/Cube001.png" ), width, height);
        this.shipIcon = Util.resize( Util.loadBuffImage("assets/icons/Ship001.png" ), width, height);
        this.ufoIcon = Util.resize( Util.loadBuffImage("assets/icons/UFO001.png" ), width, height);

        try{
            File fntFile = new File("assets/Fonts/PUSAB.otf");

            titleFont = Font.createFont(Font.TRUETYPE_FONT, fntFile).deriveFont(90f);
        }
        catch(IOException ex){
            System.out.println(ex);
        }
        catch(FontFormatException ex){
            System.out.println(ex);
        }

    }


    public void move(ArrayList<Solid> solids, ArrayList<Spike> spikes, ArrayList<Portal> portals, ArrayList<Pad> pads, ArrayList<Orb> orbs) {

        if (debugDead) {
            return;
        }

        //Player thắng khi đi tới cuối map

        if (x >= (Map.mapWidth*75)) {
            win = true;

            // Cập nhật điểm

            if (!practiceMode) {
                String s;
                int score = (int) x;
                if (MenuPanel.targetLevel == 1) {
                    Consts.lvl1TopScore = score;
                    s = Integer.toString(score) + "\n" + Integer.toString(Consts.lvl2TopScore) + "\n" + Integer.toString(Consts.lvl3TopScore);
                    Util.writeFile(Consts.scoreFile, s);
                } else if (MenuPanel.targetLevel == 2) {
                    Consts.lvl2TopScore = score;
                    s = Integer.toString(Consts.lvl1TopScore) + "\n" + Integer.toString(score) + "\n" + Integer.toString(Consts.lvl3TopScore);
                    Util.writeFile(Consts.scoreFile, s);
                } else if (MenuPanel.targetLevel == 3) {
                    Consts.lvl3TopScore = score;
                    s = Integer.toString(Consts.lvl1TopScore) + "\n" + Integer.toString(Consts.lvl2TopScore) + "\n" + Integer.toString(score);
                    Util.writeFile(Consts.scoreFile, s);
                }
            }

            return; // ngừng di chuyên
        }

        //chuyển động của Player
        gamemodeMovement();

        //tính offset để vẽ các đối tượng khác
        adjustOffset();


//        rotation adjustment: if player's rotation is not a multiple of pi/2
        if(onSurface || onCeiling){
            angleAdjust();
        }

        //set surface thanh false sau do check xem nhan vat co tren be mat khong
        onSurface = false;

        collide(solids, spikes, portals);


        onCeiling = ceilingCheck();
        prevOnSurface = onSurface;

        //check if player is on the ground;
        groundCheck();

        //dao nguoc Y vector neu changYdirection là true
        if(changeYdirection) {
            upsideDown();
            changeYdirection = false;
        }

        // va chạm với Pad
        if (!pads.isEmpty()) {
            for (Pad p: pads) {
                collidePad(p);
            }
        }

        //if player in air and mouse is pressed, then player can jump on orbs
        //neu nhan vat dang lo lung và nhay chuot phai thi nhan vat có the nhay tren orbs
        if (!onSurface && !GamePanel.mouseDown) {
            orbActivate = true;
        }

        //orb bi vo hieu khi nhan vat ở tren be mat nao do
        if(onSurface) {
            orbActivate = false;
        }

        if (!orbs.isEmpty()) {
            for (Orb o : orbs) {
                collideOrb(o);
            }
        }


    }

    public void gamemodeMovement(){

        //cube gamemode xoay mot luong xac dinh khi nhay
        //tang trong lực
        if(gamemode.equals ("cube") ) {
            jumpRotate = (double) ( -Math.PI * g ) / ( 2 * initY );
            vy += g;
            if(!onSurface) {     //cube rotation
                angle += jumpRotate;
            }
        }

        //ship movement
        if(gamemode.equals( "ship" ) ) {

            vy += shipG ;

            if (GamePanel.mouseDown ) {        //tang vy khi nhay chuot
                vy += shipLift;
            }

            //goc quay của ship được gioi han trong khoang [ -pi/4, pi/4]
            if ( vy >= Math.tan( Math.PI / 4) * vx ) {
                angle = Math.PI /4 ;
            }
            else if ( vy <= Math.tan( - Math.PI / 4) * vx ) {
                angle = -Math.PI / 4;
            }
            else if(!onSurface || !onCeiling){
                angle = 0.9 * Math.atan2( vy , vx );
            }

        }

    }


    // offsetY : su dụng de ve cac doi tuong khc tren ban do trong khung hinh
    public void adjustOffset(){
        int newOffsetY = Consts.floor - groundLevel;
       //cu khi y thay doi 120 don vi ve lại ma hinh 1 lan
        if ( Math.abs(newOffsetY - offsetY) > 120 && !gamemode.equals("ship") ) {
            offsetY = Consts.floor - groundLevel;
        }
        else if ( vy > 0 && Math.abs(py - y) > 38) {
            offsetY = Consts.floor - groundLevel - 30;
        }

        //ở ship mode, thay doi offsetY
        if (gamemode.equals("ship")) {
            offsetY = 220;
        }

        if( y + offsetY > Consts.SCREEN_HEIGHT ) {
            offsetY -= 100;
        }
        else if (y + offsetY < 0 ) {
            offsetY += 100;
        }
    }

    //kiem tra xem nhan vat co ở tren mat dat khong (neu khong ở che do đảo ngược).
    public void groundCheck() {
        if(reverse){
            return;
        }
        if (y + width > Consts.floor) {
            y = Consts.floor - width;
            vy = 0;
            groundLevel = Consts.floor ;
            onSurface = true;
        }
    }

    //kien tra xem nha vat cham tran khong trong ship mode và reverse mode
    public boolean ceilingCheck() {
        if(  gamemode.equals("ship") && y < Consts.SHIP_CEILING) {
            y = Consts.SHIP_CEILING;
            vy = 0;
            return true;
        }

        else if (reverse && y< Consts.SHIP_CEILING) {
            y = Consts.SHIP_CEILING;
            vy = 0;
            return true;
        }
        return false;
    }


    //tuy chinh goc khi nhan vat va cham vơi san hoac tran
    public void angleAdjust() {
        int floorR = (int) Math.floor( (angle / (Math.PI /2 )) );
        int ceilR = floorR + 1;
        double incre = 0.3;
        //thiet lap goc quay nha vat gan nhat voi boi cua pi/2
        if ( gamemode == "cube") {
            if (angle % (Math.PI / 2) != 0) {
                angle += incre;
            }

            if (angle > (ceilR) * (Math.PI / 2)) {
                angle = (ceilR) * (Math.PI / 2);
            }

        }

        else if (gamemode == "ship") {
            incre *= -1;
            if (angle % (Math.PI / 2) != 0) {
                angle += incre;
            }

            if (angle < (floorR) * (Math.PI / 2)) {
                angle = (floorR) * (Math.PI / 2);
            }

        }
        angle = angle % (2 * Math.PI);
    }

    public void collide(ArrayList<Solid> solids, ArrayList<Spike> spikes, ArrayList<Portal> portals) {
        //va cham thong thuong voi solid, spikes and portals
        for (int i=0; i<Math.abs(vy); i++) {
            py = y;
            if (vy < 0) {
                y -= 1;
            }
            else {
                y += 1;
            }
            for (Solid s : solids) {
                collideSolid(s);
            }
        }

        for (int j=0; j<vx; j++) {
            x += 1;
            for (Solid s : solids) {
                collideSolid(s);
            }
        }


        for (Spike s : spikes) {
            collideSpike(s);
        }
        for (Portal p : portals) {
            collidePortal(p);
        }
    }

//đảo nguoc Y vector
    public void upsideDown() {
        reverse = true;
        g = -5.19; //trọng lực
        initY = 41.55;
        shipG = -1.2;
        shipLift = -2.008 * shipG;
        jumpRotate = (double) ( Math.PI * g ) / ( 2 * initY );
    }

    //thiet lap lai Y vector
    public void upright() {
        reverse = false;
        g = 5.19;
        initY = -41.55;
        shipG = 1.2;
        shipLift = -2.008 * shipG;
        jumpRotate = (double) ( -Math.PI * g ) / ( 2 * initY );
    }

    //
    public void collideSolid( Solid solid) {
        //check xem Player va chạm vào phía nào của khối Solid

        Rectangle sHitbox = solid.getRect();

        Rectangle bottom = new Rectangle( (int) solid.getX(), (int) solid.getY() + solid.getHeight() - 1, solid.getWidth(), 1 );
        Rectangle top = new Rectangle((int)solid.getX(),(int) solid.getY(), solid.getWidth(), 1);
        Rectangle side = new Rectangle ( (int) solid.getX(), (int) solid.getY(), 1, solid.getHeight());
        boolean collideUp = false;
        boolean collideDown = false;
        boolean collideX = false;


        if( sHitbox.intersects(getHitbox())) {

            if (getHitbox().intersects(top) && (Math.min(solid.getX() + solid.getWidth(), x + width) - Math.max(x, solid.getX()) >= y + height - solid.getY() -10)) {
                collideUp = true;
            }

            if(getHitbox().intersects(bottom) && (Math.min(solid.getX() + solid.getWidth(), x + width) - Math.max(x, solid.getX()) >= solid.getY() + solid.getHeight() - y -10)) {
                collideDown = true;
            }

            if( side.intersects(getHitbox())) {
                collideX = true;
            }
        }

        //va chạm với solid  ở gamemode cube
        if (gamemode == "cube" ) {
            if(collideUp && collideDown) {
                dies();
            }
            else if (collideUp && !reverse) {
                vy = 0;
                y = solid.getY() - height;
                groundLevel = (int) solid.getY();
                onSurface = true;
            }
            else if( collideDown && reverse ) {
                vy = 0;
                y = solid.getY() + solid.getHeight();
                groundLevel = (int) solid.getY() + solid.getHeight();
                onSurface = true;
            }

            else if (!reverse && (collideDown || collideX)) {
                dies();
            }
            else if (reverse && collideUp ) {
                dies();
            }
            else if (collideX) {
                dies();
            }



        }

        //va chạm với solid ở game mode ship
        else {
            if (collideUp) {
                vy = 0;
                y = solid.getY() - height;
                groundLevel = (int) solid.getY();
                onSurface = true;
            }
            else if (collideDown) {
                vy = 0;
            }
            else if (collideX) {
                dies();
            }

        }
    }


    //va chạm với pad player nhảy cao hơn
    public void collidePad( Pad pad) {
        Rectangle padHitbox = pad.getRect();
        if (getHitbox().intersects(padHitbox) ) {
            vy = -55;
            onSurface = false;
        }
    }

    //va chạm với orb
    public void collideOrb( Orb o) {
        Rectangle orbHitbox = o.getHitbox();
        if (getHitbox().intersects(orbHitbox) && orbActivate && GamePanel.mouseDown) {
            vy = -40;
            onSurface = false;
        }
    }

    //va chạm với spikes Player dies với bất cứ gamemode nào
    public void collideSpike(Spike spike) {
        Rectangle spikeHitbox = spike.getHitbox();
        if (getHitbox().intersects(spikeHitbox)) {
            dies();
        }
    }

 //   Cổng chuyển gamemode của player theo loại cổng : có 2 loại cổng
    public void collidePortal(Portal portal) {
        Rectangle portalHitbox = portal.getRect();
        if (getHitbox().intersects(portalHitbox)) {
            if( portal.getType() == "cube") {
                gamemode = "cube";
            }
            else if (portal.getType() == "ship") {
                gamemode = "ship";
            }
            else if ( portal.getType() == "reverse"){
               upsideDown();
            }
            else if (portal.getType() == "upright") {
                upright();
            }

        }

    }

    // Khi Player chết chơi lai tu dau họac từ checkpoint
    public void dies() {
            GameFrame.stopGameSound();
            if(deathTimeCounter > 0) {
                return;
            }

            if (practiceMode) {
                if (Map.checkpoints.isEmpty()) {
                   restart();
                } else {
                    Checkpoint lastCheckpoint = Map.checkpoints.get(Map.checkpoints.size() - 1);
                    x = lastCheckpoint.getX();
                    y = lastCheckpoint.getY();
                    gamemode = lastCheckpoint.getGamemode();
                    vy = lastCheckpoint.getVy();
                    g = lastCheckpoint.g;
                    initY = lastCheckpoint.initY;
                    shipG = lastCheckpoint.shipG;
                    shipLift = lastCheckpoint.shipLift;
                    reverse= lastCheckpoint.reverse;
                    angle = 0;
                    groundLevel = (int) y + width;
                }
            }
            else {

                int score = (int) x ;
                String s;

                if (MenuPanel.targetLevel == 1) {
                    if (score > Consts.lvl1TopScore) {
                        Consts.lvl1TopScore = score;
                        s =Integer.toString(score) + "\n" +  Integer.toString(Consts.lvl2TopScore) + "\n" + Integer.toString(Consts.lvl3TopScore);
                        Util.writeFile(Consts.scoreFile, s ) ;
                    }
                }
                else if (MenuPanel.targetLevel == 2) {
                    if ( score > Consts.lvl2TopScore) {
                        Consts.lvl2TopScore = score;
                        s = Integer.toString(Consts.lvl1TopScore) + "\n" +Integer.toString(score) +"\n" + Integer.toString(Consts.lvl3TopScore);
                        Util.writeFile(Consts.scoreFile, s ) ;
                        deathTimeCounter = 10;
                    }
                }
                else if (MenuPanel.targetLevel == 3) {
                    if ( score > Consts.lvl3TopScore) {
                        Consts.lvl3TopScore = score;
                        s = Integer.toString(Consts.lvl1TopScore) + "\n" + Integer.toString(Consts.lvl2TopScore) + "\n" + Integer.toString(score) ;
                        Util.writeFile(Consts.scoreFile, s ) ;
                        deathTimeCounter = 10;
                    }
                }

                vx = 0;
                vy = 0;

                deathTimeCounter = 10;

            }

    }

    //khoi tạo lại vi tri của player cac vector chuyển trang thai ve trang thai ban dau
    public void restart() {
        GameFrame.startGameSound(MenuPanel.targetLevel);
        gamemode = "cube";
        upright();
        initY = -41.55;
        y = Consts.floor - height;
        vy = 0;
        vx = 22;
        reverse = false;
        x = constantX;
        onSurface = true;
    }

    //ở gamemode cube nhân vât có the nhay tren các be mat
    public void cubeJump() {
        if (gamemode == "cube" ) {
            if (onSurface  || prevOnSurface) {
                vy = initY; //van toc y ban dau khi nhay
            }
        }
        onSurface = false;
    }


    //player's hit box
    public Rectangle getHitbox() {
        return new Rectangle((int) x, (int) y, width, height);
    }


    //hieu ung cho player khi chet
    public void draw(Graphics g, int offsetY) {

        if (deathTimeCounter > 0) {
            g.drawImage(deathAnimation, (int) (constantX) - ((200 - width)/2), (int) (y + offsetY) - ((200-height)/2), 200, 200, null);
            return;
        }
        drawSprite( g, offsetY);

        if (win) {
            Util.drawCenteredString(g, "Map Complete", new Rectangle(0, 0, Consts.SCREEN_WIDTH, Consts.SCREEN_HEIGHT), titleFont);
        }
    }

    //hiệu ung theo gamemode
    public void drawSprite( Graphics g, int offsetY) {
        Graphics2D g2D = (Graphics2D)g;
        AffineTransform rot = new AffineTransform();
        rot.rotate(angle,(double) width/2,(double) height/2);
        AffineTransformOp rotOp = new AffineTransformOp(rot, AffineTransformOp.TYPE_BILINEAR);
        if (gamemode == "cube") {
            g2D.drawImage(icon, rotOp, (int) constantX, (int) y + offsetY);
        }

        else if (gamemode == "ship") {
            g2D.drawImage(shipIcon, rotOp, (int) constantX, (int) y + offsetY);
        }

    }



    //phuong thuc getter and setter
    public String getGamemode() {
        return gamemode;
    }
    public void setGamemode(String e) { gamemode = e;}


    public void setX(int x) { this.x = x;}
    public void setY(int y) { this.y = y; }
    public void setVY(int vy) { this.vy = vy; }

    public int getWidth() {return width;}
    public int getHeight() {return height;}
    public double getX() {return x;}
    public double getY() {return y;}
    public double getVX() {return vx;}
    public double getVY() {return vy;}
    public int getOffsetY() {return offsetY; }
    public int getGroundLevel() { return groundLevel; }


}
