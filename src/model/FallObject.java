package model;

import java.io.Serializable;

public class FallObject implements Serializable {
    private int x;
    private int y;
    public int id;
    private int velocity;
    private int type;
    private int alcLevelCoef = 1;

    /**
     * Constructor of Fallobject
     * @param id Sets id of object
     */
    public FallObject(int id) {
        this.id = id;
    }

    /**
     * Getter method for alcohol level coef
     * @return alcLevelCoef
     */
    public int getAlcLevelCoef() {
        return alcLevelCoef;
    }

    /**
     * Setter method for alcohol level coef
     * @param alcLevelCoef
     */
    public void setAlcLevelCoef(int alcLevelCoef) {
        this.alcLevelCoef = alcLevelCoef;
    }

    /**
     * Getter method for type of object.
     * @return int type
     */
    public int getType() {
        return type;
    }

    /**
     * Setter method for type of object
     * @param type Type to be set.
     */
    public void setType(int type) {
        this.type = type;
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
     * @return int Y
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

    /**
     * Getter function of object velocity.
     * @return int velocity
     */
    public int getVelocity() {
        return velocity;
    }

    /**
     * Setter function for object velocity
     * @param velocity
     */
    public void setVelocity(int velocity) {
        this.velocity = velocity;
    }
}
