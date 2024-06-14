import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

//doi tuong tang van toc phuong y cua nhan vat khi va cham
public class Orb {

    // toa do, kich thuoc
    private int x, y;
    public static int width = 75, height = 75;

    //chenh lech ban kinh các vong tron
    public int rDiff = 7;

    private BufferedImage img;

    //luu ban kinh, so chuyen mau
    ArrayList<Integer> radius = new ArrayList<Integer>();
    ArrayList<Integer> saturation = new ArrayList<Integer>();

    public Orb(int x, int y, String type) {
        Random rand = new Random();
        this.x = x;
        this.y = y;
        for( int i = 1; i <= 7; i++) {
            radius.add( i* rDiff) ;
            saturation.add(rand.nextInt(7) * 36);
        }
    }


    //hit box xac dinh va cham cua nhan vat va hitbox
    public Rectangle getHitbox() {
        return new Rectangle(x -10, y -10, width+20, height+20);
    }

    public void drawHitbox(Graphics g, int offsetX, int offsetY) {
        for (int i = 0; i<radius.size() ; i ++ ) {
            radius.set( i ,  radius.get(i) + 3);
            int r = radius.get(i);
            if ( r > rDiff * (radius.size())) {
                radius.set( i ,  rDiff * 3);
            }
            g.setColor( new Color(0, 239, 255, saturation.get(i) ));
            g.fillOval(x + width/2 -r +offsetX, y + height/2 - r + offsetY, 2 * r, 2 * r);
        }
    }

    public void draw(Graphics g, int offsetX, int offsetY) {
        drawHitbox(g, offsetX, offsetY);
    }

}
