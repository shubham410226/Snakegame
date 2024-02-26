import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

public class SnakeGame extends JFrame {

    private final int WIDTH = 400;
    private final int HEIGHT = 400;
    private final int UNIT_SIZE = 20;
    private final int GAME_UNITS = (WIDTH * HEIGHT) / UNIT_SIZE;
    private final int DELAY = 75;

    private ArrayList<Point> snake;
    private Point food;
    private char direction = 'R';
    private boolean running = false;
    private int score = 0; // Score variable
    private Timer timer;
    private JLabel scoreLabel; // JLabel for displaying score

    public SnakeGame() {
        setTitle("Snake Game");
        setSize(WIDTH, HEIGHT);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        addKeyListener(new MyKeyAdapter());

        // Create score label
        scoreLabel = new JLabel("Score: 0");
        scoreLabel.setForeground(Color.WHITE);
        add(scoreLabel, BorderLayout.NORTH);

        snake = new ArrayList<>();
        startGame();
        setVisible(true);
    }

    private void startGame() {
        snake.clear();
        snake.add(new Point(WIDTH / 2, HEIGHT / 2));
        generateFood();
        running = true;
        timer = new Timer(DELAY, e -> gameLoop());
        timer.start();
    }

    private void gameLoop() {
        if (running) {
            move();
            checkCollisions();
            repaint();
        }
    }

    private void move() {
        Point head = snake.get(0);
        Point newHead = new Point(head.x, head.y);
        switch (direction) {
            case 'U':
                newHead.y -= UNIT_SIZE;
                break;
            case 'D':
                newHead.y += UNIT_SIZE;
                break;
            case 'L':
                newHead.x -= UNIT_SIZE;
                break;
            case 'R':
                newHead.x += UNIT_SIZE;
                break;
        }
        snake.add(0, newHead);
        if (!food.equals(newHead)) {
            snake.remove(snake.size() - 1);
        } else {
            generateFood();
            score++; // Increase score when snake eats food
            scoreLabel.setText("Score: " + score); // Update score label
        }
    }

    private void checkCollisions() {
        Point head = snake.get(0);
        // Check collision with walls
        if (head.x < 0 || head.x >= WIDTH || head.y < 0 || head.y >= HEIGHT) {
            gameOver();
            return;
        }
        // Check collision with itself
        for (int i = 1; i < snake.size(); i++) {
            if (head.equals(snake.get(i))) {
                gameOver();
                return;
            }
        }
    }

    private void generateFood() {
        Random rand = new Random();
        int x = rand.nextInt((WIDTH / UNIT_SIZE)) * UNIT_SIZE;
        int y = rand.nextInt((HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
        food = new Point(x, y);
    }

    private void gameOver() {
        running = false;
        timer.stop();
        JOptionPane.showMessageDialog(this, "Game Over. Your score is: " + score, "Game Over", JOptionPane.INFORMATION_MESSAGE);
        startGame();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (running) {
            // Draw snake
            for (Point point : snake) {
                g.setColor(Color.GREEN);
                g.fillRect(point.x, point.y, UNIT_SIZE, UNIT_SIZE);
            }
            // Draw food
            g.setColor(Color.RED);
            g.fillRect(food.x, food.y, UNIT_SIZE, UNIT_SIZE);
        }
    }

    private class MyKeyAdapter implements KeyListener {
        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();
            if (key == KeyEvent.VK_UP && direction != 'D') {
                direction = 'U';
            } else if (key == KeyEvent.VK_DOWN && direction != 'U') {
                direction = 'D';
            } else if (key == KeyEvent.VK_LEFT && direction != 'R') {
                direction = 'L';
            } else if (key == KeyEvent.VK_RIGHT && direction != 'L') {
                direction = 'R';
            }
        }

        @Override
        public void keyTyped(KeyEvent e) {}

        @Override
        public void keyReleased(KeyEvent e) {}
    }

    public static void main(String[] args) {
        new SnakeGame();
    }
}
