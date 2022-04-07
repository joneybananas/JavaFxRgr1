package com.example.demo;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddModalController {

    @FXML
    private Button button;

    @FXML
    private TextField number, sum,weight;

    @FXML
    private Label error;

    private String number_;
    private double sum_;
    private double weight_;

    private boolean submitted = false;


    public String getNumber() {
        return number_;
    }

    public double getSum() {
        return sum_;
    }

    public double getWeight() {
        return weight_;
    }


    public boolean isSubmitted() {
        return submitted;
    }


    @FXML
    public void onSubmit () {
        number_ = number.getText();

        if (number_.isEmpty()) {
            error.setText("Поля не должны быть пустыми");
            return;
        }

        try {
            sum_ = Double.parseDouble(sum.getText());
        } catch (NumberFormatException e) {
            error.setText("Сумма должна быть числом");
            return;
        }

        try {
            weight_ = Double.parseDouble(weight.getText());
        } catch (NumberFormatException e) {
            error.setText("Вес должен быть числом");
            return;
        }


        submitted = true;
        Stage stage = (Stage) button.getScene().getWindow();
        stage.close();
    }
}
