package model;

import java.io.Serializable;

public class FallObject implements Serializable{
    private int x;
    private int y;
    public int id;
    private int velocity;
    private int type;

    public FallObject(int id){
        this.id = id;
    }

    public int getAlcLevelCoef() {
        return alcLevelCoef;
    }

    public void setAlcLevelCoef(int alcLevelCoef) {
        this.alcLevelCoef = alcLevelCoef;
    }

    private int alcLevelCoef = 1;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }


    public int getVelocity() {
        return velocity;
    }

    public void setVelocity(int velocity) {
        this.velocity = velocity;
    }
}
