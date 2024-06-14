/*
 * Util.java
 * Class quản lý các chức năng liên quan đến xử lý hình ảnh, âm thanh và đọc/ghi file
 */

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.*;
import java.util.HashMap;
import java.util.Scanner;
import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import javax.swing.ImageIcon;

public class Util {
    private BufferedImage pixelMap;
    private BufferedImage back;

    private int tileWidth;
    private int tileHeight;
    private HashMap<Integer, Image> tilePics = new HashMap();

    // nhận vào tệp ảnh và trả về đối tượng Image
    public Image loadImage(String name) {
        return (new ImageIcon(name)).getImage();
    }

    //nhận vào tên tệp ảnh và trả về một đối tượng BufferedImage
    public static BufferedImage loadBuffImage(String name) {
        try {
            return ImageIO.read(new File(name));
        } catch (IOException var3) {
            System.out.println(var3);
            return null;
        }
    }

    //tải thông tin header từ tệp và thiết lập các thuộc tính của đối tượng Util.
    public void loadHeader(String name) {
        try {
            Scanner inFile = new Scanner(new File(name));
            this.tileWidth = Integer.parseInt(inFile.nextLine());
            this.tileHeight = Integer.parseInt(inFile.nextLine());
            this.back = this.loadBuffImage(inFile.nextLine());
            this.pixelMap = this.loadBuffImage(inFile.nextLine());
            int numTile = Integer.parseInt(inFile.nextLine());

            for(int i = 0; i < numTile; ++i) {
                int col = Integer.parseInt(inFile.nextLine(), 16);
                this.tilePics.put(col, this.loadImage(inFile.nextLine()));
            }
        } catch (IOException var6) {
            System.out.println(var6);
        }

    }

    //vẽ đầy đủ ảnh nền từ một pixelMap và các tilePics đã được tải.
    public void makeFull() {
        Graphics buffG = this.back.getGraphics();
        int wid = this.pixelMap.getWidth();
        int height = this.pixelMap.getHeight();

        for(int x = 0; x < wid; ++x) {
            for(int y = 0; y < height; ++y) {
                int col = this.pixelMap.getRGB(x, y);
                col &= 16777215;
                if (this.tilePics.containsKey(col)) {
                    Image tile = (Image)this.tilePics.get(col);
                    int offset = this.tileHeight - tile.getHeight((ImageObserver)null);
                    buffG.drawImage(tile, x * this.tileWidth, y * this.tileHeight + offset, (ImageObserver)null);
                }
            }
        }

    }

    //trả về mã màu của tile tại vị trí (x, y) trên pixelMap.
    public int getTile(int x, int y) {
        int col = this.pixelMap.getRGB(x / this.tileWidth, y / this.tileHeight);
        col &= 16777215;
        return col;
    }

    //trả về ảnh nền của đối tượng Util.
    public Image getBackground() {
        return this.back;
    }

    //Constructor của class Util, tải thông tin header và tạo ảnh nền đầy đủ từ tệp đã chỉ định.
    public Util(String name) {
        this.loadHeader(name);
        this.makeFull();
    }

    //kiểm tra xem một đối tượng có nằm trong phạm vi hiển thị của người chơi không.
    public static boolean onScreen( Player player , double objectX) {
        int onScreenConstant = (int) (Consts.SCREEN_WIDTH / 2 - player.constantX);
        double distFromCenter = Math.abs( objectX - (player.getX() + onScreenConstant) );
        if( distFromCenter < Consts.SCREEN_WIDTH / 2 + 300) {
            return true;
        }
        return false;
    }


    //thay đổi kích thước của một BufferedImage thành kích thước mới.
    public static BufferedImage resize(BufferedImage img, int newW, int newH) {
        Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
        BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return dimg;
    }

    //vẽ một chuỗi văn bản được căn giữa vào một hình chữ nhật.
    public static void drawCenteredString(Graphics g, String text, Rectangle rect, Font font) {
        // Get the FontMetrics
        FontMetrics metrics = g.getFontMetrics(font);
        // Determine the X coordinate for the text
        int x = rect.x + (rect.width - metrics.stringWidth(text)) / 2;
        // Determine the Y coordinate for the text
        int y = rect.y + ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent();
        // Set the font
        g.setFont(font);
        // Draw the String
        g.setColor(Color.WHITE);
        g.drawString(text, x, y);
    }

    //dừng âm thanh được phát.
    public static void stopSound(Clip music) {
        if (music != null) {
            music.stop();
//            music.close();
        }
    }

    //phát một âm thanh từ đầu.
    public static void startSound(Clip music) {
//        if( ! music.isRunning()) {

        music.setFramePosition(0);
        music.start();
//        }
    }

    //nhận và trả về một đối tượng Clip từ một tệp âm thanh.
    public static Clip getSound(String soundFilePath) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(soundFilePath));

            Clip music = AudioSystem.getClip();

            music.open(audioInputStream);


            return music;

        } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    //đọc nội dung của một tệp văn bản.
    public static String readFile(String filePath, int curLvl) {

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            String line1 = reader.readLine();
            String line2 = reader.readLine();
            String line3 = reader.readLine();



            if (curLvl == 1) {
                return line1;
            }
            if(curLvl == 2) {
                return line2;
            }
            if(curLvl ==3) {
                return line3;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return "-1" ;
    }

    //ghi nội dung vào một tệp văn bản
    public static void writeFile(String filePath, String content) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(content);
            System.out.println("File written successfully!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
