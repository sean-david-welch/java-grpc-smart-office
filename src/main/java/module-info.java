module services.clientui {
    requires javafx.controls;
    requires javafx.fxml;


    opens client.ClientUI.src.main.java.services.clientui to javafx.fxml;
    exports client.ClientUI.src.main.java.services.clientui;
}