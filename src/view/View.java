package view;

import controller.GameEngine;

import javax.swing.*;

public class View implements Observer {
    private JFrame jFrame;
    private GameEngine gameEngine;


    public View() {
        jFrame = new JFrame();

        //TODO menu

        jFrame.setBounds(10, 10, 700, 600);
        jFrame.setTitle("Alkesz");
        jFrame.setResizable(false);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setLocationRelativeTo(null);

        jFrame.setVisible(true);

        gameEngine = new GameEngine();
        jFrame.add(gameEngine.getArenaRenderer());
        gameEngine.addObserver(this);

    }

    @Override
    public void update(Subject o) {
        JOptionPane.showMessageDialog(null, "Congratulations! \nYou had: " + gameEngine.getPoints() + " points", "Info", JOptionPane.INFORMATION_MESSAGE);
        gameEngine.ResetGame();
        gameEngine.setPlay(true);
    }
}
