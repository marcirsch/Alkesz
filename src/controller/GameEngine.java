package controller;

import model.*;
import network.Client;
import network.Server;
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
    private ArenaRenderer arenaRenderer;
    private TipsyOffsetGenerator tipsyOffsetGenerator;
    private Client client;
    private Server server;
    public Settings settings;
    public TopList topList;


    private boolean play = false;

    private Timer timer; // timer is used for screen update

    private Random random = new Random(System.currentTimeMillis());
    private ArrayList observers = new ArrayList();// used to communicate with view
    private int PlayerXPosition;

    public GameEngine() {

        client = new Client();
        server = new Server();

        arena = new Arena();
        arena.getPlayer().setX(Arena.WIDTH / 2);
        arena.getPlayer().setY(Arena.HEIGHT - 30);

        settings = new Settings();
        topList = new TopList();

        tipsyOffsetGenerator = new TipsyOffsetGenerator();

        timer = new Timer(getDifficultyDelay(), e -> Update()); // call update every delay milliseconds
    }


    private void Update() {
        timer.setDelay(getDifficultyDelay());
        timer.start();
        tipsyOffsetGenerator.setAlcoholLevel(arena.getPlayer().getAlcoholLevel());


        if (play) {
            UpdateFallObjects();
        }

        if (isLost()) {
            play = false;
            if (topList.getMinScore() < getArena().getPlayer().getPoints() || topList.getItemsInList() < 5) {
                String name = JOptionPane.showInputDialog("Congratulations! \n" +
                        "You had got into the top 5 with " + getPoints() + " points\n" +
                        "Please enter your name:");
                topList.add(getArena().getPlayer().getPoints(), name);
            } else {
                JOptionPane.showMessageDialog(null, "Congratulations! \nYou had: " + getPoints() + " points", "Info", JOptionPane.INFORMATION_MESSAGE);
            }
            ((View) observers.get(0)).refreshToplist();
            notifyObservers();
        }


        arena.getPlayer().setX(PlayerXPosition);
        arena.getPlayer().setX(PlayerXPosition + tipsyOffsetGenerator.getValue());
        arenaRenderer.repaint();


        if(settings.getRole() == Settings.SERVER_CLIENT_ROLE.SERVER){
            server.SendDatatoClient(arena);
        }

        else {
            client.SendDatatoServer(arena);
        }

    }


    private void UpdateFallObjects() {
        //create fallObjects randomly
        addNewFallObject();

        try {
            for (FallObject fallObject : arena.getFallObjectList()) {
                fallObject.setY(fallObject.getY() + fallObject.getVelocity());

                boolean deleteObject = false;

                if (isCollision(fallObject)) {
                    arena.getPlayer().setPoints(arena.getPlayer().getPoints() + 1);
                    arena.getPlayer().setAlcoholLevel(arena.getPlayer().getAlcoholLevel() + fallObject.getAlcLevelCoef());
                    deleteObject = true;
                    System.out.println("Player got point! points: " + arena.getPlayer().getPoints());
                } else if (fallObject.getY() > Arena.HEIGHT) {
                    arena.getPlayer().setMissed(arena.getPlayer().getMissed() + 1);
//                    arena.getPlayer().setAlcoholLevel(0);
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


    public Client getClient() {
        return client;
    }

    public Server getServer() {
        return server;
    }

    private boolean isLost() {
        return (arena.getPlayer().getMissed() > Settings.MISSED_LOSE_THRESHOLD);
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

            switch (fallObject.getType()) {
                case 0:
                    fallObject.setAlcLevelCoef(-3);
                    break;
                case 1:
                    fallObject.setAlcLevelCoef(1);
                    break;
                case 2:
                    fallObject.setAlcLevelCoef(2);
                    break;
                case 3:
                    fallObject.setAlcLevelCoef(3);
                    break;
                case 4:
                    fallObject.setAlcLevelCoef(3);
                    break;
                default:
                    fallObject.setAlcLevelCoef(1);
                    break;
            }

            fallObjectList.add(fallObject);
        }
    }

    private boolean isCollision(FallObject fallObject) {
        Rectangle fallObjRect = new Rectangle(fallObject.getX(), fallObject.getY(), ArenaRenderer.FALLOBJECT_WIDTH, ArenaRenderer.FALLOBJECT_HEIGHT);
        Rectangle playerRect = new Rectangle(arena.getPlayer().getX(), arena.getPlayer().getY(), ArenaRenderer.PLAYER_WIDTH, ArenaRenderer.PLAYER_HEIGHT);

        return fallObjRect.intersects(playerRect);
    }


    public void ResetGame() {
        arena.getPlayer().setMissed(0);
        arena.getPlayer().setPoints(0);
        arena.getPlayer().setAlcoholLevel(0);
        arenaRenderer.setBlinkEnabled(false);
        arena.getFallObjectList().clear();
    }

    public int getPoints() {
        return arena.getPlayer().getPoints();
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
//        arena.getPlayer().setX(mouseEvent.getX());
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
