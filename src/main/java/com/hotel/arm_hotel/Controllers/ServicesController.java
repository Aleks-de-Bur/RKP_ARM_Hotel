package com.hotel.arm_hotel.Controllers;

import com.hotel.arm_hotel.ARMHotel;
import com.hotel.arm_hotel.Classes.Services;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.Optional;

/** Данный класс отвечает за работу с услугами. В нём происходит получение данных из БД, вывод их в таблицу на форме,
 * а также переход к редактированию услуги или добавлению новой. */
public class ServicesController {
    private ObservableList<Services> services = FXCollections.observableArrayList();

    @FXML
    private TableView<Services> serviceTable;
    @FXML
    private TableColumn<Services, String> serviceTitleColumn;
    @FXML
    private TableColumn<Services, Integer> servicePriceColumn;

    private Stage dialogStage;
    int serviceId;

    public void setAddStage(Stage addStage) {
        this.dialogStage = addStage;
    }

    @FXML
    void initialize() {
        //Параметры таблицы Клиенты
        serviceTitleColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getTitle()));
        servicePriceColumn.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getPrice()));

        update();
    }

    /** Метод onAddService открывает диалоговое окно с помощью которого можно добавить новую услугу. */
    public void onAddService(ActionEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader();

        loader.setLocation(ARMHotel.class.getResource("addEditService-view.fxml"));
        Parent page = null;
        try {
            page = loader.load();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Ошибка открытия формы", ButtonType.OK);
            alert.showAndWait();
        }
        Stage addStage = new Stage();
        addStage.setTitle("Добавление новой услуги");
        addStage.initModality(Modality.APPLICATION_MODAL);
        addStage.initOwner(dialogStage);
        assert page != null;
        Scene scene = new Scene(page);
        addStage.setScene(scene);
        AddEditServiceController controller = loader.getController();
        controller.setAddStage(addStage);
        addStage.showAndWait();

        update();
    }

    /** Метод onEditService открывает диалоговое окно с помощью которого можно редактировать выбранную услугу. */
    public void onEditService(ActionEvent actionEvent) {
        if (serviceTable.getSelectionModel().getSelectedItem() == null){
            Alert alert = new Alert(Alert.AlertType.WARNING, "Услуга не выбрана", ButtonType.OK);
            alert.showAndWait();
            return;
        }
        FXMLLoader loader = new FXMLLoader();

        loader.setLocation(ARMHotel.class.getResource("addEditService-view.fxml"));
        Parent page = null;
        try {
            page = loader.load();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Ошибка открытия формы", ButtonType.OK);
            alert.showAndWait();
        }
        Stage addStage = new Stage();
        addStage.setTitle("Добавление новой услуги");
        addStage.initModality(Modality.APPLICATION_MODAL);
        addStage.initOwner(dialogStage);
        assert page != null;
        Scene scene = new Scene(page);
        addStage.setScene(scene);
        AddEditServiceController controller = loader.getController();
        controller.setAddStage(addStage);
        controller.setServices(serviceTable.getSelectionModel().getSelectedItem()); // ИСПРАВИТЬ
        addStage.showAndWait();

        update();
    }

    /** Метод onDeleteService удаляет выбранную услугу из базы данных. */
    public void onDeleteService(ActionEvent actionEvent) {
        if (serviceTable.getSelectionModel().getSelectedItem() == null){
            Alert alert = new Alert(Alert.AlertType.WARNING, "Услуга не выбрана", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        Alert alert1 = new Alert(Alert.AlertType.CONFIRMATION);
        alert1.setTitle("Удаление");
        alert1.setHeaderText("Вы уверены, что хотите удалить выбранную услугу?");

        ButtonType buttonTypeOne = new ButtonType("Да");
        ButtonType buttonTypeTwo = new ButtonType("Нет", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert1.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo);

        Optional<ButtonType> result = alert1.showAndWait();
        if (result.get() == buttonTypeOne) {
            serviceId = serviceTable.getSelectionModel().getSelectedItem().getServiceId();
            Statement statement = null;
            try {
                statement = ARMHotel.getConnection().createStatement();
            } catch (SQLException e) {
                return;
            }
            try {
                ResultSet resultSet = statement.executeQuery("Delete from Services Where Services_id like " + serviceId);
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Услуга успешно удалёна из базы данных", ButtonType.OK);
                alert.showAndWait();
                update();

            } catch (SQLException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Ошибка удаления.", ButtonType.OK);
                alert.showAndWait();
            }
        }
    }

    /** Метод onExit закрывает данное диалоговое окно. */
    public void onExit(ActionEvent actionEvent) {
        Stage stage = (Stage) serviceTable.getScene().getWindow();
        stage.close();
    }

    /** Метод update получает данные из базы данных и выводит их в таблицу на форме. */
    void update(){
        services.clear();
        serviceTable.getItems().clear();

        Statement statement = null;
        try {
            statement = ARMHotel.getConnection().createStatement();
        } catch (SQLException e) {
            return;
        }
        try {
            //Получаем список Услуг
            ResultSet resultSet = statement.executeQuery("SELECT * FROM Services");
            while (resultSet.next()) {
                serviceId = resultSet.getInt("Services_id");
                services.add(new Services(serviceId, resultSet.getString("Title"), resultSet.getInt("Price")));
            }
            serviceTable.setItems(services);
        } catch (SQLException ex) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Не удалось вывести данные из базы данных", ButtonType.OK);
            alert.showAndWait();
        }
    }
}
