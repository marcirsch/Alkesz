package view;

import model.Arena;
import model.FallObject;
import model.Player;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.Buffer;

/**
 * <h2>The game field</h2>
 * This class implements the game field of the game.
 */
public class ArenaRenderer extends JPanel implements Runnable {

    public static final int PLAYER_WIDTH = 60;
    public static final int PLAYER_HEIGHT = 30;
    public static final int FALLOBJECT_WIDTH = 10;
    public static final int FALLOBJECT_HEIGHT = 10;

    private JLabel scoreLabel;
    private JLabel alcoholLevelLabel;
    private JLabel missedLabel;
    private Arena arena;
    private int blinkT = 1000;
    private int blinkCounter = 1;
    private int blinkDelay = 2000;
    private int blinkSpeed = 50;
    private int blinkDir = 1;
    private int blinkDelayCounter = 0;
    private boolean blinkEnabled = false;

    private Image imageAspirin;
    private Image imageJager;
    private Image imageUnicum;
    private Image imageTatratea;
    private Image imagePlayer;

    private Timer timer; // timer is used for screen update
    /**
     * The constructor of the class. It reads in all the images in advance and buffers them.
     */
    public ArenaRenderer(Arena arena) {
        this.arena = arena;

        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        BufferedImage img;
        try {
            img = ImageIO.read(getClass().getResource("/resources/images/Aspirin.png"));
            imageAspirin = img.getScaledInstance(30, 30, Image.SCALE_DEFAULT);
            img = ImageIO.read(getClass().getResource("/resources/images/Jager.png"));
            imageJager = img.getScaledInstance(40, 40, Image.SCALE_DEFAULT);
            img = ImageIO.read(getClass().getResource("/resources/images/Unicum.png"));
            imageUnicum = img.getScaledInstance(40, 40, Image.SCALE_DEFAULT);
            img = ImageIO.read(getClass().getResource("/resources/images/Tatratea.png"));
            imageTatratea = img.getScaledInstance(40, 40, Image.SCALE_DEFAULT);
            img = ImageIO.read(getClass().getResource("/resources/images/DrunkGuy.png"));
            imagePlayer = img.getScaledInstance(80, 80, Image.SCALE_DEFAULT);
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        timer = new Timer(5, e -> repaint());
        timer.start();
    }
    /**
     * This methods renders the arena, based on the actual state of the game. Also if a given treshold for alcohol
     * level is passed, it applies blur to the field and the blinking effect.
     * @param g The base background for the field.
     */
    private void RenderArena(Graphics2D g) {
        int height = this.getHeight();
        int width = this.getWidth();
        int blinkHeight;
        if (blinkEnabled) {
            blinkDelayCounter = blinkDelayCounter + blinkSpeed * 1;
//            System.out.println(blinkDelayCounter)blinkDelayCounter;
            if (blinkDelayCounter >= blinkDelay) {
                blinkCounter = Math.max(Math.min(blinkCounter + blinkDir * blinkSpeed * 1, blinkT), 0);
            }
        }
        //draw background
        g.setColor(new Color(74, 78, 71));
        //draw player
        g.fillRect(0, 0, width, height);


        Player player = arena.getPlayer();
        RenderPlayer(player, g);
        //draw falling object
        g.setColor(Color.YELLOW);
        try {
            if (!arena.getFallObjectList().isEmpty()) {
                for (FallObject fallObject : arena.getFallObjectList()) {
                    RenderFallObject(fallObject, g);

                }
            }
        } catch (Exception e) {
//            System.out.println("empty list AR");
        }

        //draw tipsy effects
        //Blinking

        if (blinkEnabled) {
            blinkHeight = (int) (((double) blinkCounter / (double) blinkT) * ((double) height / 2));
            g.setColor(new Color(0, 0, 0, 245));
            g.fillRect(0, 0, width, blinkHeight);
            g.fillRect(0, height - blinkHeight, width, blinkHeight);
            if ((blinkCounter == 0 || blinkCounter == blinkT) && blinkDelayCounter >= blinkDelay) {
                if (blinkCounter == 0) {
                    blinkDelayCounter = 0;
                }
                blinkDir *= -1;
            }
        }


    }
    /**
     * This methods renders a fallobject on the screen depending on the type of it.
     * @param g The image, it draws the object onto.
     * @param  fallObject The fallobject to draw
     */
    private void RenderFallObject(FallObject fallObject, Graphics2D g) {
        switch (fallObject.getType()) {
            case 0:
                g.drawImage(imageAspirin, fallObject.getX() - imageAspirin.getWidth(null) / 2, fallObject.getY() - imageAspirin.getHeight(null) - 1, null);
                break;
            case 1:
                g.setColor(Color.RED);
                g.drawImage(imageJager, fallObject.getX() - imageAspirin.getWidth(null) / 2, fallObject.getY() - imageAspirin.getHeight(null) - 1, null);
                break;
            case 2:
                g.setColor(Color.DARK_GRAY);
                g.drawImage(imageUnicum, fallObject.getX() - imageAspirin.getWidth(null) / 2, fallObject.getY() - imageAspirin.getHeight(null) - 1, null);
                break;
            case 3:
                g.setColor(Color.CYAN);
                g.drawImage(imageTatratea, fallObject.getX() - imageAspirin.getWidth(null) / 2, fallObject.getY() - imageAspirin.getHeight(null) - 1, null);
                break;
            case 4:
                g.setColor(Color.BLUE);
                g.drawImage(imageTatratea, fallObject.getX() - imageAspirin.getWidth(null) / 2, fallObject.getY() - imageAspirin.getHeight(null) - 1, null);
                break;
            default:
                g.setColor(Color.RED);
                break;
        }

//        g.fillOval(fallObject.getX(), fallObject.getY(), FALLOBJECT_WIDTH, FALLOBJECT_HEIGHT);
    }
    /**
     * This methods renders the player on the screen.
     * @param g The image, it draws the object onto.
     * @param  player The player to render.
     */
    private void RenderPlayer(Player player, Graphics2D g) {
        g.drawImage(imagePlayer, player.getX() - imagePlayer.getWidth(null) / 2 + 30, player.getY() - imagePlayer.getHeight(null) / 2, null);
//        g.fillRect(player.getX(), player.getY(), PLAYER_WIDTH, PLAYER_HEIGHT);
    }
    /**
     * This methods applies blur to the given image.
     * @param buf The image, it blurs.
     */
    public BufferedImage blur(BufferedImage buf) {
        BufferedImage bufDest = new BufferedImage(buf.getWidth(), buf.getHeight(), buf.getType());
        float data[] = {0.0725f, 0.125f, 0.0725f, 0.125f, 0.25f, 0.125f,
                0.0725f, 0.125f, 0.0725f, 0.125f, 0.0725f, 0.125f, 0.0725f, 0.125f, 0.0725f, 0.125f};
        Kernel kernel = new Kernel(4, 4, data);
        ConvolveOp convolve = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP,
                null);
        convolve.filter(buf, bufDest);
        return bufDest;
    }

