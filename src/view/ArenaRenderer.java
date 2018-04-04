package view;

import model.Arena;
import model.FallObject;
import model.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.nio.Buffer;

public class ArenaRenderer extends JPanel {

    public static final int PLAYER_WIDTH = 30;
    public static final int PLAYER_HEIGHT = 30;
    public static final int FALLOBJECT_WIDTH = 10;
    public static final int FALLOBJECT_HEIGHT = 10;

    private JLabel scoreLabel;
    private JLabel alcoholLevelLabel;
    private JLabel missedLabel;
    private Arena arena;


    public ArenaRenderer(Arena arena) {
        this.arena = arena;

        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
    }

    private void RenderArena(Graphics2D g) {

        //draw player
        g.setColor(Color.GREEN);
        Player player = arena.getPlayer();
        g.fillRect(player.getX(), player.getY(), PLAYER_WIDTH, PLAYER_HEIGHT);

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


    }

    private void RenderFallObject(FallObject fallObject, Graphics2D g) {
        switch (fallObject.getType()) {
            case 0:
                g.setColor(Color.GREEN);
                break;//TODO replace with aspirin
            case 1:
                g.setColor(Color.RED);
                break;//TODO replace with a picture of some kind of alcohol
            case 2:
                g.setColor(Color.DARK_GRAY);
                break;//TODO replace with a picture of some kind of alcohol
            case 3:
                g.setColor(Color.CYAN);
                break;//TODO replace with a picture of some kind of alcohol
            case 4:
                g.setColor(Color.BLUE);
                break;//TODO replace with a picture of some kind of alcohol
            default:
                g.setColor(Color.RED);
                break;
        }

        g.fillOval(fallObject.getX(), fallObject.getY(), FALLOBJECT_WIDTH, FALLOBJECT_HEIGHT);
    }

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

    public void paint(Graphics g) {
        BufferedImage bufferedImage = new BufferedImage(Arena.WIDTH, Arena.HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = (Graphics2D) bufferedImage.getGraphics();
        RenderArena(graphics2D);

        if (arena.getPlayer().getAlcoholLevel() > 10) {
            bufferedImage = blur(bufferedImage);
        }


        g.drawImage(bufferedImage, 0, 0, null);

        updateLabels();
    }


    private void updateLabels() {
        this.missedLabel.setText(String.valueOf(arena.getPlayer().getMissed()));
        this.alcoholLevelLabel.setText(String.valueOf(arena.getPlayer().getAlcoholLevel()));
        this.scoreLabel.setText(String.valueOf(arena.getPlayer().getPoints()));
    }


    public void setAlcoholLevelLabel(JLabel alcoholLevelLabel) {
        this.alcoholLevelLabel = alcoholLevelLabel;
    }

    public void setMissedLabel(JLabel missedLabel) {
        this.missedLabel = missedLabel;
    }

    public void setScoreLabel(JLabel scoreLabel) {
        this.scoreLabel = scoreLabel;
    }


}
