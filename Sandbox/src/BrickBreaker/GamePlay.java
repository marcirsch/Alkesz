package BrickBreaker;


import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.nio.Buffer;
import java.util.Random;
import javax.swing.Timer;

import javax.swing.JPanel;
import javax.swing.border.Border;


public class GamePlay extends JPanel implements KeyListener, ActionListener, MouseMotionListener {
    private boolean play = false;
    private boolean lost = false;

    private int score = 0;

    private int totalBricks = 21;


    private Timer timer;
    private int delay = 8;

    private int playerX = 310;

    private int ballPosX = 120;
    private int ballPosY = 350;
    private int ballXdir = -1;
    private int ballYdir = -2;

    private final int BORDER_WIDTH = 3;
    private final int PLAYER_WIDTH = 100;
    private int screenWidth = 600;
    private int screenHeight = 700;

    private int blankCounter = 0;
    private int blankDir = 1;

    private MapGenerator mapGenerator;

    public GamePlay() {
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        addMouseMotionListener(this);

        mapGenerator = new MapGenerator(3, 7);


        timer = new Timer(delay, this);
        timer.start();


    }

    public void paint(Graphics g) {
        screenHeight = getHeight();
        screenWidth = getWidth();


        BufferedImage bufferedImage = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_RGB);


        Graphics2D graphics2D = (Graphics2D) bufferedImage.getGraphics();


        graphics2D.setColor(Color.lightGray);
        graphics2D.fillRect(1, 1, screenWidth, screenHeight);

//        System.out.println("width: " + getWidth() + " Height: " + getHeight());

        //borders
        graphics2D.setColor(Color.YELLOW);
        graphics2D.fillRect(0, 0, BORDER_WIDTH, screenHeight);
        graphics2D.fillRect(0, 0, screenWidth, BORDER_WIDTH);
        graphics2D.fillRect(screenWidth - BORDER_WIDTH, 0, BORDER_WIDTH, screenHeight);

        //panel
        graphics2D.setColor(Color.GREEN);
        graphics2D.fillRect(playerX, screenHeight - 50, PLAYER_WIDTH, 8);

        //ball
        graphics2D.setColor(Color.GREEN);
        graphics2D.fillOval(ballPosX, ballPosY, 20, 20);

        mapGenerator.draw(graphics2D);

        if (lost) {
//            System.out.println("You Lost, like a lot");
            graphics2D.setFont(new Font("TimesRoman", Font.BOLD, 30));
            graphics2D.drawString("You Lost, like a lot", screenWidth / 2 - 30, 700);

            int bl = blankCounter * 10;
            graphics2D.setColor(Color.BLACK);
            graphics2D.fillRect(0, 0, screenWidth, bl);
            graphics2D.fillRect(0, screenHeight - bl, screenWidth, screenHeight / 2 + bl);
//            graphics2D.fillRect(0,0,bl,screenHeight);
//            graphics2D.fillRect(screenHeight -bl,0,screenWidth/2 +bl,screenHeight);

            if (bl > screenHeight / 2 || bl < 0) {
                blankDir = -blankDir;
            }
            blankCounter += blankDir;
        }



        g.drawImage(bufferedImage, 0, 0, null);


    }

    void Reset() {
        ballPosX = 120;
        ballPosY = 350;
    }


    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        timer.start();

        if (play) {
            if (new Rectangle(ballPosX, ballPosY, 20, 20).intersects(new Rectangle(playerX, screenHeight - 50, PLAYER_WIDTH, 8))) {
                ballYdir *= -1;
            }

            ballPosX += ballXdir;
            ballPosY += ballYdir;

            if (ballPosX <= 0) {
                ballXdir *= -1;
            } else if (ballPosX >= screenWidth - 20) {
                ballXdir *= -1;
            }

            if (ballPosY < 0) {
                ballYdir *= -1;
            }

            if (ballPosY > screenHeight) {
                play = false;
                lost = true;
                System.out.println("You lost");
                Reset();
            }
        }

        repaint();
    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {

    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        System.out.println("playerX = " + playerX);
        if (keyEvent.getKeyCode() == KeyEvent.VK_RIGHT) {
            if (playerX >= screenWidth - PLAYER_WIDTH - BORDER_WIDTH) {
                playerX = screenWidth - PLAYER_WIDTH - BORDER_WIDTH;
            } else {
                moveRight();
            }

        }
        if (keyEvent.getKeyCode() == KeyEvent.VK_LEFT) {
            if (playerX <= BORDER_WIDTH + 20) {
                playerX = BORDER_WIDTH;
            } else {
                moveLeft();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {

    }

    public void moveBall() {

    }

    public void moveRight() {
        play = true;
        lost = false;
        playerX += 20;
    }

    public void moveLeft() {
        play = true;
        lost = false;
        playerX -= 20;
    }

    @Override
    public void mouseDragged(MouseEvent mouseEvent) {

        mouseEvent.consume();
    }

    @Override
    public void mouseMoved(MouseEvent mouseEvent) {
//        System.out.println("X: " + mouseEvent.getX() + " Y: " + mouseEvent.getY());
        playerX = mouseEvent.getX() - PLAYER_WIDTH / 2;
        mouseEvent.consume();
    }
}
