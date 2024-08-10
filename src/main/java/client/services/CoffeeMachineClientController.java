package client.services;

import io.grpc.StatusRuntimeException;
import services.smartCoffee.BrewCoffeeRequest;
import services.smartCoffee.CoffeeType;
import services.smartCoffee.SmartCoffeeMachineGrpc;

public class CoffeeMachineClientController {
    private final SmartCoffeeMachineGrpc.SmartCoffeeMachineBlockingStub coffeeMachineStub;

    public CoffeeMachineClientController(SmartCoffeeMachineGrpc.SmartCoffeeMachineBlockingStub coffeeMachineStub) {
        this.coffeeMachineStub = coffeeMachineStub;
    }

    public String brewCoffee(CoffeeType coffeeType) {
        try {
            BrewCoffeeRequest request = BrewCoffeeRequest.newBuilder()
                    .setCoffeeType(coffeeType)
                    .build();
            services.smartCoffee.ActionResponse response = coffeeMachineStub.brewCoffee(request);
            return "Coffee brewed: " + response.toString();
        } catch (StatusRuntimeException e) {
            return "Error: " + e.getStatus().getDescription();
        }
    }
}
