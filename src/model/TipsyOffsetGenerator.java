package model;

import javax.swing.*;
import java.util.concurrent.TimeUnit;

public class TipsyOffsetGenerator {
    private Timer timer;
    private int delay = 40;

    private int alcoholLevel;
    private int speed = 1;


    private int direction = 1;
    private int value = 0;

    /**
     * Constructor for tipsy offset.
     * This class is responsible for generating tipsy movement of player after several drinks.
     */
    public TipsyOffsetGenerator() {
        timer = new Timer(delay, e -> update());
        timer.start();
    }

    /**
     * Getter function for alcohol level
     *
     * @return
     */
    public int getAlcoholLevel() {
        return alcoholLevel;
    }

    /**
     * Setter method for alcohol level
     *
     * @param alcoholLevel
     */
    public void setAlcoholLevel(int alcoholLevel) {
        this.alcoholLevel = alcoholLevel;
        if (alcoholLevel == 0) {
            value = 0;
        }
    }

    /**
     * Getter method for offset value
     *
     * @return
     */
    public int getValue() {
        return value;
    }

    /**
     * Update method called by timer
     */
    private void update() {
        int threshold = (alcoholLevel / 5) * 10;
        if (value < -1 * threshold || value > threshold) {
            direction *= -1;
        }

        if (alcoholLevel > 5)
            value = value + direction * speed;

    }

    /**
     * Test main method
     *
     * @param args
     */
    public static void main(String[] args) {
        TipsyOffsetGenerator toge = new TipsyOffsetGenerator();
        toge.setAlcoholLevel(5);

        while (true) {
            System.out.println(toge.getValue());
            try {
                TimeUnit.MILLISECONDS.sleep(30);
            } catch (Exception e) {

            }
        }
    }
}
