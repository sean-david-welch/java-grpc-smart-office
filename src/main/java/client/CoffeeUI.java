package client;

import services.smartCoffee.CoffeeType;

import javax.swing.*;
import java.awt.*;

public class CoffeeUI extends JPanel {
    public CoffeeUI(GrpcClient grpcClient, ClientUI parent) {
        setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.WHITE), "Coffee Machine", 0, 0, null, Color.WHITE));
        setLayout(new GridLayout(2, 2, 5, 5));
        setBackground(new Color(40, 40, 40));

        JLabel coffeeTypeLabel = createStyledLabel();
        String[] coffeeTypes = {"AMERICANO", "ESPRESSO", "LATTE"};
        JComboBox<String> coffeeTypeComboBox = createStyledComboBox(coffeeTypes);
        add(coffeeTypeLabel);
        add(coffeeTypeComboBox);

        JButton brewCoffeeButton = createStyledButton();
        brewCoffeeButton.addActionListener(e -> {
            String coffeeType = (String) coffeeTypeComboBox.getSelectedItem();
            CoffeeType type = CoffeeType.valueOf(coffeeType);
            String response = grpcClient.brewCoffee(type);
            parent.displayResponse(response, response.contains("Error"));
        });

        add(new JLabel());
        add(brewCoffeeButton);
    }

    private JLabel createStyledLabel() {
        JLabel label = new JLabel("Coffee Type:");
        label.setForeground(Color.WHITE);
        return label;
    }

    private JComboBox<String> createStyledComboBox(String[] items) {
        JComboBox<String> comboBox = new JComboBox<>(items);
        comboBox.setBackground(new Color(60, 60, 60));
        comboBox.setForeground(Color.WHITE);
        comboBox.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        return comboBox;
    }

    private JButton createStyledButton() {
        return StyledButtonUI.getjButton("Brew Coffee");
    }
}
