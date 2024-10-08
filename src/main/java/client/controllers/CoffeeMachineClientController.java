package client.controllers;

import io.grpc.Deadline;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import services.smartCoffee.*;

import java.util.Iterator;
import java.util.Map;
import java.util.Optional;

public class CoffeeMachineClientController {
    private final SmartCoffeeMachineGrpc.SmartCoffeeMachineBlockingStub coffeeMachineStub;
    private final SmartCoffeeMachineGrpc.SmartCoffeeMachineStub asyncCoffeeMachineStub;

    public CoffeeMachineClientController(SmartCoffeeMachineGrpc.SmartCoffeeMachineBlockingStub coffeeMachineStub,
                                         SmartCoffeeMachineGrpc.SmartCoffeeMachineStub asyncCoffeeMachineStub) {
        this.coffeeMachineStub = coffeeMachineStub;
        this.asyncCoffeeMachineStub = asyncCoffeeMachineStub;
    }

    public String brewCoffee(CoffeeType coffeeType, Deadline deadline) {
        try {
            BrewCoffeeRequest request = BrewCoffeeRequest.newBuilder()
                    .setCoffeeType(coffeeType)
                    .build();
            ActionResponse response = coffeeMachineStub
                    .withDeadline(deadline)
                    .brewCoffee(request);
            return "Coffee brewed: " + response.toString();
        } catch (StatusRuntimeException e) {
            return "Error: " + e.getStatus().getDescription();
        }
    }

    public String checkInventory(Optional<InventoryItem> itemOptional) {
        StringBuilder responseBuilder = new StringBuilder("Inventory Status:\n");
        try {
            CheckInventoryRequest.Builder requestBuilder = CheckInventoryRequest.newBuilder();
            itemOptional.ifPresent(requestBuilder::setItem);
            CheckInventoryRequest request = requestBuilder.build();

            Iterator<InventoryResponse> responses = coffeeMachineStub.checkInventory(request);

            while (responses.hasNext()) {
                InventoryResponse response = responses.next();
                responseBuilder.append("Item: ").append(response.getItem())
                        .append(", Quantity: ").append(response.getQuantity())
                        .append("\n");
            }
        } catch (StatusRuntimeException e) {
            return "Error: " + e.getStatus().getDescription();
        }

        return responseBuilder.toString();
    }

    public String refillInventory(Map<InventoryItem, Integer> itemsToRefill) {
        try {
            StreamObserver<RefillItemRequest> requestObserver = asyncCoffeeMachineStub.refillInventory(new StreamObserver<>() {
                @Override
                public void onNext(InventoryResponse response) {
                    System.out.println("Last refilled item: " + response.getItem() + ", Total items refilled: " + response.getQuantity());
                }

                @Override
                public void onError(Throwable t) {
                    System.err.println("Error: " + t.getMessage());
                }

                @Override
                public void onCompleted() {
                    System.out.println("Refill completed successfully.");
                }
            });

            for (Map.Entry<InventoryItem, Integer> entry : itemsToRefill.entrySet()) {
                RefillItemRequest request = RefillItemRequest.newBuilder()
                        .setItem(entry.getKey())
                        .setQuantity(entry.getValue())
                        .build();
                requestObserver.onNext(request);
            }

            // Mark the end of requests
            requestObserver.onCompleted();
            return "Refill process initiated.";
        } catch (StatusRuntimeException e) {
            return "Error: " + e.getStatus().getDescription();
        }
    }
}
