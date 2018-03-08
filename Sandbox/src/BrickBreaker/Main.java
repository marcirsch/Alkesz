package BrickBreaker;

import javax.swing.*;
import java.awt.*;

public class Main {

    public static void main(String[] args) {

        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame jFrame = new JFrame();
                GamePlay gamePlay = new GamePlay();

                jFrame.setBounds(10, 10, 700, 600);
                jFrame.setTitle("Breakout ball");
                jFrame.setResizable(true);
                jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                jFrame.setLocationRelativeTo(null);
                jFrame.add(gamePlay);



                jFrame.getContentPane().setCursor(new Cursor(Cursor.HAND_CURSOR));

                jFrame.setVisible(true);
            }
        });


    }
}
