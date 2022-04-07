package com.example.demo;

import com.example.account.Gemstone;
import javafx.beans.property.*;
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

public class MainViewController {
    private ObservableList<Gemstone> gemstoneList = FXCollections.observableArrayList();

    @FXML
    private Button addBtn, deleteBtn, countBtn, saveBtn;

    @FXML
    private TextField searchInput;

    @FXML
    private TableView<Gemstone> table;

    @FXML
    private TableColumn<Gemstone, String> number;

    @FXML
    private TableColumn<Gemstone, String> sum;

    @FXML
    private TableColumn<Gemstone, String> weight;

    private void initData() {
        JThread jThread = new JThread("load", gemstoneList);
        jThread.loadFile();
        this.gemstoneList = jThread.getAccountList();
        Logger.log(String.format("Запуск приложения"));
    }

    @FXML
    private void initialize() {
        table.setPlaceholder(new Label("Банковские счета отсутствуют"));
        // Загрузка данных из файла
        initData();

        // Привязка колонок таблицы
        number.setCellValueFactory(new PropertyValueFactory<Gemstone, String>("number"));
        sum.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(String.valueOf(cellData.getValue().getSum())));
        weight.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(String.valueOf(cellData.getValue().getWeight())));

        // Отрисовка сущностей в таблице
        table.setItems(gemstoneList);

        // Редактирование полей в таблице
        number.setCellFactory(TextFieldTableCell.<Gemstone> forTableColumn());
        number.setOnEditCommit((TableColumn.CellEditEvent<Gemstone, String> event) -> {
            TablePosition<Gemstone, String> pos = event.getTablePosition();

            String number = event.getNewValue();

            int row = pos.getRow();
            Gemstone gemstone = event.getTableView().getItems().get(row);

            gemstone.setNumber(number);
            Logger.log(String.format("Номер счета %s изменился на %s", event.getOldValue(), gemstone.getNumber()));
        });

        sum.setCellFactory(TextFieldTableCell.<Gemstone> forTableColumn());
        sum.setOnEditCommit( (TableColumn.CellEditEvent<Gemstone, String> event) -> {
            String oldValue = event.getOldValue();
            String sum = event.getNewValue();

            try {
                Double.parseDouble(sum);
            } catch (NumberFormatException e) {
                table.refresh();
                return;
            }

            TablePosition<Gemstone, String> pos = event.getTablePosition();

            int row = pos.getRow();
            Gemstone gemstone = event.getTableView().getItems().get(row);

            gemstone.setSum(Double.parseDouble(sum));
            Logger.log(String.format("Сумма на счету %s изменилась с %s на %s", gemstone.getNumber(), oldValue, sum));
        });

        weight.setCellFactory(TextFieldTableCell.<Gemstone> forTableColumn());
        weight.setOnEditCommit( (TableColumn.CellEditEvent<Gemstone, String> event) -> {
            String oldValue = event.getOldValue();
            String weight = event.getNewValue();

            try {
                Double.parseDouble(weight);
            } catch (NumberFormatException e) {
                table.refresh();
                return;
            }

            TablePosition<Gemstone, String> pos = event.getTablePosition();

            int row = pos.getRow();
            Gemstone gemstone = event.getTableView().getItems().get(row);

            gemstone.setSum(Double.parseDouble(weight));
            Logger.log(String.format("Вес каминя  %s изменён с %s на %s", gemstone.getNumber(), oldValue, weight));
        });
    }

    @FXML
    void onAddButtonClick () {
        FXMLLoader loader = createWindow("addModal.fxml", "Добавление камня");
        addAccount(loader);
    }

    @FXML
    void onDeleteButtonClick () {
        int indexRemove = table.getSelectionModel().getSelectedIndex();

        if (indexRemove != -1) {
            Gemstone gemstone = table.getSelectionModel().getSelectedItem();
            gemstoneList.remove(gemstone);
            filterAndRender();
            Logger.log(String.format("Удален счет с номером %s", gemstone.getNumber()));
        }
    }

    @FXML
    void onSaveButtonClick () {
        JThread jThread = new JThread("save", gemstoneList);
        jThread.saveFile();
        Logger.log(String.format("Сохранение изменений"));
    }

    @FXML
    void onCountButtonClick () {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("countModal.fxml"));

        try {
            Parent root = loader.load();
            CountModalController controller = loader.getController();
            controller.initData(gemstoneList);
            Stage stage = new Stage();
            stage.setTitle("Сводка по камням");
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
            Gemstone gemstone = new Gemstone(number, sum, weight);
            gemstoneList.add(gemstone);
            filterAndRender();
            Logger.log(String.format("Добавлен новый счет с номером %s и суммой %s", gemstone.getNumber(), gemstone.getSum()));
        }
    }

    private void filterAndRender() {
        String searchStr = searchInput.getText();
        table.setItems(gemstoneList.filtered(gemstone -> gemstone.getNumber().contains(searchStr)));
    }
}