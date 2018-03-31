package controller;

import model.*;
import view.ArenaRenderer;
import view.Observer;
import view.Subject;
import model.TopList;
import view.View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class GameEngine implements MouseMotionListener, Subject {

    private Arena arena;
    private Player player;

    public TopList topList;

    private ArenaRenderer arenaRenderer;

    private Timer timer; // timer is used for screen update
    private int delay = 10;

    private Random random = new Random(System.currentTimeMillis());
    private boolean play = false;

    private ArrayList observers = new ArrayList();// used to communicate with view

    public Settings settings;

    public GameEngine() {
        player = new Player();
        player.setX(Arena.WIDTH / 2);
        player.setY(Arena.HEIGHT - 30);

        arena = new Arena(player);
        settings = new Settings();
        topList = new TopList();

        timer = new Timer(delay, e -> Update()); // call update every delay milliseconds
    }


    private void Update() {
        timer.start();
        if (play) {
            UpdateFallObjects();
        }

        arenaRenderer.setScore(player.getPoints());
        arenaRenderer.setMissed(player.getMissed());
        arenaRenderer.setAlcoholLevel(player.getAlcoholLevel());
        arenaRenderer.repaint();
    }


    private void UpdateFallObjects() {
        //create fallObjects randomly
        if (random.nextInt(1000) % 100 == 1) {

            FallObject fallObject = new FallObject();
            fallObject.setY(0);
            fallObject.setX(random.nextInt(Arena.WIDTH));
            fallObject.setVelocity(1);
            fallObject.setType(random.nextInt(5));

            arena.getFallObjectList().add(fallObject);
        }


        try {
            for (FallObject fallObject : arena.getFallObjectList()) {
                fallObject.setY(fallObject.getY() + fallObject.getVelocity());
//            System.out.println("current y: " + fallObject.getY() + " velocity: " + fallObject.getVelocity());
                boolean caught = false;

                if (new Rectangle(fallObject.getX(), fallObject.getY(), ArenaRenderer.FALLOBJECT_WIDTH, ArenaRenderer.FALLOBJECT_HEIGHT).intersects(new Rectangle(player.getX(), player.getY(), ArenaRenderer.PLAYER_WIDTH, ArenaRenderer.PLAYER_HEIGHT))) {
                    player.setPoints(player.getPoints() + 1);
                    caught = true;
                    System.out.println("Player got point! points: " + player.getPoints());
                }


                if (fallObject.getY() > Arena.HEIGHT && !caught) {
                    player.setMissed(player.getMissed() + 1);
                    caught = true; // set caught to remove from list

                    if (player.getMissed() > 5) {
                        play = false;

//                        TODO Leellenőrizni, hogy benne van e a legjobb 5ben, és ha igen akkor egy inputmezőt felhozni, hogy beírja a nevét
                        topList.add(player.getPoints(),"G.I.JOE");
                        ((View)observers.get(0)).refreshToplist();
                        notifyObservers();
                        break;
                    }
                }


                if (caught) {
                    arena.getFallObjectList().remove(fallObject);
                    System.out.println("Fallobject got removed");
                }

            }
        } catch (Exception e) {
//            System.out.println("Empty list GE" + e.getMessage());
        }
    }

    public void ResetGame(){
        player.setMissed(0);
        player.setPoints(0);
        player.setAlcoholLevel(0);

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
        if (play==true){
            timer.start();
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
        }catch (Exception e){
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
