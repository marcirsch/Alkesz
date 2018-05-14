package model;

import java.net.InetAddress;
import java.net.UnknownHostException;


public class Settings {
    private GAME_DIFFICULTY difficulty;
    private SERVER_CLIENT_ROLE role;
    private String localIPAddress;
    private String remoteIPAddress;

    private GAME_MODE gameMode = GAME_MODE.SINGLEPLAYER;

    public enum GAME_DIFFICULTY {EASY, MEDIUM, HARD}

    public enum GAME_MODE {SINGLEPLAYER, MULTIPLAYER}

    public enum SERVER_CLIENT_ROLE {SERVER, CLIENT}

    public static final int MISSED_LOSE_THRESHOLD = 5;

    /**
     * Constructor of Settings.
     * Default at start:
     * -Difficulty: Easy
     * -Role: Server
     * -IP: 127.0.0.1
     */
    public Settings() {
        difficulty = GAME_DIFFICULTY.EASY;
        role = SERVER_CLIENT_ROLE.SERVER;
        InetAddress ip;
        try {
            ip = InetAddress.getLocalHost();
            localIPAddress = ip.getHostAddress();
            System.out.println("Current IP address : " + localIPAddress);

        } catch (UnknownHostException e) {

            e.printStackTrace();

        }
    }

    /**
     * Getter method for ip address of local pc
     *
     * @return ip address
     */
    public String getLocalIPAddress() {
        return localIPAddress;
    }

    /**
     * Setter function for ip address of local pc
     *
     * @param ip
     */
    public void setLocalIPAddress(String ip) {
        this.localIPAddress = ip;
    }

    /**
     * Getter method for ip address of remote pc
     *
     * @return remote ip address
     */
    public String getRemoteIPAddress() {
        return remoteIPAddress;
    }

    /**
     * Getter function for Role
     *
     * @return role
     */
    public SERVER_CLIENT_ROLE getRole() {
        return role;
    }


    /**
     * Setter method for game difficulty
     *
     * @param difficulty
     */
    public void setDifficulty(GAME_DIFFICULTY difficulty) {
        this.difficulty = difficulty;
    }

    /**
     * Getter method for game difficulty.
     *
     * @return difficulty
     */
    public GAME_DIFFICULTY getDifficulty() {
        return difficulty;
    }

    /**
     * Setter method for ip address of remote pc
     *
     * @param remoteIPAddress
     */
    public void setRemoteIPAddress(String remoteIPAddress) {
        this.remoteIPAddress = remoteIPAddress;
    }

    /**
     * Setter method for role
     *
     * @param role
     */
    public void setRole(SERVER_CLIENT_ROLE role) {
        this.role = role;
    }

    /**
     * Getter method for game mode
     *
     * @return
     */
    public GAME_MODE getGameMode() {
        return gameMode;
    }

    /**
     * Setter function for game mode
     *
     * @param gameMode
     */
    public void setGameMode(GAME_MODE gameMode) {
        this.gameMode = gameMode;
    }

}
