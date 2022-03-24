module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.demo to javafx.fxml;
    opens com.example.account to main_view.fxml;
    exports com.example.demo;
    exports com.example.account;
}