package model;


import java.io.Serializable;

public class Player implements Serializable {
    private int x;
    private int y;

    private int points = 0;
    private int alcoholLevel = 0;

    private int missed = 0;

    /**
     * Getter method for number of missed alcohols.
     * @return
     */
    public int getMissed() {
        return missed;
    }

    /**
     * Setter method for number of missed alcohols.
     * @param missed
     */
    public void setMissed(int missed) {
        this.missed = missed;
    }

    /**
     * Getter method for alcohol level
     * @return
     */
    public int getAlcoholLevel() {
        return alcoholLevel;
    }

    /**
     * Setter method for Alcohol level
     * @param alcoholLevel
     */
    public void setAlcoholLevel(int alcoholLevel) {
        this.alcoholLevel = alcoholLevel;
    }

    /**
     * Getter method for points
     * @return points
     */
    public int getPoints() {
        return points;
    }

    /**
     * Setter method for Points.
     * @param points
     */
    public void setPoints(int points) {
        this.points = points;
    }
    /**
     * Getter method for X coordinate of object.
     * @return int X
     */
    public int getX() {
        return x;
    }
    /**
     * Setter method for X coordinate of object.
     * @param x
     */
    public void setX(int x) {
        this.x = x;
    }
    /**
     * Getter method for Y coordinate of object.
     * @return int y
     */
    public int getY() {
        return y;
    }
    /**
     * Setter method for Y coordinate of object.
     * @param y
     */
    public void setY(int y) {
        this.y = y;
    }
}