    /**
     * This methods paints the rendered graphics.
     * @param g The graphics to show.
     */
    public void paint(Graphics g) {
        BufferedImage bufferedImage = new BufferedImage(Arena.WIDTH, Arena.HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = (Graphics2D) bufferedImage.getGraphics();
        RenderArena(graphics2D);

        if (arena.getPlayer().getAlcoholLevel() > 10) {
            bufferedImage = blur(bufferedImage);
            this.setBlinkEnabled(true);
            this.blinkSpeed = (arena.getPlayer().getAlcoholLevel()) + 40;
        } else {
            this.setBlinkEnabled(false);
        }
        g.drawImage(bufferedImage, 0, 0, null);

        updateLabels();
    }
    /**
     * This methods updates the score labels above the game field once it is invoked.
     */
    private void updateLabels() {
        this.missedLabel.setText(String.valueOf(arena.getPlayer().getMissed()));
        this.alcoholLevelLabel.setText(String.valueOf(arena.getPlayer().getAlcoholLevel()));
        this.scoreLabel.setText(String.valueOf(arena.getPlayer().getPoints()));
    }

    /**
     * This methods sets a handle for the alcoholLevelLabel.
     * @param alcoholLevelLabel The JLabel it stores a reference of.
     */
    public void setAlcoholLevelLabel(JLabel alcoholLevelLabel) {
        this.alcoholLevelLabel = alcoholLevelLabel;
    }

    /**
     * This methods sets a handle for the missedLabel.
     * @param missedLabel The JLabel it stores a reference of.
     */
    public void setMissedLabel(JLabel missedLabel) {
        this.missedLabel = missedLabel;
    }
    /**
     * This methods sets a handle for the scoreLabel.
     * @param scoreLabel The JLabel it stores a reference of.
     */
    public void setScoreLabel(JLabel scoreLabel) {
        this.scoreLabel = scoreLabel;
    }
    /**
     * Setter for the blink enabled property.
     * @param blinkEnabled
     */
    public void setBlinkEnabled(boolean blinkEnabled) {
        this.blinkEnabled = blinkEnabled;
        if (!blinkEnabled) {
            blinkCounter = 0;
            blinkCounter = 0;
            blinkDir = 1;
        }
    }

    @Override
    public void run() {

    }
}
