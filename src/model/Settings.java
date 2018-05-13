package model;

import java.net.InetAddress;
import java.net.UnknownHostException;


public class Settings {
    private GAME_DIFFICULTY difficulty = GAME_DIFFICULTY.EASY;
    private SERVER_CLIENT_ROLE role;
    private String localIPAddress;
    private String remoteIPAddress;

    public GAME_MODE getGameMode() {
        return gameMode;
    }

    public void setGameMode(GAME_MODE gameMode) {
        this.gameMode = gameMode;
    }

    private GAME_MODE gameMode = GAME_MODE.SINGLEPLAYER;

    public enum GAME_DIFFICULTY {EASY, MEDIUM, HARD}
    public enum GAME_MODE {SINGLEPLAYER, MULTIPLAYER}
    public enum SERVER_CLIENT_ROLE {SERVER, CLIENT}

    public static final int MISSED_LOSE_THRESHOLD = 5;

    public Settings() {
        difficulty = GAME_DIFFICULTY.EASY;
        role = SERVER_CLIENT_ROLE.SERVER;
        InetAddress ip;
        try {
//TODO Refresh
            ip = InetAddress.getLocalHost();
            localIPAddress = ip.getHostAddress();
            System.out.println("Current IP address : " + localIPAddress);

        } catch (UnknownHostException e) {

            e.printStackTrace();

        }
    }

    public String getLocalIPAddress() {
        return localIPAddress;
    }

    public String getRemoteIPAddress() {
        return remoteIPAddress;
    }


    public SERVER_CLIENT_ROLE getRole() {
        return role;
    }

    public void setLocalIPAddress(String ip) {
        this.localIPAddress = ip;
    }

    public void setDifficulty(GAME_DIFFICULTY difficulty) {
        this.difficulty = difficulty;
    }

    public GAME_DIFFICULTY getDifficulty() {
        return difficulty;
    }


    public void setRemoteIPAddress(String remoteIPAddress) {
        this.remoteIPAddress = remoteIPAddress;
    }

    public void setRole(SERVER_CLIENT_ROLE role) {
        this.role = role;
    }
}
