package BrickBreaker;

import java.awt.*;

public class MapGenerator {
    public int map[][];
    public int brickWidth;
    public int brickHeight;


    private int screenWidth = 540;
    private int screenHeight = 150;


    public MapGenerator(int row, int col) {
        map = new int[row][col];

        for (int row_n = 0; row_n < map.length; row_n++) {
            for (int col_n = 0; col_n < map[0].length; col_n++) {
                map[row_n][col_n] = 1;
            }
        }

        brickHeight = screenHeight / row;
        brickWidth = screenWidth / col;
    }

    public void draw(Graphics2D g2) {
        for (int row_n = 0; row_n < map.length; row_n++) {
            for (int col_n = 0; col_n < map[0].length; col_n++) {
                if (map[row_n][col_n] > 0) {
                    g2.setColor(Color.LIGHT_GRAY);
                    g2.fillRect(col_n * brickWidth + 80, row_n * brickHeight + 50, brickWidth, brickHeight);

                    g2.setStroke(new BasicStroke(3));
                    g2.setColor(Color.BLACK);
                    g2.drawRect(col_n * brickWidth + 80, row_n * brickHeight + 50, brickWidth, brickHeight);
                }
            }
        }
    }

}
