package model;

import view.View;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Arena implements Serializable {
    public static final int HEIGHT = View.WINDOW_HEIGHT / 25 * 18;
    public static final int WIDTH = View.WINDOW_WIDTH;

    private Player player;
    private List<FallObject> fallObjectList = new ArrayList<>();

    private Random random = new Random(System.currentTimeMillis());


    public Arena() {
        this.player = new Player();

    }


    public Player getPlayer() {
        return player;
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
