package model;

import view.View;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Arena implements Serializable {
    public static int HEIGHT = View.WINDOW_HEIGHT / 25 * 18;
    public static int WIDTH = View.WINDOW_WIDTH;



    private Player player;

    public void setFallObjectList(List<FallObject> fallObjectList) {
        this.fallObjectList = fallObjectList;
    }

    private List<FallObject> fallObjectList = new ArrayList<>();

    private Random random = new Random(System.currentTimeMillis());


    public Arena() {
        this.player = new Player();
    }

    public void resetArena(){
        this.player.setX(Arena.WIDTH / 2);
        this.player.setY(Arena.HEIGHT - 30);
        this.fallObjectList.clear();
    }

    public Player getPlayer() {
        return player;
    }
    public void setPlayer(Player player) {
        this.player = player;
    }

    public List<FallObject> getFallObjectList() {
        return fallObjectList;
    }

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
