package network;

import model.FallObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Container implements Serializable{

    private int x;
    private int points = 0;
    private int alcoholLevel = 0;
    private int missed = 0;
    private List<FallObject> fallObjectList = new ArrayList<>();

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getAlcoholLevel() {
        return alcoholLevel;
    }

    public void setAlcoholLevel(int alcoholLevel) {
        this.alcoholLevel = alcoholLevel;
    }

    public int getMissed() {
        return missed;
    }

    public void setMissed(int missed) {
        this.missed = missed;
    }

    public List<FallObject> getFallObjectList() {
        return fallObjectList;
    }

    public void setFallObjectList(List<FallObject> fallObjectList) {
        this.fallObjectList = fallObjectList;
    }
}
