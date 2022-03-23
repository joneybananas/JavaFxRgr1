package com.example.demo;

import com.example.account.Account;
import com.example.addModal.AddModalController;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;

import java.io.IOException;
import java.util.Optional;

public class HelloController {
    private Account account;

    @FXML
    private Button addBtn;

    @FXML
    private Button countBtn;

    @FXML
    private TextField searchInput;

    @FXML
    private TableView<?> table;

    @FXML
    private TextField addModalNumber;

    @FXML
    private TextField addModalSum;

    @FXML
    void onAddButtonClick(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("addModal.fxml"));
        DialogPane addDialogPane = fxmlLoader.load();

        HelloController addModelController = fxmlLoader.getController();

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setDialogPane(addDialogPane);
        dialog.setTitle("Добавить счет");

        Optional<ButtonType> clickedButton = dialog.showAndWait();

        if (clickedButton.get() == ButtonType.OK) {
            addAccount();
        }
    }

    public void addAccount () {
        this.account = new Account();

        System.out.println(this.account.getNumber());

        StringProperty x = new SimpleStringProperty(this.account.getNumber());

        addModalNumber.textProperty().bindBidirectional(x);
    }
}