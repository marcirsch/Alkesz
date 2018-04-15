package controller;

import model.*;
import view.ArenaRenderer;
import view.Observer;
import view.Subject;
import model.TopList;
import view.View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import java.util.Random;

public class GameEngine implements MouseMotionListener, Subject {

    private Arena arena;
    private Player player;
    private ArenaRenderer arenaRenderer;
    private TipsyOffsetGenerator tipsyOffsetGenerator;

    public Settings settings;
    public TopList topList;


    private boolean play = false;

    private Timer timer; // timer is used for screen update

    private Random random = new Random(System.currentTimeMillis());
    private ArrayList observers = new ArrayList();// used to communicate with view
    private int PlayerXPosition;

    public GameEngine() {
        player = new Player();
        player.setX(Arena.WIDTH / 2);
        player.setY(Arena.HEIGHT - 30);

        arena = new Arena(player);
        settings = new Settings();
        topList = new TopList();

        tipsyOffsetGenerator = new TipsyOffsetGenerator();

        timer = new Timer(getDifficultyDelay(), e -> Update()); // call update every delay milliseconds
    }


    private void Update() {
        timer.setDelay(getDifficultyDelay());
        timer.start();

        tipsyOffsetGenerator.setAlcoholLevel(player.getAlcoholLevel());


        if (play) {
            UpdateFallObjects();
        }

        if (isLost()) {
            play = false;
            if (topList.getMinScore() < getArena().getPlayer().getPoints() || topList.getItemsInList()<5){
                String name = JOptionPane.showInputDialog("Congratulations! \n"+
                        "You had got into the top 5 with " + getPoints() + " points\n"+
                        "Please enter your name:");
                topList.add(getArena().getPlayer().getPoints(),name);
            }else {
                JOptionPane.showMessageDialog(null, "Congratulations! \nYou had: " + getPoints() + " points", "Info", JOptionPane.INFORMATION_MESSAGE);
            }
            ((View) observers.get(0)).refreshToplist();
            notifyObservers();
        }


        player.setX(PlayerXPosition);
        player.setX(PlayerXPosition + tipsyOffsetGenerator.getValue());
        arenaRenderer.repaint();
    }


    private void UpdateFallObjects() {
        //create fallObjects randomly
        addNewFallObject();

        try {
            for (FallObject fallObject : arena.getFallObjectList()) {
                fallObject.setY(fallObject.getY() + fallObject.getVelocity());

                boolean deleteObject = false;

                if (isCollision(fallObject)) {
                    player.setPoints(player.getPoints() + 1);
                    player.setAlcoholLevel(player.getAlcoholLevel() + 1);
                    deleteObject = true;
                    System.out.println("Player got point! points: " + player.getPoints());
                } else if (fallObject.getY() > Arena.HEIGHT) {
                    player.setMissed(player.getMissed() + 1);
                    deleteObject = true; // set caught to remove from list
                }


                if (deleteObject) {
                    arena.getFallObjectList().remove(fallObject);
                    System.out.println("Fallobject got removed");
                }

            }
        } catch (Exception e) {
//            System.out.println("Empty list GE" + e.getMessage());
        }
    }


    private int getDifficultyDiv() {
        int divisor = 1;
        switch (settings.getDifficulty()) {
            case EASY:
                divisor = 50;
                break;
            case MEDIUM:
                divisor = 10;
                break;
            case HARD:
                divisor = 2;
                break;
            default:
                break;
        }
        return divisor;
    }

    private int getDifficultyTh() {
        int threshold = 1;
        switch (settings.getDifficulty()) {
            case EASY:
                threshold = 150;
                break;
            case MEDIUM:
                threshold = 100;
                break;
            case HARD:
                threshold = 75;
                break;
            default:
                break;
        }
        return threshold;
    }

    private int getDifficultyDelay() {
        int delay = 1;
        switch (settings.getDifficulty()) {
            case EASY:
                delay = 10;
                break;
            case MEDIUM:
                delay = 8;
                break;
            case HARD:
                delay = 6;
                break;
            default:
                break;
        }
        return delay;
    }


    private boolean isLost() {
        return (player.getMissed() > Settings.MISSED_LOSE_THRESHOLD);
    }

    private void addNewFallObject() {
//        System.out.println("miny: " + arena.getMinY());
        if (random.nextInt(1000) % getDifficultyDiv() == 1 && arena.getMinY() > getDifficultyTh()) {
            List<FallObject> fallObjectList = arena.getFallObjectList();

            FallObject fallObject = new FallObject();
            fallObject.setY(0);
            fallObject.setX(random.nextInt(Arena.WIDTH));
            fallObject.setVelocity(1);
            fallObject.setType(random.nextInt(5));

            fallObjectList.add(fallObject);
        }
    }

    private boolean isCollision(FallObject fallObject) {
        Rectangle fallObjRect = new Rectangle(fallObject.getX(), fallObject.getY(), ArenaRenderer.FALLOBJECT_WIDTH, ArenaRenderer.FALLOBJECT_HEIGHT);
        Rectangle playerRect = new Rectangle(player.getX(), player.getY(), ArenaRenderer.PLAYER_WIDTH, ArenaRenderer.PLAYER_HEIGHT);
        return fallObjRect.intersects(playerRect);
    }


    public void ResetGame() {
        player.setMissed(0);
        player.setPoints(0);
        player.setAlcoholLevel(0);
        arenaRenderer.setBlinkEnabled(false);
        arena.getFallObjectList().clear();
    }

    public int getPoints() {
        return player.getPoints();
    }

    public boolean isPlay() {
        return play;
    }

    public void setPlay(boolean play) {
        this.play = play;
        if (play) {
            timer.start();
        }
    }


    @Override
    public void mouseDragged(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseMoved(MouseEvent mouseEvent) {
//        player.setX(mouseEvent.getX());
        PlayerXPosition = mouseEvent.getX();
    }


    public ArenaRenderer getArenaRenderer() {
        return arenaRenderer;
    }

    public void setArenaRenderer(ArenaRenderer arenaRenderer) {
        this.arenaRenderer = arenaRenderer;
        this.arenaRenderer.addMouseMotionListener(this);
    }

    public Arena getArena() {
        return arena;
    }

    @Override
    public void addObserver(Observer o) {
        try {
            observers.add(o);
        } catch (Exception e) {
            System.out.println("Failed to add to Observer list");
        }
    }

    @Override
    public void removeObserver(Observer o) {
        observers.remove(o);
    }

    private void notifyObservers() {
        // loop through and notify each observer
        Iterator i = observers.iterator();
        while (i.hasNext()) {
            Observer o = (Observer) i.next();
            o.update(this);
        }
    }
}
