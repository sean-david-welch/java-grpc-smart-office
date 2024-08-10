package client.UI;

import client.controllers.GrpcClient;
import services.smartCoffee.CoffeeType;
import services.smartCoffee.InventoryItem;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static client.UI.StyledButtonUI.getjButton;
import static client.UI.UtilityStyles.getStringJComboBox;
import static client.UI.UtilityStyles.getjTextField;

public class CoffeeUI extends JPanel {
    private final GrpcClient grpcClient;
    private final ClientUI parent;

    public CoffeeUI(GrpcClient grpcClient, ClientUI parent) {
        this.grpcClient = grpcClient;
        this.parent = parent;

        setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.WHITE), "Coffee Machine", 0, 0, null, Color.WHITE));
        setLayout(new GridLayout(9, 2, 5, 5));
        setBackground(new Color(40, 40, 40));

        brewCoffeeUI();
        checkInventoryUI();
        refillInventoryUI();
    }

    private void brewCoffeeUI() {
        JLabel coffeeTypeLabel = createStyledLabelWithBorder("Coffee Type:");
        JComboBox<String> coffeeTypeComboBox = createComboBox(CoffeeType.values());
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

    private void checkInventoryUI() {
        JLabel inventoryItemLabel = createStyledLabelWithBorder("Inventory Item:");

        InventoryItem[] inventoryItems = InventoryItem.values();
        String[] inventoryItemNames = new String[inventoryItems.length + 1];
        inventoryItemNames[0] = "All Items";
        for (int i = 0; i < inventoryItems.length; i++) {
            inventoryItemNames[i + 1] = inventoryItems[i].name();
        }
        JComboBox<String> inventoryItemComboBox = getStringJComboBox(inventoryItemNames);
        add(inventoryItemLabel);
        add(inventoryItemComboBox);

        JButton checkInventoryButton = getjButton("Check Inventory");
        checkInventoryButton.addActionListener(e -> {
            String selectedItem = (String) inventoryItemComboBox.getSelectedItem();
            String response;

            if ("All Items".equals(selectedItem)) {
                response = grpcClient.checkInventory(Optional.empty());
            } else {
                InventoryItem inventoryItem = InventoryItem.valueOf(selectedItem);
                response = grpcClient.checkInventory(Optional.of(inventoryItem));
            }

            parent.displayResponse(response, response.contains("Error"));
        });

        add(new JLabel());
        add(checkInventoryButton);
    }

    private void refillInventoryUI() {
        JLabel refillItemLabel = createStyledLabelWithBorder("Refill Item:");
        JComboBox<String> refillItemComboBox = createComboBox(InventoryItem.values());
        add(refillItemLabel);
        add(refillItemComboBox);

        JLabel quantityLabel = createStyledLabelWithBorder("Quantity:");
        JTextField quantityField = getjTextField();
        add(quantityLabel);
        add(quantityField);

        JButton refillButton = getjButton("Refill Inventory");
        refillButton.addActionListener(e -> {
            try {
                String item = (String) refillItemComboBox.getSelectedItem();
                InventoryItem inventoryItem = InventoryItem.valueOf(item);
                int quantity = Integer.parseInt(quantityField.getText());

                Map<InventoryItem, Integer> itemsToRefill = new HashMap<>();
                itemsToRefill.put(inventoryItem, quantity);

                String response = grpcClient.refillInventory(itemsToRefill);
                parent.displayResponse(response, response.contains("Error"));
            } catch (NumberFormatException ex) {
                parent.displayResponse("Error: Please enter a valid numeric quantity", true);
            }
        });

        add(new JLabel());
        add(refillButton);
    }

    private <T extends Enum<T>> JComboBox<String> createComboBox(T[] enumValues) {
        String[] enumNames = new String[enumValues.length];
        for (int i = 0; i < enumValues.length; i++) {
            enumNames[i] = enumValues[i].name();
        }
        return getStringJComboBox(enumNames);
    }

    private JLabel createStyledLabelWithBorder(String text) {
        JLabel label = createStyledLabel(text);
        label.setBorder(new EmptyBorder(5, 15, 5, 15));
        return label;
    }

    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(Color.WHITE);
        return label;
    }
}
