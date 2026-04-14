import javax.swing.*;
import java.awt.*;

public class SnakeGame {
    // 
    public static void main(String[] args) {    
        JFrame frame = new JFrame("Snake");
        frame.setSize(600, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new GamePanel());
        frame.setVisible(true);
    }

    static class GamePanel extends JPanel {
        public GamePanel() {
            setBackground(Color.BLACK);
        }
    }
}
