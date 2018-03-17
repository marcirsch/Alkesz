package view;

import controller.GameEngine;

import javax.swing.*;

public class View {
    JFrame jFrame;
    GameEngine gameEngine;

    public View() {
        jFrame = new JFrame();
        gameEngine = new GameEngine();

        jFrame.setBounds(10, 10, 700, 600);
        jFrame.setTitle("Alkesz");
        jFrame.setResizable(false);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setLocationRelativeTo(null);

        jFrame.setVisible(true);

        jFrame.add(gameEngine.getArenaRenderer());
    }
}
