module com.example.quizkampenclientserver {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.quizkampenclientserver to javafx.fxml;
    exports com.example.quizkampenclientserver;
}