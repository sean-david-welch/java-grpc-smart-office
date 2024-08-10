package client.ClientUI.src.main.java.services.clientui;

import client.GrpcClient;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class ClientUI extends Application {
    private GrpcClient grpcClient;

    @Override
    public void start(Stage primaryStage) {
        grpcClient = new GrpcClient("localhost", 8080);

        Label responseLabel = new Label("Response will be displayed here");

        Button accessButton = new Button("Unlock Door");
        accessButton.setOnAction(event -> {
            String response = grpcClient.accessControl();
            responseLabel.setText("Access control response: " + response);
        });

        VBox vbox = new VBox(10, accessButton, responseLabel);
        Scene scene = new Scene(vbox, 300, 200);

        primaryStage.setScene(scene);
        primaryStage.setTitle("gRPC Client UI");
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        grpcClient.shutdown();
        super.stop();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
