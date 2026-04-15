import javax.swing.*;
import java.awt.*;

public class SnakeGame {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Snake");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new GamePanel());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    static class GamePanel extends JPanel {
        private static final int CELL_SIZE = 30;
        private static final int COLUMNS = 20;
        private static final int ROWS = 20;
        private static final Color BACKGROUND_COLOR = new Color(40, 40, 40);
        private static final Color GRID_COLOR = new Color(60, 60, 60);
        private static final Color SNAKE_COLOR = new Color(0, 200, 0);

        private final Point[] snake = {
            new Point(8, 9),
            new Point(9, 9),
            new Point(10, 9)
        };

        public GamePanel() {
            setPreferredSize(new Dimension(COLUMNS * CELL_SIZE, ROWS * CELL_SIZE));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            drawBackground(g);
            drawGrid(g);
            drawSnake(g);
        }

        private void drawBackground(Graphics g) {
            g.setColor(BACKGROUND_COLOR);
            g.fillRect(0, 0, getWidth(), getHeight());
        }

        private void drawGrid(Graphics g) {
            g.setColor(GRID_COLOR);
            for (int x = 0; x <= COLUMNS; x++) {
                g.drawLine(x * CELL_SIZE, 0, x * CELL_SIZE, ROWS * CELL_SIZE);
            }
            for (int y = 0; y <= ROWS; y++) {
                g.drawLine(0, y * CELL_SIZE, COLUMNS * CELL_SIZE, y * CELL_SIZE);
            }
        }

        private void drawSnake(Graphics g) {
            g.setColor(SNAKE_COLOR);
            for (Point segment : snake) {
                g.fillRect(segment.x * CELL_SIZE, segment.y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
            }
        }
    }
}
