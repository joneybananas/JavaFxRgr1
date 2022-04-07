package com.example.demo;

import com.example.account.Gemstone;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class CountModalController {

    @FXML
    private Label  WeightSumLabel, sumLabel;

    public void initData (ObservableList<Gemstone> gemstoneList) {
        final double[] sum = {0};
        final double[] weightSum = {0};

        gemstoneList.forEach(gemstone -> {
                weightSum[0] += gemstone.getWeight()   ;
            sum[0] += gemstone.getSum();
        });


        WeightSumLabel.setText(WeightSumLabel.getText() + weightSum[0]);
        sumLabel.setText(sumLabel.getText() + sum[0]);
    }
}
