//Effect.java
/*
Class này được thiết kế để tạo và quản lý các hiệu ứng hạt cho các đối tượng khác nhau như cube, tàu (ship), và pad trong trò chơi.
Các hạt này là những hình vuông nhỏ dần mờ đi và biến mất, mô phỏng hiệu ứng như tia lửa hoặc mảnh vỡ.
 */
import java.awt.*;
public class Effect {
    // toạ độ, kích thước, huong di chuyen (radian)
    double x, y, angle;
    double width, height;

    //vector, tp toạ độ
    double vx, vy, speed;

    //mốc ban đầu
    double startX, startY;

    //color saturation
    int saturation = 255;

    //khoang cach toi da ma hieu ung ton tai
    // xoa khoi array list trong game panel điều khiển đối tượng
    double maxdist;

    // constructor
    public Effect(double x, double y, double angle, double width, double height, double speed, double maxdist) {
        this.x = x;
        this.startX = x;
        this.y= y;
        this.startY = y;
        this.width = width;
        this.height = height;
        this.angle = angle;
        this.speed = speed;
        this.vx = speed * Math.cos(angle);
        this.vy = speed * Math.sin(angle);
        this.maxdist = maxdist;
    }

    //chuyển động
    public void move() {
        x+=vx;
        y+=vy;

        //set kích thước nhỏ dần
        width = width* 0.97;
        height = height * 0.97;

    }

    // vẽ
    public void draw( Graphics g, int offsetX, int offsetY) {
        g.setColor( new Color(255, 255, 25, saturation));
        g.fillRect((int)x + offsetX, (int)y + offsetY, (int)width, (int) height);
        saturation = (int) (saturation * 0.87) ; //biến mất dần
    }

}
