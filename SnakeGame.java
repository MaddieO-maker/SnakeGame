import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

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
        private static final Color FOOD_COLOR = new Color(200, 80, 0);
        private static final Color TEXT_COLOR = Color.WHITE;
        private static final int TIMER_DELAY = 150;

        private final List<Point> snake = new LinkedList<>();
        private final Random random = new Random();
        private Direction currentDirection = Direction.RIGHT;
        private Direction nextDirection = Direction.RIGHT;
        private Point food;
        private Timer timer;
        private boolean gameOver;
        private int score;

        public GamePanel() {
            setPreferredSize(new Dimension(COLUMNS * CELL_SIZE, ROWS * CELL_SIZE));
            setFocusable(true);
            setBackground(BACKGROUND_COLOR);
            addKeyListener(new SnakeKeyAdapter());
            resetGame();
        }

        @Override
        public void addNotify() {
            super.addNotify();
            requestFocusInWindow();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            drawBackground(g);
            drawGrid(g);
            drawSnake(g);
            drawFood(g);
            drawScore(g);
            if (gameOver) {
                drawGameOver(g);
            }
        }

        private void resetGame() {
            snake.clear();
            snake.add(new Point(8, 9));
            snake.add(new Point(9, 9));
            snake.add(new Point(10, 9));
            currentDirection = Direction.RIGHT;
            nextDirection = Direction.RIGHT;
            score = 0;
            gameOver = false;
            spawnFood();

            if (timer != null) {
                timer.stop();
            }

            timer = new Timer(TIMER_DELAY, e -> {
                moveSnake();
                repaint();
            });
            timer.start();
            requestFocusInWindow();
        }

        private void moveSnake() {
            if (gameOver) {
                return;
            }

            currentDirection = nextDirection;
            Point head = snake.get(snake.size() - 1);
            int nextX = head.x + currentDirection.dx;
            int nextY = head.y + currentDirection.dy;

            if (nextX < 0 || nextX >= COLUMNS || nextY < 0 || nextY >= ROWS) {
                endGame();
                return;
            }

            Point nextHead = new Point(nextX, nextY);
            if (snake.contains(nextHead)) {
                endGame();
                return;
            }

            snake.add(nextHead);
            if (nextHead.equals(food)) {
                score++;
                spawnFood();
            } else {
                snake.remove(0);
            }
        }

        private void spawnFood() {
            do {
                int x = random.nextInt(COLUMNS);
                int y = random.nextInt(ROWS);
                food = new Point(x, y);
            } while (snake.contains(food));
        }

        private void endGame() {
            gameOver = true;
            if (timer != null) {
                timer.stop();
            }
            repaint();
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

        private void drawFood(Graphics g) {
            if (food != null) {
                g.setColor(FOOD_COLOR);
                g.fillOval(food.x * CELL_SIZE + 4, food.y * CELL_SIZE + 4, CELL_SIZE - 8, CELL_SIZE - 8);
            }
        }

        private void drawScore(Graphics g) {
            g.setColor(TEXT_COLOR);
            g.setFont(new Font("SansSerif", Font.BOLD, 18));
            g.drawString("Score: " + score, 10, 22);
        }

        private void drawGameOver(Graphics g) {
            String gameOverText = "Game Over";
            String scoreText = "Final Score: " + score;
            String restartText = "Press R to restart";
            g.setColor(new Color(0, 0, 0, 180));
            g.fillRect(0, getHeight() / 2 - 70, getWidth(), 140);
            g.setColor(TEXT_COLOR);
            g.setFont(new Font("SansSerif", Font.BOLD, 36));
            FontMetrics metrics = g.getFontMetrics();
            int textWidth = metrics.stringWidth(gameOverText);
            g.drawString(gameOverText, (getWidth() - textWidth) / 2, getHeight() / 2 - 15);
            g.setFont(new Font("SansSerif", Font.PLAIN, 22));
            metrics = g.getFontMetrics();
            textWidth = metrics.stringWidth(scoreText);
            g.drawString(scoreText, (getWidth() - textWidth) / 2, getHeight() / 2 + 15);
            g.setFont(new Font("SansSerif", Font.PLAIN, 18));
            metrics = g.getFontMetrics();
            textWidth = metrics.stringWidth(restartText);
            g.drawString(restartText, (getWidth() - textWidth) / 2, getHeight() / 2 + 45);
        }

        private class SnakeKeyAdapter extends KeyAdapter {
            @Override
            public void keyPressed(KeyEvent e) {
                if (gameOver && e.getKeyCode() == KeyEvent.VK_R) {
                    resetGame();
                    return;
                }

                if (gameOver) {
                    return;
                }

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
