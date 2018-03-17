package view;

import model.Arena;
import model.FallObject;
import model.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class ArenaRenderer extends JPanel {

    public static final int PLAYER_WIDTH = 30;
    public static final int PLAYER_HEIGHT = 30;
    public static final int FALLOBJECT_WIDTH = 10;
    public static final int FALLOBJECT_HEIGHT = 10;


    private Arena arena;


    public ArenaRenderer(Arena arena) {
        this.arena = arena;

        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
    }

    private void Render(Graphics2D g) {

        //draw player
        g.setColor(Color.GREEN);
        Player player = arena.getPlayer();
        g.fillRect(player.getX(), player.getY(), PLAYER_WIDTH, PLAYER_HEIGHT);

        //draw falling object
        g.setColor(Color.YELLOW);
        try {
            for (FallObject fallObject : arena.getFallObjectList()) {
                RenderFallObject(fallObject, g);

            }
        } catch (Exception e) {
//            System.out.println("empty list AR");
        }


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

    public void paint(Graphics g) {
        BufferedImage bufferedImage = new BufferedImage(Arena.WIDTH, Arena.HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = (Graphics2D) bufferedImage.getGraphics();
        this.Render(graphics2D);

        g.drawImage(bufferedImage, 0, 0, null);
    }

}
