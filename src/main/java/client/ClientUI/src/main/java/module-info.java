module services.clientui {
    requires javafx.controls;
    requires javafx.fxml;


    opens services.clientui to javafx.fxml;
    exports services.clientui;
}