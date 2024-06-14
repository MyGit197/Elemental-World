// Solid.java
import java.awt.*;
import java.awt.image.BufferedImage;

public class Solid {
    //kich thuoc va toa do

    private int x, y;
    public int width;
    public int height;

    private BufferedImage img = Util.loadBuffImage("assets/solids/block.png");

    private String type;


    // pt khoi tao
    public Solid(int x, int y, String type) {

        this.x = x;
        this.y = y;
        if(type == "slabDown"){
            this.y = y + 37;
        }
        this.type = type;

        if (type == "solid") {
            this.width = 75;
            this.height = 75;
        }
        else if (type == "slabUp" || type == "slabDown") {
            this.width = 75;
            this.height = 37;
        }

        img = Util.resize(img, width, height);
    }



    public Rectangle getRect() {
        return new Rectangle(x, y, height, width);
    }


    public void draw(Graphics g, int offsetX, int offsetY, Player player) {

        int n = (int) (Consts.SCREEN_WIDTH / 2 - GamePanel.player.constantX);
        int a = (int) Math.abs(x - (player.getX() + n));
        //ve solid neu khoang cach giua no và player khong vuot qua 1/2 man hinh
        if( Util.onScreen(player, x)) {
            int b =  (int) (a * 0.07 );//thay doi y cho phu hơp
            if ( a < 550 || Math.abs(player.getGroundLevel() - Consts.floor) <300) {
                b = 0;
            }
            if( y + width < player.getY()) { // khi solid ở ngay duoi player di chuyen xuong
                b *= -1;
            }
            g.drawImage(img, x + offsetX, y + offsetY + b, width, height, null);

        }


    }

    public double getX() { return x;}
    public double getY() { return y;}
    public int getWidth() {return width;}
    public int getHeight() {return height;}



}