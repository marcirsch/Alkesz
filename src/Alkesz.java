import view.View;

import javax.swing.*;

/**
 * Alkesz main class
 */

public class Alkesz {


    public static void main(String[] args) {


        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                View view = new View();

            }
        });
    }
}
