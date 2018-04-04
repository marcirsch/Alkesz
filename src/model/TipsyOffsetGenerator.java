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
    private int counter = 0;

    public TipsyOffsetGenerator() {
        timer = new Timer(delay, e -> update());
        timer.start();
    }

    public int getAlcoholLevel() {
        return alcoholLevel;
    }

    public void setAlcoholLevel(int alcoholLevel) {
        this.alcoholLevel = alcoholLevel;
        if (alcoholLevel == 0) {
            value = 0;
        }
    }

    public int getValue() {
        return value;
    }

    private void update() {
        int threshold = (alcoholLevel / 5) * 10;
        if (value < -1 * threshold || value > threshold) {
            direction *= -1;
        }

        if(alcoholLevel > 5)
        value = value + direction * speed;

    }


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
