package services.smartCoffee;

import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.stream.Stream;

import static org.mockito.Mockito.*;

public class SmartCoffeeMachineImplTest {

    @Mock
    private Connection mockConnection;

    @Mock
    private PreparedStatement mockPreparedStatement;

    @Mock
    private ResultSet mockResultSet;

    @Mock
    private StreamObserver<ActionResponse> actionResponseObserver;

    @Mock
    private StreamObserver<InventoryResponse> inventoryResponseObserver;

    @InjectMocks
    private SmartCoffeeMachineImpl smartCoffeeMachine;

    @BeforeEach
    public void setup() throws SQLException {
        MockitoAnnotations.openMocks(this);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
    }

    @Test
    public void testBrewCoffeeSuccess() throws SQLException {
        BrewCoffeeRequest request = BrewCoffeeRequest.newBuilder()
                .setCoffeeType(CoffeeType.AMERICANO)
                .build();

        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt("quantity")).thenReturn(100);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

        smartCoffeeMachine.brewCoffee(request, actionResponseObserver);

        verify(actionResponseObserver).onNext(any(ActionResponse.class));
        verify(actionResponseObserver).onCompleted();
    }

    @Test
    public void testBrewCoffeeInsufficientStock() throws SQLException {
        BrewCoffeeRequest request = BrewCoffeeRequest.newBuilder()
                .setCoffeeType(CoffeeType.AMERICANO)
                .build();

        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt("quantity")).thenReturn(0);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

        smartCoffeeMachine.brewCoffee(request, actionResponseObserver);

        verify(actionResponseObserver).onNext(argThat(response -> !response.getSuccess()));
        verify(actionResponseObserver).onCompleted();
    }


    @Test
    public void testCheckInventoryAllItemsSuccess() throws SQLException {
        CheckInventoryRequest request = CheckInventoryRequest.newBuilder().build();

        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt("quantity")).thenReturn(50);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

        smartCoffeeMachine.checkInventory(request, inventoryResponseObserver);

        int expectedCalls = (int) Stream.of(InventoryItem.values())
                .filter(item -> item != InventoryItem.UNKNOWN_ITEM && item != InventoryItem.UNRECOGNIZED)
                .count();
        verify(inventoryResponseObserver, times(expectedCalls)).onNext(any(InventoryResponse.class));
        verify(inventoryResponseObserver).onCompleted();
    }


    @Test
    public void testCheckInventorySpecificItemSuccess() throws SQLException {
        CheckInventoryRequest request = CheckInventoryRequest.newBuilder()
                .setItem(InventoryItem.COFFEE_BEANS)
                .build();

        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt("quantity")).thenReturn(50);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

        smartCoffeeMachine.checkInventory(request, inventoryResponseObserver);

        verify(inventoryResponseObserver).onNext(argThat(response ->
                response.getItem() == InventoryItem.COFFEE_BEANS &&
                        response.getQuantity() == 50 &&
                        response.getSuccess()
        ));
        verify(inventoryResponseObserver).onCompleted();
    }

    @Test
    public void testRefillInventorySuccess() throws SQLException {
        RefillItemRequest request = RefillItemRequest.newBuilder()
                .setItem(InventoryItem.COFFEE_BEANS)
                .setQuantity(100)
                .build();

        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt("quantity")).thenReturn(100);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

        StreamObserver<RefillItemRequest> refillObserver = smartCoffeeMachine.refillInventory(inventoryResponseObserver);

        refillObserver.onNext(request);
        refillObserver.onCompleted();

        verify(inventoryResponseObserver).onNext(argThat(response ->
                response.getItem() == InventoryItem.COFFEE_BEANS &&
                        response.getQuantity() == 1 &&
                        response.getSuccess()
        ));
        verify(inventoryResponseObserver).onCompleted();
    }
}