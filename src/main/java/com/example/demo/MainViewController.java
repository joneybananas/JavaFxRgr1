package com.example.demo;

import com.example.account.Account;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class MainViewController {
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
    private TableColumn<Account, String> sum;

    @FXML
    private TableColumn<Account, String> weight;

    private void initData() {
        JThread jThread = new JThread("load", accountList);
        jThread.loadFile();
        this.accountList = jThread.getAccountList();
        Logger.log(String.format("Запуск приложения"));
    }

    @FXML
    private void initialize() {
        table.setPlaceholder(new Label("Банковские счета отсутствуют"));
        // Загрузка данных из файла
        initData();

        // Привязка колонок таблицы
        number.setCellValueFactory(new PropertyValueFactory<Account, String>("number"));
        sum.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(String.valueOf(cellData.getValue().getSum())));
        weight.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(String.valueOf(cellData.getValue().getWeight())));

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
            Logger.log(String.format("Номер счета %s изменился на %s", event.getOldValue(), account.getNumber()));
        });

        sum.setCellFactory(TextFieldTableCell.<Account> forTableColumn());
        sum.setOnEditCommit( (TableColumn.CellEditEvent<Account, String> event) -> {
            String oldValue = event.getOldValue();
            String sum = event.getNewValue();

            try {
                Double.parseDouble(sum);
            } catch (NumberFormatException e) {
                table.refresh();
                return;
            }

            TablePosition<Account, String> pos = event.getTablePosition();

            int row = pos.getRow();
            Account account = event.getTableView().getItems().get(row);

            account.setSum(Double.parseDouble(sum));
            Logger.log(String.format("Сумма на счету %s изменилась с %s на %s", account.getNumber(), oldValue, sum));
        });

        weight.setCellFactory(TextFieldTableCell.<Account> forTableColumn());
        weight.setOnEditCommit( (TableColumn.CellEditEvent<Account, String> event) -> {
            String oldValue = event.getOldValue();
            String weight = event.getNewValue();

            try {
                Double.parseDouble(weight);
            } catch (NumberFormatException e) {
                table.refresh();
                return;
            }

            TablePosition<Account, String> pos = event.getTablePosition();

            int row = pos.getRow();
            Account account = event.getTableView().getItems().get(row);

            account.setSum(Double.parseDouble(weight));
            Logger.log(String.format("Вес каминя  %s изменён с %s на %s", account.getNumber(), oldValue, weight));
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
            Account account = table.getSelectionModel().getSelectedItem();
            accountList.remove(account);
            filterAndRender();
            Logger.log(String.format("Удален счет с номером %s", account.getNumber()));
        }
    }

    @FXML
    void onSaveButtonClick () {
        JThread jThread = new JThread("save", accountList);
        jThread.saveFile();
        Logger.log(String.format("Сохранение изменений"));
    }

    @FXML
    void onCountButtonClick () {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("countModal.fxml"));

        try {
            Parent root = loader.load();
            CountModalController controller = loader.getController();
            controller.initData(accountList);
            Stage stage = new Stage();
            stage.setTitle("Сводка по счетам");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void onSearchInput () {
        filterAndRender();
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

    private void addAccount (FXMLLoader loader) {
        AddModalController addModalController = loader.getController();
        String number = addModalController.getNumber();
        double sum = addModalController.getSum();
        double weight = addModalController.getWeight();

        if (addModalController.isSubmitted()) {
            Account account = new Account(number, sum, weight);
            accountList.add(account);
            filterAndRender();
            Logger.log(String.format("Добавлен новый счет с номером %s и суммой %s", account.getNumber(), account.getSum()));
        }
    }

    private void filterAndRender() {
        String searchStr = searchInput.getText();
        table.setItems(accountList.filtered(account -> account.getNumber().contains(searchStr)));
    }
}