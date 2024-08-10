package services.smartCoffee;

import io.grpc.Context;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SmartCoffeeMachineImpl extends SmartCoffeeMachineGrpc.SmartCoffeeMachineImplBase {

    private static final System.Logger logger = System.getLogger(SmartCoffeeMachineImpl.class.getName());
    private final Connection conn;

    public SmartCoffeeMachineImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void brewCoffee(BrewCoffeeRequest request, StreamObserver<ActionResponse> responseObserver) {
        Context context = Context.current();
        if (context.getDeadline() != null && context.getDeadline().isExpired()) {
            responseObserver.onError(Status.DEADLINE_EXCEEDED.withDescription("Deadline exceeded").asRuntimeException());
            return;
        }

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
        try {
            if (request.hasItem()) {
                InventoryItem item = request.getItem();
                if (item != InventoryItem.UNKNOWN_ITEM && item != InventoryItem.UNRECOGNIZED) {
                    int quantity = getInventoryQuantity(item);
                    InventoryResponse response = InventoryResponse.newBuilder()
                            .setItem(item)
                            .setQuantity(quantity)
                            .setSuccess(true)
                            .build();
                    responseObserver.onNext(response);
                }
            } else {
                for (InventoryItem item : InventoryItem.values()) {
                    if (item != InventoryItem.UNKNOWN_ITEM && item != InventoryItem.UNRECOGNIZED) {
                        int quantity = getInventoryQuantity(item);
                        InventoryResponse response = InventoryResponse.newBuilder()
                                .setItem(item)
                                .setQuantity(quantity)
                                .setSuccess(true)
                                .build();
                        responseObserver.onNext(response);
                    }
                }
            }
            responseObserver.onCompleted();
        } catch (SQLException e) {
            logger.log(System.Logger.Level.ERROR, "Error checking inventory: {0}", e.getMessage());
            responseObserver.onError(e);
        }
    }

    @Override
    public StreamObserver<RefillItemRequest> refillInventory(final StreamObserver<InventoryResponse> responseObserver) {
        return new StreamObserver<>() {
            private int totalItemsRefilled = 0;
            private InventoryItem lastItem = null;

            @Override
            public void onNext(RefillItemRequest request) {
                try {
                    updateInventoryQuantity(request.getItem(), request.getQuantity());
                    totalItemsRefilled++;
                    lastItem = request.getItem();
                } catch (SQLException e) {
                    logger.log(System.Logger.Level.ERROR, "Error refilling inventory: {0}", e.getMessage());
                }
            }

            @Override
            public void onError(Throwable t) {
                logger.log(System.Logger.Level.ERROR, "Error in refill: {0}", t.getMessage());
                responseObserver.onError(t);
            }

            @Override
            public void onCompleted() {
                InventoryResponse response = InventoryResponse.newBuilder()
                        .setItem(lastItem)
                        .setQuantity(totalItemsRefilled)
                        .setSuccess(true)
                        .build();
                responseObserver.onNext(response);
                responseObserver.onCompleted();
            }
        };
    }

    private boolean brewCoffeeInternal(BrewCoffeeRequest request) {
        CoffeeType type = request.getCoffeeType();

        int beansRequired;
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
                logger.log(System.Logger.Level.INFO, "Unknown coffee type");
                return false;
        }

        try {
            conn.setAutoCommit(false);

            int availableBeans = getInventoryQuantity(InventoryItem.COFFEE_BEANS);
            int availableWater = getInventoryQuantity(InventoryItem.WATER);
            int availableMilk = getInventoryQuantity(InventoryItem.MILK);

            if (!(availableBeans >= beansRequired && availableWater >= waterRequired && availableMilk >= milkRequired)) {
                conn.rollback();
                logger.log(System.Logger.Level.INFO, "Not enough stock, please refill and try again");
                return false;
            }

            updateInventoryQuantity(InventoryItem.COFFEE_BEANS, -beansRequired);
            if (waterRequired > 0) {
                updateInventoryQuantity(InventoryItem.WATER, -waterRequired);
            } else {
                updateInventoryQuantity(InventoryItem.MILK, -milkRequired);
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException rollbackEx) {
                logger.log(System.Logger.Level.ERROR, "Error rolling back transaction: {0}", rollbackEx.getMessage());
            }
            logger.log(System.Logger.Level.ERROR, "Error brewing coffee: {0}", e.getMessage());
            return false;
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                logger.log(System.Logger.Level.ERROR, "Error resetting auto-commit: {0}", e.getMessage());
            }
        }
    }

    private void updateInventoryQuantity(InventoryItem item, int change) throws SQLException {
        String sql = "update inventory_item set quantity = quantity + ? WHERE item = ?";
        logger.log(System.Logger.Level.INFO, "Updating inventory: {0} {1}", new Object[]{change, item.name().toLowerCase()});
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, change);
            statement.setString(2, item.name().toLowerCase());
            statement.executeUpdate();
            getInventoryQuantity(item);
        } catch (SQLException e) {
            logger.log(System.Logger.Level.ERROR, "An error occurred while updating inventory: {0}", e.getMessage());
        }
    }

    private int getInventoryQuantity(InventoryItem item) throws SQLException {
        String sql = "select quantity from inventory_item where item = ?";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, item.name().toLowerCase());
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                return result.getInt("quantity");
            }
        } catch (SQLException e) {
            logger.log(System.Logger.Level.ERROR, "Error querying database: {0}", e.getMessage());
        }
        return 0;
    }
}
