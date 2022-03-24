package com.example.demo;

import com.example.account.Account;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloController {
    private ObservableList<Account> accountList = FXCollections.observableArrayList();

    @FXML
    private Button addBtn, deleteBtn, countBtn, saveBtn;

    @FXML
    private TextField searchInput;

    @FXML
    private TableView<Account> table;

    @FXML
    private TableColumn<Account, String> number;

    @FXML
    private TableColumn<Account, Double> sum;

    @FXML
    private TableColumn<Account, Boolean> blocked;

    private void initData() {
        JThread jThread = new JThread("load", accountList);
        jThread.loadFile();
        this.accountList = jThread.getAccountList();
    }

    @FXML
    private void initialize() {
        // Загрузка данных из файла
        initData();

        // Привязка колонок таблицы
        number.setCellValueFactory(new PropertyValueFactory<Account, String>("number"));
        sum.setCellValueFactory(new PropertyValueFactory<Account, Double>("sum"));
        blocked.setCellValueFactory(new PropertyValueFactory<Account, Boolean>("blocked"));

        // Отрисовка сущностей в таблице
        table.setItems(accountList);

        // Редактирование полей в таблице
        number.setCellFactory(TextFieldTableCell.<Account> forTableColumn());
        number.setOnEditCommit((TableColumn.CellEditEvent<Account, String> event) -> {
            TablePosition<Account, String> pos = event.getTablePosition();

            String number = event.getNewValue();

            int row = pos.getRow();
            Account account = event.getTableView().getItems().get(row);

            account.setNumber(number);
        });
    }

    @FXML
    void onAddButtonClick () {
        FXMLLoader loader = createWindow("addModal.fxml", "Добавление счета");
        addAccount(loader);
    }

    @FXML
    void onDeleteButtonClick () {
        int indexRemove = table.getSelectionModel().getSelectedIndex();

        if (indexRemove != -1) {
            accountList.remove(indexRemove);
            table.setItems(accountList);
        }
    }

    @FXML
    void onSaveButtonClick () {
        JThread jThread = new JThread("save", accountList);
        jThread.saveFile();
    }

    private FXMLLoader createWindow(String FXMLName,String stageTitle) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(FXMLName));

        try {
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle(stageTitle);
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return loader;
    }

    public void addAccount (FXMLLoader loader) {
        AddModalController addModalController = loader.getController();
        String number = addModalController.getNumber();
        double sum = addModalController.getSum();

        if (addModalController.isSubmitted()) {
            accountList.add(new Account(number, sum, false));
            table.setItems(accountList);
        }
    }
}