package client.UI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class UtilityStyles {
    public static JTextField getjTextField() {
        JTextField textField = new JTextField(1);
        textField.setBackground(new Color(60, 60, 60));
        textField.setForeground(Color.WHITE);
        textField.setFont(new Font("Jetbrains Mono", Font.PLAIN, 12));
        textField.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        textField.setCaretColor(Color.WHITE);
        textField.setHorizontalAlignment(JTextField.CENTER);
        return textField;
    }

    public static JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Jetbrains Mono", Font.PLAIN, 12));
        label.setBorder(new EmptyBorder(5, 15, 5, 15));
        return label;
    }

    public static JComboBox<String> getStringJComboBox(String[] items) {
        JComboBox<String> comboBox = new JComboBox<>(items);
        comboBox.setBackground(Color.WHITE);
        comboBox.setForeground(Color.BLACK);
        comboBox.setFont(new Font("Jetbrains Mono", Font.PLAIN, 12));
        comboBox.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        return comboBox;
    }

    static String[] generateTimeSlots() {
        String[] timeSlots = new String[17];
        for (int i = 8; i < 25; i++) {
            timeSlots[i - 8] = String.format("%02d:00", i);
        }
        return timeSlots;
    }
}
