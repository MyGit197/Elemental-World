/*
    Checkpoint.java
Class này được thiết kế để lưu trữ thông tin về các checkpoint trong trò chơi
Các checkpoint này cho phép người chơi lưu lại trạng thái hiện tại và quay lại đó khi cần
Dùng trong practice mode

 */

import java.awt.*;
import java.awt.image.BufferedImage;

public class Checkpoint {

    private double x, y; // toạ độ checkpoint
    private int width = 30, height = 60; // kích thước khi vẽ checkpoint
    private BufferedImage img; // hình ảnh checkpoint
    // Các giá trị vận tốc và trọng lực được lưu trữ, bao gồm vận tốc theo trục x và y, trọng lực, vị trí y ban đầu, trọng lực, lực nâng tàu
    public double vx, vy, g, initY, shipG, shipLift;
    private String gamemode; //  Chế độ chơi được lưu trữ
    public boolean reverse; // Trạng thái của cube/ship khi lộn ngược).

    // constructor
    public Checkpoint(double xx, double yy, double vx, double vy, double g, double initY, double shipG, double shipLift, String gamemode, boolean reverse) {
        this.x = xx;
        this.y = yy;
        this.vx = vx;
        this.vy = vy;
        this.g = g;
        this.shipG = shipG;
        this.initY = initY;
        this.shipLift = shipLift;
        this.gamemode = gamemode;
        img = Util.loadBuffImage("assets/checkpoint/checkpoint.png");
        img = Util.resize(img, width, height);
        this.reverse = reverse;
    }

    public void draw(Graphics g, int offsetX, int offsetY) { // vẽ checkpoint
        g.drawImage(img, (int)x + offsetX, (int)y + offsetY, null);
    }

    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }
    public double getVy() {
        return vy;
    }
    public String getGamemode() {
        return gamemode;
    }

    public String toString() {
        return "<Checkpoint: " + x + ", " + y + ", " + gamemode + ">";
    }
}
