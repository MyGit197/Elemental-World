/*
    Pad.class
    Đối tuong khi nhan vat tuong tac cung se day nhan vat bat len
 */

import java.awt.*;

public class Pad {
    //coordinate and dimensions
    private double x,y, width, height;

    public Pad(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;

        this.width = width;
        this.height = height;
    }

    public Rectangle getRect() {
        return new Rectangle( (int) x, (int) y, (int)height, (int)width);
    }


    public void draw(Graphics g, int offsetX, int offsetY) {
        g.setColor(Color.BLACK);
        g.setColor(new Color(0, 208, 255, 255));
    }


    public double getY() { return y;}
    public double getWidth() {return width;}
    public double getX() {return x;}
    public double getHeight() {return height;}



}