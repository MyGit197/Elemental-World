// Portal.java

//doi tuong thay doi che do, huong di chuyen cua nhan vat khi qua cong
import java.awt.*;
import java.awt.image.BufferedImage;

public class Portal {

    // toa do va kich tuong
    private int x, y;
    public static int width = 75;
    public static int height = 225;
    private static int gravity; // 0 no switch, 1 upside down, 2 up

    //loại thay đôi
    private String type;

    private BufferedImage portalIcon;

    // phuong thuc khoi tao
    public Portal(int x, int y, String type, int gravity) {

        this.x = x;
        this.y = y;
        this.type = type;
        this.gravity = gravity;

        //assign portal image accorddingly the reseize it
        if (type == "cube"){
            portalIcon = Util.loadBuffImage("assets/portals/cubePortal.png");
        }
        else if (type == "ship" ||  type == "reverse" || type == "upright") {
            portalIcon = Util.loadBuffImage("assets/portals/shipPortal.png");
        }
        portalIcon = Util.resize(portalIcon, width, height);
    }

    public Rectangle getRect() {
        return new Rectangle(x, y, width, height);
    }

    public void draw( Graphics g, int offsetX, int offsetY) { //draws the portal
        g.drawImage(portalIcon, x + offsetX, y + offsetY, null);
    }


    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }


    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getType() {
        return type;
    }

}
