package services.smartCoffee;

import io.grpc.stub.StreamObserver;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

public class SmartCoffeeMachineImpl extends SmartCoffeeMachineGrpc.SmartCoffeeMachineImplBase {

    private final Map<InventoryItem, Integer> inventory;

    public SmartCoffeeMachineImpl(Connection conn) {
        inventory = new HashMap<>();
        inventory.put(InventoryItem.MILK, 1000);
        inventory.put(InventoryItem.WATER, 2000);
        inventory.put(InventoryItem.COFFEE_BEANS, 500);
    }

    @Override
    public void brewCoffee(BrewCoffeeRequest request, StreamObserver<ActionResponse> responseObserver) {
        boolean brewed = brewCoffeeLogic(request);

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
        int newQuantity = refillInventoryLogic(request.getItem(), request.getQuantity());

        InventoryResponse response = InventoryResponse.newBuilder()
                .setQuantity(newQuantity)
                .setSuccess(true)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    private boolean brewCoffeeLogic(BrewCoffeeRequest request) {

        CoffeeType type = request.getCoffeeType();
        CupSize size = request.getCupSize();
        Strength strength = request.getStrength();

        inventory.put(InventoryItem.COFFEE_BEANS, inventory.get(InventoryItem.COFFEE_BEANS) - 10);
        inventory.put(InventoryItem.WATER, inventory.get(InventoryItem.WATER) - 100);
        if (type != CoffeeType.AMERICANO) {
            inventory.put(InventoryItem.MILK, inventory.get(InventoryItem.MILK) - 50);
        }

        return true;
    }

    private int getInventoryQuantity(InventoryItem item) {
        return inventory.getOrDefault(item, 0);
    }

    private int refillInventoryLogic(InventoryItem item, int quantity) {
        int currentQuantity = inventory.getOrDefault(item, 0);
        int newQuantity = currentQuantity + quantity;
        inventory.put(item, newQuantity);
        return newQuantity;
    }
}