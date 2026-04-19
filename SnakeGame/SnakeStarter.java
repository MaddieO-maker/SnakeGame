import java.awt.*;
import javax.swing.*;

public class SnakeStarter extends JPanel {
    private final int gridSize = 20;
    private final int cellSize = 25;

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(new Color(22, 28, 39));
        g.fillRect(0, 0, gridSize * cellSize, gridSize * cellSize);
        // draw a green 3-segment snake near the center
        g.setColor(new Color(0, 255, 0));
        int centerX = gridSize / 2 * cellSize;  
        int centerY = gridSize / 2 * cellSize;
        g.fillRect(centerX, centerY, cellSize, cellSize);
        g.fillRect(centerX - cellSize, centerY, cellSize, cellSize);
        g.fillRect(centerX - 2 * cellSize, centerY, cellSize,
cellSize);
    }

    // main method to create the window
    public static void main(String[] args) {
        JFrame frame = new JFrame("Snake Starter");
        frame.setSize(500, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new SnakeStarter());
        frame.setVisible(true);
    }

}