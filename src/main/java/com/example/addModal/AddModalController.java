package com.example.addModal;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class AddModalController {

    @FXML
    private TextField addModalNumber;

    @FXML
    private TextField addModalSum;

    public void printNumber () {
        System.out.println(addModalNumber.getText());
    }

}
