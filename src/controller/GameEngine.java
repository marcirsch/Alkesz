package controller;

import model.Arena;
import model.FallObject;
import model.Player;
import view.ArenaRenderer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.Random;

public class GameEngine implements MouseMotionListener {

    private Arena arena;
    private Player player;

    private ArenaRenderer arenaRenderer;

    private Timer timer;
    private int delay = 10;

    private Random random = new Random(System.currentTimeMillis());


    public GameEngine() {
        player = new Player();
        player.setX(Arena.WIDTH / 2);
        player.setY(Arena.HEIGHT - 30);

        arena = new Arena(player);
        arenaRenderer = new ArenaRenderer(arena);
        arenaRenderer.addMouseMotionListener(this);

        timer = new Timer(delay, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                timer.start();
                Update();
                arenaRenderer.repaint();
            }
        });
        timer.start();
    }


    private void Update() {
        if (random.nextInt(1000) % 100 == 1) {

            FallObject fallObject = new FallObject();
            fallObject.setY(0);
            fallObject.setX(random.nextInt(Arena.WIDTH));
            fallObject.setVelocity(1);

            arena.getFallObjectList().add(fallObject);
        }


        int i = 0;
        try {
            for (FallObject fallObject : arena.getFallObjectList()) {
                fallObject.setY(fallObject.getY() + fallObject.getVelocity());
//            System.out.println("current y: " + fallObject.getY() + " velocity: " + fallObject.getVelocity());
                boolean removeFallObject = false;

                if (fallObject.getY() > Arena.HEIGHT) {
                    removeFallObject = true;
                }
                if (new Rectangle(fallObject.getX(), fallObject.getY(), ArenaRenderer.FALLOBJECT_WIDTH, ArenaRenderer.FALLOBJECT_HEIGHT).intersects(new Rectangle(player.getX(), player.getY(), ArenaRenderer.PLAYER_WIDTH, ArenaRenderer.PLAYER_HEIGHT))) {
                    player.setPoints(player.getPoints() + 1);
                    removeFallObject = true;
                    System.out.println("Player got point! points: " + player.getPoints());
                }

                if (removeFallObject) {
                    arena.getFallObjectList().remove(i);
                    System.out.println("Fallobject with id: " + i + " got removed");
                }

                ++i;
            }
        } catch (Exception e) {
            System.out.println("Empty list");
        }


    }


    @Override
    public void mouseDragged(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseMoved(MouseEvent mouseEvent) {
        player.setX(mouseEvent.getX());
    }

    public ArenaRenderer getArenaRenderer() {
        return arenaRenderer;
    }
}
