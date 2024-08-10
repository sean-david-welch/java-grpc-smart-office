package client.UI;

import client.services.GrpcClient;
import services.smartCoffee.CoffeeType;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

import static client.UI.StyledButtonUI.getjButton;
import static client.UI.UtilityStyles.createStyledLabel;
import static client.UI.UtilityStyles.getStringJComboBox;

public class CoffeeUI extends JPanel {
    public CoffeeUI(GrpcClient grpcClient, ClientUI parent) {
        setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.WHITE), "Coffee Machine", 0, 0, null, Color.WHITE));
        setLayout(new GridLayout(2, 2, 5, 5));
        setBackground(new Color(40, 40, 40));


        JLabel coffeeTypeLabel = createStyledLabel("Coffee Type:");
        coffeeTypeLabel.setBorder(new EmptyBorder(5, 15, 5, 15));
        CoffeeType[] coffeeTypes = CoffeeType.values();
        String[] coffeeTypeNames = new String[coffeeTypes.length];
        for (int i = 0; i < coffeeTypes.length; i++) {
            coffeeTypeNames[i] = coffeeTypes[i].name();
        }
        JComboBox<String> coffeeTypeComboBox = getStringJComboBox(coffeeTypeNames);
        add(coffeeTypeLabel);
        add(coffeeTypeComboBox);

        JButton brewCoffeeButton = getjButton("Brew Coffee");
        brewCoffeeButton.addActionListener(e -> {
            String coffeeType = (String) coffeeTypeComboBox.getSelectedItem();
            CoffeeType type = CoffeeType.valueOf(coffeeType);
            String response = grpcClient.brewCoffee(type);
            parent.displayResponse(response, response.contains("Error"));
        });

        add(new JLabel());
        add(brewCoffeeButton);
    }
}
