import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.LinkedList;
import java.util.List;

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
        private static final int TIMER_DELAY = 150;

        private final List<Point> snake = new LinkedList<>();
        private Direction currentDirection = Direction.RIGHT;
        private Direction nextDirection = Direction.RIGHT;

        public GamePanel() {
            setPreferredSize(new Dimension(COLUMNS * CELL_SIZE, ROWS * CELL_SIZE));
            setFocusable(true);
            setBackground(BACKGROUND_COLOR);
            addKeyListener(new SnakeKeyAdapter());

            snake.add(new Point(8, 9));
            snake.add(new Point(9, 9));
            snake.add(new Point(10, 9));

            Timer timer = new Timer(TIMER_DELAY, e -> {
                moveSnake();
                repaint();
            });
            timer.start();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            drawBackground(g);
            drawGrid(g);
            drawSnake(g);
        }

        private void moveSnake() {
            currentDirection = nextDirection;
            Point head = snake.get(snake.size() - 1);
            int nextX = head.x + currentDirection.dx;
            int nextY = head.y + currentDirection.dy;

            if (nextX < 0) {
                nextX = COLUMNS - 1;
            } else if (nextX >= COLUMNS) {
                nextX = 0;
            }
            if (nextY < 0) {
                nextY = ROWS - 1;
            } else if (nextY >= ROWS) {
                nextY = 0;
            }

            snake.remove(0);
            snake.add(new Point(nextX, nextY));
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

        private class SnakeKeyAdapter extends KeyAdapter {
            @Override
            public void keyPressed(KeyEvent e) {
                Direction requested = null;

                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT:
                        requested = Direction.LEFT;
                        break;
                    case KeyEvent.VK_RIGHT:
                        requested = Direction.RIGHT;
                        break;
                    case KeyEvent.VK_UP:
                        requested = Direction.UP;
                        break;
                    case KeyEvent.VK_DOWN:
                        requested = Direction.DOWN;
                        break;
                }

                if (requested != null && !requested.isOpposite(currentDirection)) {
                    nextDirection = requested;
                }
            }
        }

        private enum Direction {
            UP(0, -1),
            DOWN(0, 1),
            LEFT(-1, 0),
            RIGHT(1, 0);

            final int dx;
            final int dy;

            Direction(int dx, int dy) {
                this.dx = dx;
                this.dy = dy;
            }

            boolean isOpposite(Direction other) {
                return dx == -other.dx && dy == -other.dy;
            }
        }
    }
}
