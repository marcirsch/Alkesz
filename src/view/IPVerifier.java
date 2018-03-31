package view;

import javax.swing.*;
import java.awt.*;
import java.util.StringTokenizer;

public class IPVerifier extends InputVerifier {
    public boolean verify(JComponent input) {
        if (input instanceof JFormattedTextField) {
            JFormattedTextField ftf = (JFormattedTextField) input;
            JFormattedTextField.AbstractFormatter formatter = ftf.getFormatter();
            if (formatter != null) {
                String text = ftf.getText();
                StringTokenizer st = new StringTokenizer(text, ".");
                while (st.hasMoreTokens()) {
                    int value = Integer.parseInt((String) st.nextToken());
                    if (value < 0 || value > 255) {
                        input.setInputVerifier(null);
                        JOptionPane.showMessageDialog(new Frame(), "Malformed IP Address!", "Error",
                                JOptionPane.ERROR_MESSAGE);
                        input.setInputVerifier(this);
                        return false;
                    }
                }
                return true;
            }
        }
        return true;
    }

}


