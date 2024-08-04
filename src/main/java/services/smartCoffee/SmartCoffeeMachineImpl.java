package services.smartCoffee;

import io.grpc.stub.StreamObserver;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SmartCoffeeMachineImpl extends SmartCoffeeMachineGrpc.SmartCoffeeMachineImplBase {

    private final Connection conn;


    public SmartCoffeeMachineImpl(Connection conn) {
        this.conn = conn;
    }

    // External Grpc methods
    @Override
    public void brewCoffee(BrewCoffeeRequest request, StreamObserver<ActionResponse> responseObserver) {
        boolean brewed = brewCoffeeInternal(request);

        ActionResponse response = ActionResponse.newBuilder()
                .setSuccess(brewed)
                .setErrorCode(brewed ? ErrorCode.NONE : ErrorCode.SYSTEM_ERROR)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void checkInventory(CheckInventoryRequest request, StreamObserver<InventoryResponse> responseObserver) {
        int quantity = getInventoryQuantity(request.getItem());

        InventoryResponse response = InventoryResponse.newBuilder()
                .setQuantity(quantity)
                .setSuccess(true)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void refillInventory(RefillItemRequest request, StreamObserver<InventoryResponse> responseObserver) {
        int newQuantity = refillInventoryInternal(request.getItem(), request.getQuantity());

        InventoryResponse response = InventoryResponse.newBuilder()
                .setQuantity(newQuantity)
                .setSuccess(true)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    // Interal Logic Methods
    private boolean brewCoffeeInternal(BrewCoffeeRequest request) {
        CoffeeType type = request.getCoffeeType();
        CupSize size = request.getCupSize();
        Strength strength = request.getStrength();

        int beansRequired = 0;
        int waterRequired = 0;
        int milkRequired = 0;

        switch (type) {
            case AMERICANO:
                beansRequired = 10;
                waterRequired = 100;
                break;
            case CORTADO:
                beansRequired = 10;
                milkRequired = 50;
                break;
            case FLAT_WHITE:
                beansRequired = 10;
                milkRequired = 100;
                break;
            default:
                System.out.println("Unknown coffee type");
                return false;
        }

        try {
            conn.setAutoCommit(false);

            if (!checkIngredients(beansRequired, waterRequired, milkRequired)) {
                conn.rollback();
                System.out.println("Not enough ingredients");
                return false;
            }

            updateInventoryQuantity(InventoryItem.COFFEE_BEANS, -beansRequired);
            updateInventoryQuantity(InventoryItem.WATER, -waterRequired);
            if (milkRequired > 0) {
                updateInventoryQuantity(InventoryItem.MILK, -milkRequired);
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException rollbackEx) {
                System.out.println("error occurred while connecting to sql database" + e);
            }
            System.out.println("Error brewing coffee: " + e.getMessage());
            return false;
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                System.out.println("error occurred before committing transaction" + e);
            }
        }
    }

    private int getInventoryQuantity(InventoryItem item) throws SQLException {
        String sql = "SELECT quantity FROM inventoryItem WHERE item = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, item.name());
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("quantity");
            }
        }
        return 0;
    }

    private int refillInventoryInternal(InventoryItem item, int quantity) {
//        int currentQuantity = inventory.getOrDefault(item, 0);
//        int newQuantity = currentQuantity + quantity;
//        inventory.put(item, newQuantity);
//        return newQuantity;
        return 0;
    }

    private boolean checkIngredients(int beansRequired, int waterRequired, int milkRequired) throws SQLException {
        return (getInventoryQuantity(InventoryItem.COFFEE_BEANS) >= beansRequired &&
                getInventoryQuantity(InventoryItem.WATER) >= waterRequired &&
                getInventoryQuantity(InventoryItem.MILK) >= milkRequired);
    }

    private void updateInventoryQuantity(InventoryItem item, int change) throws SQLException {
        String sql = "UPDATE inventoryItem SET quantity = quantity + ? WHERE item = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, change);
            pstmt.setString(2, item.name());
            pstmt.executeUpdate();
        }
    }
}