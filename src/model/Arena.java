package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Arena {
    public static final int HEIGHT = 600;
    public static final int WIDTH = 700;

    private Player player;
    private List<FallObject> fallObjectList = new ArrayList<FallObject>();

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
}
