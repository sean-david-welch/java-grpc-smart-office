package client.UI;

import client.services.GrpcClient;
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

    private void checkInventoryUI() {
        JLabel inventoryItemLabel = createStyledLabelWithBorder("Inventory Item:");
        JComboBox<String> inventoryItemComboBox = itemComboBox(inventoryItemLabel);

        JButton checkInventoryButton = getjButton("Check Inventory");
        checkInventoryButton.addActionListener(e -> {
            String item = (String) inventoryItemComboBox.getSelectedItem();
            InventoryItem inventoryItem = InventoryItem.valueOf(item);
            grpcClient.checkInventory(Optional.of(inventoryItem));
        });

        add(new JLabel());
        add(checkInventoryButton);
    }

    private void refillInventoryUI() {
        JLabel refillItemLabel = createStyledLabelWithBorder("Refill Item:");
        JComboBox<String> refillItemComboBox = itemComboBox(refillItemLabel);

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

    private JComboBox<String> itemComboBox(JLabel refillItemLabel) {
        InventoryItem[] refillItems = InventoryItem.values();
        String[] refillItemNames = new String[refillItems.length];
        for (int i = 0; i < refillItems.length; i++) {
            refillItemNames[i] = refillItems[i].name();
        }
        JComboBox<String> refillItemComboBox = getStringJComboBox(refillItemNames);
        add(refillItemLabel);
        add(refillItemComboBox);

        return refillItemComboBox;
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
