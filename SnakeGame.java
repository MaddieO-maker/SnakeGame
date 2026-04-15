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
        private static final Color[] RAINBOW_COLORS = {
            Color.RED,
            new Color(255, 165, 0),
            Color.YELLOW,
            Color.GREEN,
            Color.BLUE,
            new Color(75, 0, 130),
            new Color(128, 0, 128)
        };
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
            Graphics2D g2 = (Graphics2D) g.create();
            for (int i = 0; i < snake.size(); i++) {
                Point segment = snake.get(i);
                int colorIndex = (snake.size() - 1 - i) % RAINBOW_COLORS.length;
                if (colorIndex < 0) {
                    colorIndex += RAINBOW_COLORS.length;
                }
                g2.setColor(RAINBOW_COLORS[colorIndex]);
                int x = segment.x * CELL_SIZE;
                int y = segment.y * CELL_SIZE;

                if (i == snake.size() - 1 && snake.size() > 1) {
                    Direction headDirection = getDirection(snake.get(i - 1), segment);
                    drawHalfRoundedSegment(g2, x, y, headDirection, true);
                } else if (i == 0 && snake.size() > 1) {
                    Direction tailDirection = getDirection(segment, snake.get(1));
                    drawHalfRoundedSegment(g2, x, y, tailDirection, false);
                } else {
                    g2.fillRect(x, y, CELL_SIZE, CELL_SIZE);
                }
            }
            g2.dispose();
        }

        private void drawHalfRoundedSegment(Graphics2D g2, int x, int y, Direction direction, boolean frontHalfRounded) {
            int arc = CELL_SIZE / 2;
            g2.fillRoundRect(x, y, CELL_SIZE, CELL_SIZE, arc, arc);
            if (direction == null) {
                return;
            }

            if (frontHalfRounded) {
                switch (direction) {
                    case RIGHT:
                        g2.fillRect(x, y, CELL_SIZE / 2, CELL_SIZE);
                        break;
                    case LEFT:
                        g2.fillRect(x + CELL_SIZE / 2, y, CELL_SIZE / 2, CELL_SIZE);
                        break;
                    case DOWN:
                        g2.fillRect(x, y, CELL_SIZE, CELL_SIZE / 2);
                        break;
                    case UP:
                        g2.fillRect(x, y + CELL_SIZE / 2, CELL_SIZE, CELL_SIZE / 2);
                        break;
                }
            } else {
                switch (direction) {
                    case RIGHT:
                        g2.fillRect(x + CELL_SIZE / 2, y, CELL_SIZE / 2, CELL_SIZE);
                        break;
                    case LEFT:
                        g2.fillRect(x, y, CELL_SIZE / 2, CELL_SIZE);
                        break;
                    case DOWN:
                        g2.fillRect(x, y + CELL_SIZE / 2, CELL_SIZE, CELL_SIZE / 2);
                        break;
                    case UP:
                        g2.fillRect(x, y, CELL_SIZE, CELL_SIZE / 2);
                        break;
                }
            }
        }

        private Direction getDirection(Point from, Point to) {
            int dx = to.x - from.x;
            int dy = to.y - from.y;
            if (dx > 0) {
                return Direction.RIGHT;
            }
            if (dx < 0) {
                return Direction.LEFT;
            }
            if (dy > 0) {
                return Direction.DOWN;
            }
            if (dy < 0) {
                return Direction.UP;
            }
            return null;
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
