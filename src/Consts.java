/*
    Consts.java
    Class này chứa các biến và hằng số toàn cục được sử dụng trong game
 */

import javax.sound.sampled.Clip;
import javax.swing.*;

public class Consts {

    public static final int SCREEN_WIDTH = 1250;
    public static final int SCREEN_HEIGHT = 800;

    public static final int floor = SCREEN_HEIGHT - 50; // GIA TRI Y CUA FLOOR
    public static final int slabWidth = 75;
    public static final int solidWidth = 75;
    public static final int solidHeight = 75;

    public static final int SHIP_CEILING = Consts.floor - Consts.solidHeight * 11 + 35; // THE Y VALUE FOR THE CEILING

    //Đường dẫn đến hình ảnh bản đồ.
    static String map1 = "assets/maps/ForestFrontier.png";
    static String map2 = "assets/maps/OceanDepth.png";
    static String map3 = "assets/maps/LavaPeak.png";
    // Đường dẫn đến file am thanh.
    static String MainMenuSound = "soundTrack/MainMenu.wav";
    static String ForestFrontierSound = "soundTrack/ForestFrontier.wav";
    static String OceanDepthSound = "soundTrack/OceanDepth.wav";
    static String LavaPeakSound = "soundTrack/LavaPeak.wav";

    // Biến lưu trữ các đối tượng Clip của âm thanh.
    public static Clip lvl1Sound = Util.getSound(Consts.ForestFrontierSound);
    public static Clip lvl2Sound = Util.getSound(OceanDepthSound);
    public static Clip lvl3Sound = Util.getSound(LavaPeakSound);
    public static Clip MenuMusic = Util.getSound(Consts.MainMenuSound);

    //Đường dẫn đến file chứa điểm số cao nhất của mỗi cấp độ.
    public static String scoreFile = "src/scores";

    // ĐIỂM CAO NHAT MỖI LEVEL
    public static int lvl1TopScore = Integer.parseInt(Util.readFile(Consts.scoreFile, 1) );
    public static int lvl2TopScore = Integer.parseInt(Util.readFile(Consts.scoreFile, 2) );
    public static int lvl3TopScore = Integer.parseInt(Util.readFile(Consts.scoreFile, 3 ));


    public static ImageIcon windowIcon = new ImageIcon("assets/logos/ElementalWorld.png"); // LOGO
}