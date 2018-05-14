package model;

import view.View;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Arena implements Serializable {
    public static int HEIGHT = View.WINDOW_HEIGHT / 25 * 18;
    public static int WIDTH = View.WINDOW_WIDTH;

    private Player player;
    private List<FallObject> fallObjectList = new ArrayList<>();

    /**
     * Constructor of arena, creates new player.
     */
    public Arena() {
        this.player = new Player();
    }

    /**
     * Sets player to starting position and clears fall object list.
     */
    public void resetArena(){
        this.player.setX(Arena.WIDTH / 2);
        this.player.setY(Arena.HEIGHT - 30);
        this.fallObjectList.clear();
    }

    /**
     * Setter method for player.
     * @param player reference to player
     */
    public void setPlayer(Player player) {
        this.player = player;
    }

    /**
     * Getter method for player
     * @return player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Getter method for fall object list
     * @return fall object list
     */
    public List<FallObject> getFallObjectList() {
        return fallObjectList;
    }

    /**
     * Setter method for fall object list
     * @param fallObjectList
     */
    public void setFallObjectList(List<FallObject> fallObjectList) {
        this.fallObjectList = fallObjectList;
    }

    /**
     * Returns Y coordinate of last generated object.
     * @return int Y coordinate
     */
    public int getMinY() {
        int minY = View.WINDOW_HEIGHT;

        if (fallObjectList.isEmpty()) {
            return minY;
        }

        for (FallObject i : fallObjectList) {
            int y = i.getY();
            if (y < minY && y != 0) {
                minY = y;
            }
        }
        return minY;
    }
}
