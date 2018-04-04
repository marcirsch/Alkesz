package model;

import view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Arena {
    public static final int HEIGHT = 600 / 25 * 18;
    public static final int WIDTH = 700;

    private Player player;
    private List<FallObject> fallObjectList = new ArrayList<>();

    private Random random = new Random(System.currentTimeMillis());


    public Arena(Player player) {
        this.player = player;

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
