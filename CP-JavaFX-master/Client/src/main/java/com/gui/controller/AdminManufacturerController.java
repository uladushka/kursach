package com.gui.controller;

import com.SQLsupport.DBClass.Manufacturer;
import com.SQLsupport.DBClass.Product;
import com.implementation.client.OwnClient;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.Vector;

import static com.gui.Constants.*;
import static com.gui.LanguageSupport.*;

public class AdminManufacturerController extends AdminMenuController{
    private ObservableList<Manufacturer> list, selectableManufacturerList;
    protected OwnClient client;
    @FXML
    private TableColumn<Manufacturer, Integer> countryColumn;

    @FXML
    private TableColumn<Manufacturer, Integer> emailColumn;

    @FXML
    private TableColumn<Manufacturer, Integer> idColumn;

    @FXML
    private TableView<Manufacturer> manufacturerTable;

    @FXML
    private TableColumn<Manufacturer, String> nameColumn;

    @FXML
    private TableColumn<Manufacturer, Integer> ratingColumn;

    @FXML
    private Button addManufButton;

    @FXML
    private TextField contryField;

    @FXML
    private TextField countField;;

    @FXML
    private Button deleteManufButton;

    @FXML
    private TextField emailField;

    @FXML
    private Button languageButton;

    @FXML
    private TextField nameField;

    @FXML
    private TextField ratingField;

    @FXML
    private TableColumn<Manufacturer, Integer> course_work;

    @FXML
    private Button createReport;

    @FXML
    void initialize() {
        client = OwnClient.getInstance(); // Инициализация клиента

        if (client == null) {
            System.out.println("Клиент не инициализирован.");
            return;
        }

        this.initMainScene();

        idColumn.setCellValueFactory(new PropertyValueFactory<Manufacturer, Integer>("idmark"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<Manufacturer, String>("first_name"));
        countryColumn.setCellValueFactory(new PropertyValueFactory<Manufacturer, Integer>("maths"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<Manufacturer, Integer>("programming"));
        ratingColumn.setCellValueFactory(new PropertyValueFactory<Manufacturer, Integer>("unity"));
        course_work.setCellValueFactory(new PropertyValueFactory<Manufacturer, Integer>("course_work"));

        SelectAllManufacturers();
    }


    public void initMainScene(){

        super.initMainScene();

        list= FXCollections.observableArrayList();
        selectableManufacturerList=FXCollections.observableArrayList();

        createReport.setOnMouseClicked(event -> {createReport();});
        addManufButton.setOnMouseClicked(event -> {createManuf();});
        deleteManufButton.setOnMouseClicked(event -> {deleteOneManuf();});

        manufacturerTable.getSelectionModel().selectedItemProperty().addListener(
                (obs,oldSelection,newSelection)->{
                    if(newSelection!=null){
                        selectableManufacturerList.clear();
                        selectableManufacturerList.add(manufacturerTable.getSelectionModel().getSelectedItem());
                    }
                }
        );

        languageButton.setOnMouseClicked(event -> {
            int language_count1=client.isRussianLanguage()?LANGUAGE_ENGLISH:LANGUAGE_RUSSIAN;
            this.switchLanguage(language_count1);
            client.switchLanguage();
        });

    }

    public void SelectAllManufacturers(){
        super.client.sendDataToServer("select all marks");
        super.client.sendDataToServer(" ");
        Vector<Manufacturer> manufacturers = super.client.receiveManufacturers();
        list.clear();
        list.addAll(manufacturers);
        manufacturerTable.setItems(list);
    }


    public void createManuf() {
        String idmark = countField.getText().trim();
        String programming = emailField.getText().trim();
        String rating = ratingField.getText().trim();
        String maths = contryField.getText().trim();
        String first_name = nameField.getText().trim();

        // Проверка на пустоту полей
        if (first_name.isEmpty() || idmark.isEmpty() || programming.isEmpty() || maths.isEmpty() || rating.isEmpty()) {
            System.out.println("Все поля должны быть заполнены.");
            return;
        }

        int idstudent = 0;
        TableView<Manufacturer> tableView = manufacturerTable.getSelectionModel().getTableView();
        ObservableList<Manufacturer> observableList = tableView.getItems();

        if (!observableList.isEmpty()) {
            idstudent = observableList.get(0).getIdmark() + 1; // Увеличиваем id
        } else {
            System.out.println("Список производителей пуст. Устанавливаем id по умолчанию.");
            idstudent = 1; // Устанавливаем значение по умолчанию
        }

        // Логирование перед отправкой данных
        System.out.println("Отправка данных на сервер: " + idstudent + ", " + first_name + ", " + idmark + ", " + programming + ", " + maths + ", " + rating);

        // Отправка данных на сервер
        client.sendDataToServer("create manufacturer");
        client.sendDataToServer(idstudent + "@@@" + first_name + "@@@" + idmark + "@@@" + programming + "@@@" + maths + "@@@" + rating);

        if (client.receiveResult()) {
            this.SelectAllManufacturers();
        } else {
            System.out.println("Ошибка при создании производителя.");
        }
    }
    private void createReport()
    {
        client.sendDataToServer("print marks");
        client.sendDataToServer(" ");
        client.receiveFilePath();
    }

    private void deleteOneManuf() {
        Manufacturer manufacturer=selectableManufacturerList.get(0);
        if (manufacturer==null)
            return;
        client.sendDataToServer("delete one manufacturer");
        int id=manufacturer.getIdmark();
        client.sendDataToServer(Integer.toString(id));

        if(client.receiveResult()){
            this.SelectAllManufacturers();
        }
    }

    @Override
    protected void switchLanguage(int language_count){
        super.switchLanguage(language_count);
        headLabel.setText(LABEL_MANUFACTURER_TEXT[language_count]);
        idColumn.setText(MANUFACTURER_NUMBER_TEXT[language_count]);
        nameColumn.setText(MANUFACTURER_NAME_TEXT[language_count]);
        countryColumn.setText(MANUFACTURER_COUNTRY_TEXT[language_count]);
        emailColumn.setText(MANUFACTURER_EMAIL_TEXT[language_count]);
        ratingColumn.setText(MANUFACTURER_RATING_TEXT[language_count]);
    }
}
