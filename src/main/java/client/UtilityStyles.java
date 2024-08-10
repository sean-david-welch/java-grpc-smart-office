package client;

import javax.swing.*;
import java.awt.*;

public class UtilityStyles {
    public static JTextField getjTextField() {
        JTextField textField = new JTextField(10);
        textField.setBackground(new Color(60, 60, 60));
        textField.setForeground(Color.WHITE);
        textField.setFont(new Font("Jetbrains Mono", Font.PLAIN, 12));
        textField.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        textField.setCaretColor(Color.WHITE);
        return textField;
    }

    public static JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Jetbrains Mono", Font.PLAIN, 12));
        return label;
    }

    public static JComboBox<String> getStringJComboBox() {
        String[] timeSlots = generateTimeSlots();

        JComboBox<String> comboBox = new JComboBox<>(timeSlots);
        comboBox.setBackground(new Color(60, 60, 60));
        comboBox.setForeground(Color.WHITE);
        comboBox.setFont(new Font("Jetbrains Mono", Font.PLAIN, 12));
        comboBox.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        return comboBox;
    }

    static String[] generateTimeSlots() {
        String[] timeSlots = new String[10];
        for (int i = 8; i < 18; i++) {
            timeSlots[i - 8] = String.format("%02d:00", i);
        }
        return timeSlots;
    }

}
