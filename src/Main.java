import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
// THIS IS THE MAIN.
/*
Điều khiển tất cả các Scenes và Panels
 */

public class Main extends JFrame implements ActionListener {
    //điều khiển các khung, bảng, bộ hẹn giờ khác nhau

    //3 frames:
    static MenuFrame MenuF = new MenuFrame();
    static GameFrame GameF = new GameFrame();

    static PauseScreen pauseMenu = new PauseScreen();

    public Main() {}

    public void actionPerformed(ActionEvent e) {
    }

    public static void main(String[] args) {
        MenuF.setResizable(false);
        GameF.setResizable(false);
        pauseMenu.setResizable(false);
        start();
    }


    //hiển thị menu, phát âm thanh
    private static void start() {
        MenuF.timer.start();
        MenuF.setVisible(true);

        GameF.stopTimer();
        GameF.setVisible(false);

        pauseMenu.pausemenu.timer.stop();
        pauseMenu.setVisible(false);

        MenuF.startMenuSound();

    }

    //hiển thị game, phát nhạc game
    public static void enterGame(int lvl){

        System.out.println("entering game");

        GameF.startTimer(lvl);
        GameF.setVisible(true);

        MenuF.menu.timer.stop();
        MenuF.setVisible(false);

        pauseMenu.timer.stop();
        pauseMenu.setVisible(false);

        GameF.startGameSound(MenuPanel.targetLevel);

        MenuF.stopMenuSound();
    }

    //hiển thị màn hình tạm dừng, ko phát âm thanh
    public static void pauseGame() {

        GameF.stopTimer();
        GameF.setVisible(false);
        GameF.stopGameSound();
        pauseMenu.pausemenu.mousePressed=false;
        pauseMenu.pausemenu.timer.start();
        MenuF.setVisible(false);
        pauseMenu.setVisible(true);
        MenuF.menu.timer.stop();


    }

    //quay lại game, phát nhạc game
    public static void resumeGame() {
        System.out.println("resuming game");
        pauseMenu.pausemenu.timer.stop();
        pauseMenu.setVisible(false);
        MenuF.setVisible(false);
        GameF.elementalWorld.timer.start();
        GameF.setVisible(true);
        GameF.startGameSound(MenuPanel.targetLevel);

    }

    //hiển thị menu, phát nhạc menu
    public static void toMainMenu() {
        System.out.println("to MENU");
        GameF.setVisible(false);
        GameF.stopTimer();
        GameF.RESET();

        pauseMenu.pausemenu.timer.stop();
        pauseMenu.setVisible(false);

        MenuF.menu.timer.start();
        MenuF.setVisible(true);

        MenuF.startMenuSound();
        GameF.stopGameSound();
    }


}