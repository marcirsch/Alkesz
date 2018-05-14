package controller;

import model.*;
import network.Client;
import network.Server;
import view.ArenaRenderer;
import view.View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameEngine implements MouseMotionListener {

    private Arena arena;
    private int fallObjectIter = 0;


    private Arena oppArena;
    private ArenaRenderer arenaRenderer;
    private ArenaRenderer oppArenaRenderer;
    private TipsyOffsetGenerator tipsyOffsetGenerator;
    private Client client;
    private Server server;
    public Settings settings;
    public TopList topList;
    private View view;

    public Arena getArena_rx() {
        return arena_rx;
    }


    private volatile Arena arena_rx;

    private boolean play = false;

    private Timer timer; // timer is used for screen update

    private Random random = new Random(System.currentTimeMillis());
    private ArrayList observers = new ArrayList();// used to communicate with view
    private int PlayerXPosition;

    /**
     * Constructor of GameEngine prepares Network, GUI, Arena, settings and timer for run.
     *
     * @param gui Reference to GUI
     */

    public GameEngine(View gui) {


        client = new Client(this);
        server = new Server(this);
        this.view = gui;
        arena = new Arena();
        arena.resetArena();

        oppArena = new Arena();
        oppArena.resetArena();

        settings = new Settings();
        topList = new TopList();

        tipsyOffsetGenerator = new TipsyOffsetGenerator();

        timer = new Timer(getDifficultyDelay(), e -> Update()); // call update every delay milliseconds
    }


    /**
     * Update is called by Timer every set seconds (set by difficulty).
     * This method updates arena, object movements and interactions between them.
     * Updates fall objects and generates new ones.
     * If game mode is set to multiplayer, it either sends data to or receives data from client.
     */
    private void Update() {
        //Restart timer
        timer.setDelay(getDifficultyDelay());
        timer.start();

        //Update tipsyoffset with current alcohol level
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
            this.view.refreshToplist();
            this.ResetGame();

        }


        arena.getPlayer().setX(PlayerXPosition);
        arena.getPlayer().setX(PlayerXPosition + tipsyOffsetGenerator.getValue());
        arenaRenderer.repaint();

        if (settings.getGameMode() == Settings.GAME_MODE.MULTIPLAYER && play) {
            if (settings.getRole() == Settings.SERVER_CLIENT_ROLE.SERVER) {
                server.SendDatatoClient(arena);
            } else {
                client.SendDatatoServer(arena);
            }
        }

    }

    /**
     * In Single player and multi player server mode this method creates new fall objects randomly.
     * Iterates through fall object list and updates Y coordinates using speed of object.
     */
    private void UpdateFallObjects() {
        //create fallObjects randomly
        if (this.settings.getRole() == Settings.SERVER_CLIENT_ROLE.SERVER || this.settings.getGameMode() == Settings.GAME_MODE.SINGLEPLAYER) {
            addNewFallObject();
        }

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

    /**
     * Returns difficulty set in Settings, in form of a divisor used for new fall object generation.
     *
     * @return int divisor
     */
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

    /**
     * Returns difficulty threshold used for Fall object generation. This threshold sets minimum distance between objects
     *
     * @return int threshold for given threshold
     */
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

    /**
     * Returns delay for set difficulty in settings. Used for Timer recalls.
     *
     * @return int difficulty delay
     */
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

    /**
     * Getter method for Client
     *
     * @return Client client
     */
    public Client getClient() {
        return client;
    }

    /**
     * Getter method for Server
     *
     * @return Server server
     */
    public Server getServer() {
        return server;
    }

    /**
     * Determines if player has lost, with set conditions.
     *
     * @return boolean True if lost, False if still in game
     */
    private boolean isLost() {
        return (arena.getPlayer().getMissed() > Settings.MISSED_LOSE_THRESHOLD);
    }

    /**
     * This method adds new fall objects to list held by arena:
     * If generated random number mod(difficultyDiv) ==1 and closest existing object height is higher than threshold.
     * Y coordinate is set to 0, X is set by random number
     */
    private void addNewFallObject() {
//        System.out.println("miny: " + arena.getMinY());
        if (random.nextInt(1000) % getDifficultyDiv() == 1 && arena.getMinY() > getDifficultyTh()) {
            List<FallObject> fallObjectList = arena.getFallObjectList();

            FallObject fallObject = new FallObject(this.fallObjectIter);
            if (this.settings.getRole() == Settings.SERVER_CLIENT_ROLE.SERVER) {
                this.fallObjectIter++;
            }
            fallObject.setY(0);
            fallObject.setVelocity(1);
            fallObject.setX(random.nextInt(Arena.WIDTH));

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

    /**
     * Returns true if collision occurred between player and fallobject
     *
     * @param fallObject Object to be tested
     * @return True if collision occurred. False otherwise.
     */
    private boolean isCollision(FallObject fallObject) {
        Rectangle fallObjRect = new Rectangle(fallObject.getX(), fallObject.getY(), ArenaRenderer.FALLOBJECT_WIDTH, ArenaRenderer.FALLOBJECT_HEIGHT);
        Rectangle playerRect = new Rectangle(arena.getPlayer().getX(), arena.getPlayer().getY(), ArenaRenderer.PLAYER_WIDTH, ArenaRenderer.PLAYER_HEIGHT);

        return fallObjRect.intersects(playerRect);
    }

    public void setArena_rx(Arena arena_rx) {
        boolean foNew = true;
        this.arena_rx = arena_rx;
        this.oppArena.setPlayer(this.arena_rx.getPlayer());
        this.oppArena.setFallObjectList(this.arena_rx.getFallObjectList());
        if (this.settings.getRole() == Settings.SERVER_CLIENT_ROLE.CLIENT) {
            for (FallObject fo_rx : arena_rx.getFallObjectList()) {
                foNew = true;
                for (FallObject fo : arena.getFallObjectList()) {
                    if (fo_rx.id == fo.id || fo_rx.getY() > 20) {
                        foNew = false;
                    }
                }
                if (foNew) {
                    arena.getFallObjectList().add(fo_rx);
                }
            }
        }
    }

    /**
     * Resets game to start state.
     */
    public void ResetGame() {
        arena.getPlayer().setMissed(0);
        arena.getPlayer().setPoints(0);
        arena.getPlayer().setAlcoholLevel(0);
        arenaRenderer.setBlinkEnabled(false);
        arena.getFallObjectList().clear();
        this.setPlay(true);
    }

    /**
     * Getter method for points.
     *
     * @return int Points
     */
    public int getPoints() {
        return arena.getPlayer().getPoints();
    }

    /**
     * Getter method for play variable.
     *
     * @return boolean True if game play, false otherwise.
     */
    public boolean isPlay() {
        return play;
    }

    /**
     * Setter method for play variable.
     *
     * @param play
     */
    public void setPlay(boolean play) {
        this.play = play;
        if (play) {
            timer.start();
        }
    }

    /**
     * Starts Multi player
     */
    public void startMultiPlayer() {
        this.setPlay(true);

    }

    /**
     * Setup for starting game
     *
     * @param gameMode Multi player or Single player
     */
    public void startGame(Settings.GAME_MODE gameMode) {
        this.settings.setGameMode(gameMode);
        this.setPlay(true);
        if (gameMode == Settings.GAME_MODE.SINGLEPLAYER) {
            this.getArena().WIDTH = View.WINDOW_WIDTH;
            this.view.setMultiplayerView(false);
            arena.getPlayer().setX(Arena.WIDTH / 2);
            arena.getPlayer().setY(Arena.HEIGHT - 30);
            oppArena.getPlayer().setX(Arena.WIDTH / 2);
            oppArena.getPlayer().setY(Arena.HEIGHT - 30);
            arena.resetArena();
            oppArena.resetArena();
        }

        if (gameMode == Settings.GAME_MODE.MULTIPLAYER) {
            this.view.showPage(View.PAGENAME.GAME);
            this.getArena().WIDTH = View.WINDOW_WIDTH / 2;
            this.view.setMultiplayerView(true);
            arena.resetArena();
            oppArena.resetArena();
            arena.getPlayer().setX(Arena.WIDTH / 2);
            arena.getPlayer().setY(Arena.HEIGHT - 50);
            oppArena.getPlayer().setX(Arena.WIDTH / 2);
            oppArena.getPlayer().setY(Arena.HEIGHT - 50);

            fallObjectIter = -1;
        }
    }

    /**
     * Stops gameplay for Single or Multi player mode
     *
     * @param gameMode game mode
     */
    public void stopGame(Settings.GAME_MODE gameMode) {
        arena.resetArena();
        oppArena.resetArena();
        this.ResetGame();
        this.setPlay(false);
        if (gameMode == Settings.GAME_MODE.MULTIPLAYER) {
            view.showPage(View.PAGENAME.MULTIPLAYERSETTINGS);
            if (settings.getRole() == Settings.SERVER_CLIENT_ROLE.SERVER) {
                server.stop_receive();
            } else {
                client.stop_receive();
            }

        }
    }

    /**
     * Mouse drag event listener
     *
     * @param mouseEvent
     */
    @Override
    public void mouseDragged(MouseEvent mouseEvent) {

    }

    /**
     * Mouse moved event listener. Used to update player position
     *
     * @param mouseEvent
     */
    @Override
    public void mouseMoved(MouseEvent mouseEvent) {
//        arena.getPlayer().setX(mouseEvent.getX());
        PlayerXPosition = mouseEvent.getX();
    }


    /**
     * Getter method for arena renderer
     *
     * @return ArenaRenderer ArenaRenderer of current Arena
     */
    public ArenaRenderer getArenaRenderer() {
        return arenaRenderer;
    }

    /**
     * Setter method for ArenaRenderer
     *
     * @param arenaRenderer arenarenderer
     */
    public void setArenaRenderer(ArenaRenderer arenaRenderer) {
        this.arenaRenderer = arenaRenderer;
        this.arenaRenderer.addMouseMotionListener(this);
    }

    /**
     * Getter method for Arena
     *
     * @return Arena arena
     */
    public Arena getArena() {
        return arena;
    }

    /**
     * This method starts network thread as client.
     */
    public void startClient() {
        if (this.client.ConnectToServer(settings.getRemoteIPAddress())) {
            this.client.start_receive();
            new Thread(this.client::receive_loop).start();
        }
    }

    /**
     * This method starts network thread as server.
     */
    public void startServer() {
        if (this.server.StartServer()) {
            this.server.start_receive();
            new Thread(this.server::receive_loop).start();
        }
    }

    /**
     * Getter method for opponent arenarenderer.
     *
     * @return ArenaRenderer oppArenaRenderer
     */
    public ArenaRenderer getOppArenaRenderer() {
        return oppArenaRenderer;
    }

    /**
     * Setter method for opponent arena renderer
     *
     * @param oppArenaRenderer oppArenaRenderer
     */
    public void setOppArenaRenderer(ArenaRenderer oppArenaRenderer) {
        this.oppArenaRenderer = oppArenaRenderer;
    }

    /**
     * Getter method for opponent arena
     *
     * @return Arena arena
     */
    public Arena getOppArena() {
        return oppArena;
    }

    /**
     * Setter method for opponent Arena
     *
     * @param oppArena oppArena
     */
    public void setOppArena(Arena oppArena) {
        this.oppArena = oppArena;
    }
}
