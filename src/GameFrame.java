//GameFrame.java
// Lop nay de tao ra 1 Frame la GameFrame chua GamePanel

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class GameFrame extends JFrame implements ActionListener {
    static GamePanel elementalWorld = new GamePanel(Consts.map1, Consts.ForestFrontierSound);


    public GameFrame() {
        super("Elemental World");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(Consts.SCREEN_WIDTH, Consts.SCREEN_HEIGHT);
        add(elementalWorld);
        setIconImage(Consts.windowIcon.getImage());
    }

    public void actionPerformed(ActionEvent e) {
        elementalWorld.move();
        elementalWorld.repaint();
    }

    public static void stopTimer() {
        elementalWorld.timer.stop();
    }

    public static void RESET() {//reset the player
        elementalWorld.resetPlayer();
    }

    // Khoi dong timer, tai lai map de reset man choi
    public static void startTimer(int lv) {
        if (lv == 1) {
            elementalWorld.mapReload(Consts.map1, Consts.ForestFrontierSound);

        } else if (lv == 2) {
            elementalWorld.mapReload(Consts.map2, Consts.OceanDepthSound);

        } else if (lv == 3) {
            elementalWorld.mapReload(Consts.map3, Consts.LavaPeakSound);
        }

        elementalWorld.timer.start();
    }

    // Phuong thuc de dung am thanh
    public static void stopGameSound(){
        Util.stopSound(Consts.lvl1Sound);
        Util.stopSound(Consts.lvl2Sound);
        Util.stopSound(Consts.lvl3Sound);
    }


    // Phuong thuc de bat am thanh
    public static void startGameSound(int lv) {
        if (lv == 1) {
            Util.startSound(Consts.lvl1Sound);
            Util.stopSound(Consts.lvl2Sound);
            Util.stopSound(Consts.lvl3Sound);

        }

        else if (lv == 2) {
            Util.startSound(Consts.lvl2Sound);
            Util.stopSound(Consts.lvl1Sound);
            Util.stopSound(Consts.lvl3Sound);

        }

        else if (lv == 3) {
            Util.startSound(Consts.lvl3Sound);
            Util.stopSound(Consts.lvl2Sound);
            Util.stopSound(Consts.lvl1Sound);
        }

    }


}
